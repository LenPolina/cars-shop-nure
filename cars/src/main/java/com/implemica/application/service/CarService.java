package com.implemica.application.service;

import com.implemica.application.service.dto.CarDTO;
import io.undertow.util.BadRequestException;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.implemica.application.domain.Car}.
 */
public interface CarService {
    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    CarDTO save(CarDTO carDTO);

    /**
     * Updates a car.
     *
     * @param carDTO the entity to update.
     * @return the persisted entity.
     */
    CarDTO update(CarDTO carDTO);

    /**
     * Partially updates a car.
     *
     * @param carDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarDTO> partialUpdate(CarDTO carDTO);

    /**
     * Get all the cars.
     *
     * @return the list of entities.
     */
    List<CarDTO> findAll();

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarDTO> findOne(Long id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    String getCarImageUrlById(Long id) throws BadRequestException;

    List<CarDTO> findFilteredCars(String brand, String bodyType, String gearbox, String minPrice, String maxPrice);
}
