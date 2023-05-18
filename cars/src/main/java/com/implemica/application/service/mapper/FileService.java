package com.implemica.application.service.mapper;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface for performing operations with image files.
 */
public interface FileService {
    /**
     * Saves a file to an image storage service.
     * @param file that need to save
     * @return message about result of saving
     */
    String saveFile(MultipartFile file);

    /**
     * Deletes a file to an image storage service.
     * @param fileName that need to delete
     * @return message about result of deleting
     */
    String deleteFile(String fileName);

    /**
     * Validates a file for the required extension.
     * @param multipartFile file to validate
     * @return message about result of validation
     * @throws IOException unsuccessful attempt to read image from file
     */
    String validateFile(MultipartFile multipartFile) throws IOException;
}
