package com.implemica.application.rest;

import com.implemica.application.repository.CarRepository;
import com.implemica.application.rest.errors.BadRequestAlertException;
import com.implemica.application.service.CarService;
import com.implemica.application.service.dto.CarDTO;
import com.implemica.application.service.mapper.FileService;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.implemica.application.domain.Car}.
 */
@RestController
@RequestMapping("/catalog")
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarService carService;
    private final FileService fileService;

    private final CarRepository carRepository;

    public CarResource(CarService carService, FileService fileService, CarRepository carRepository) {
        this.carService = carService;
        this.fileService = fileService;
        this.carRepository = carRepository;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param carDTO the carDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carDTO, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cars")
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO,
                                            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws URISyntaxException, IOException {
        log.debug("REST request to save Car : {}", carDTO);
        if (carDTO.getId() != null) {
            throw new BadRequestAlertException("A new car cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarDTO result = carService.save(carDTO);
        return ResponseEntity
            .created(new URI("/catalog/cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping(value = "/cars/update/image/{id}")
    public ResponseEntity<CarDTO> updateImage(@PathVariable Long id,
                                              @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        CarDTO carToEdit = carService.findOne(id).get();
        fileService.validateFile(multipartFile);

        if (carToEdit.getCarImageUrl() != null) {
            fileService.deleteFile(carToEdit.getCarImageUrl());
        }

        carToEdit.setCarImageUrl(fileService.saveFile(multipartFile));
        CarDTO car = carService.save(carToEdit);
        log.debug("REST request to save Car : {}", car);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * {@code PUT  /cars/:id} : Updates an existing car.
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cars/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody CarDTO carDTO)
        throws URISyntaxException {
        log.debug("REST request to update Car : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarDTO result = carService.update(carDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cars/:id} : Partial updates given fields of an existing car, field will ignore if it is null
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarDTO> partialUpdateCar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarDTO carDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Car partially : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarDTO> result = carService.partialUpdate(carDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
   /* @GetMapping("/cars/find")
    public List<CarDTO> getAllCars() {
        log.debug("REST request to get all Cars");
        return carService.findAll();
    }*/
    @GetMapping("/cars/find")
    public List<CarDTO> getFilteredCars(@RequestParam(required = false) String carBrand, @RequestParam(required = false) String carBody, @RequestParam(required = false) String gearbox,
                                        @RequestParam(required = false) String minPrice, @RequestParam(required = false) String maxPrice) {
        log.debug("REST request to get filtered Cars");
        List<CarDTO> filteredCars;
        if (carBrand != null && carBody!= null && gearbox!= null){
           filteredCars = carService.findFilteredCars(carBrand, carBody,gearbox, minPrice, maxPrice);
        }
        else{
            filteredCars = carService.findAll();
        }
        return filteredCars;
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the carDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        log.debug("REST request to get Car : {}", id);
        Optional<CarDTO> carDTO = carService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carDTO);
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" car.
     *
     * @param id the id of the carDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) throws BadRequestException {
        log.debug("REST request to delete Car : {}", id);
        String carImageUrl = carService.getCarImageUrlById(id);
        if (carImageUrl != null) {
            fileService.deleteFile(carImageUrl);
        }
        carService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
