package com.implemica.application.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.implemica.application.rest.CarResource;
import com.implemica.application.service.mapper.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for performing operations related to files and Amazon S3 service.
 */
@Service
public class FileServiceImpl implements FileService {
    private final Logger log = LoggerFactory.getLogger(CarResource.class);
    /**
     * Amazon S3 provides storage for the Internet, and is designed to make
     * web-scale computing easier for developers. Used to store
     * and retrieve any amount of data, at any time, from anywhere on the web.
     */
    private final AmazonS3 s3;
    /**
     * The name of the bucket on Amazon S3 service to upload images.
     */
    @Value("${buketName}")
    private String buketName;
    /**
     * The value parameter for caching the image on Amazon S3 service.
     */
    @Value("${max.age.value}")
    private String maxAgeValue;
    /**
     * The constant containing a message about the successful addition of an image from Amazon S3 service.
     */
    private static final String FILE_WAS_ADDED_TO_THE_S_3_BUKET = "File {} was added to the s3 buket";
    /**
     * The constant containing a message about the successful deletion of the image from Amazon S3 service.
     */
    private static final String FILE_WAS_DELETED_FROM_THE_S_3_BUKET = "File {} was deleted from the s3 buket";
    /**
     * The constant containing a message about the result of deletion.
     */
    private static final String FILE_WAS_DELETED_SUCCESSFULLY = "File was deleted successfully";
    /**
     * The constant containing a message that the file does not exist on Amazon S3 service in the bucket.
     */
    private static final String FILE_DOES_NOT_EXIST = "File does not exist";
    /**
     * Constant containing the message of unsuccessful file validation.
     */
    private static final String FILE_IS_NOT_AN_IMAGE = "File is not an image";
    /**
     * Constant containing the message of successful file validation.
     */
    private static final String FILE_IS_AN_IMAGE = "File is an image";
    /**
     * Constant containing the link to cloud front service that provides access to view images.
     */
    private static final String LINK_TO_ACCESS_THE_FILE= "https://cars-shop-nure.s3.amazonaws.com/";
    /**
     * Constant containing the file extension to upload to S3.
     */
    private static final String FILE_EXTENSION= ".png";

    public FileServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    /**
     * Uploads a file on Amazon S3 service with a random name, the specified extension and the value for caching.
     * Catches exceptions {@link SdkClientException} on an unsuccessful upload attempt
     * or {@link IOException} if the file validation fails - the file is not an image.
     * Throws an exception {@link ResponseStatusException} with the status {@link HttpStatus#BAD_REQUEST}
     * and an error message instead.
     * @param multipartFile that need to save
     * @return link to view the image online
     */
    @Override
    public String saveFile(MultipartFile multipartFile) {
        String originalFilename = UUID.randomUUID().toString();

        try {
            File file = convertMultipartFileToFile(multipartFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl(maxAgeValue);

            InputStream targetStream = new FileInputStream(file);
            originalFilename +=FILE_EXTENSION;
            s3.putObject(buketName, originalFilename, targetStream, metadata);
            log.info(FILE_WAS_ADDED_TO_THE_S_3_BUKET, originalFilename);


            return LINK_TO_ACCESS_THE_FILE + originalFilename;

        } catch (IOException | SdkClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes a file from Amazon S3 service by link to it. Throws an exception {@link ResponseStatusException}
     * if there is no file with the specified name on Amazon S3 service in the specified bucket.
     * @param fileUrl link to file on Amazon S3 service that need to delete
     * @return result of deletion
     */
    @Override
    public String deleteFile(String fileUrl){
        try {
            //Getting the filename from a link to it
            int index = fileUrl.lastIndexOf("/");
            String fileName = fileUrl.substring(index + 1);

            s3.deleteObject(buketName, fileName);
            log.info(FILE_WAS_DELETED_FROM_THE_S_3_BUKET, fileName);
            return FILE_WAS_DELETED_SUCCESSFULLY;
        } catch (SdkClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Checks if file is an image. Throws an exception {@link ResponseStatusException}
     * if the file check for the image is unsuccessful.
     * @param multipartFile file to validate
     * @return result of validation
     * @throws IOException on unsuccessful conversion multipartFile to file in {@link FileServiceImpl#convertMultipartFileToFile}
     */
    @Override
    public String validateFile(MultipartFile multipartFile) throws IOException {
        File file = convertMultipartFileToFile(multipartFile);
        if (ImageIO.read(file) == null) {
            log.info(FILE_IS_NOT_AN_IMAGE);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FILE_IS_NOT_AN_IMAGE);
        }
        log.info(FILE_IS_AN_IMAGE);
        return FILE_IS_AN_IMAGE;
    }

    /**
     * Converts multipartFile to file.
     * @param multipartFile object that need to convert
     * @return file object with data from multipartFile
     * @throws IOException unsuccessful conversion multipartFile to file
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}
