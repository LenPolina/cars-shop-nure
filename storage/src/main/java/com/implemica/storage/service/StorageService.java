package com.implemica.storage.service;


import com.amazonaws.services.cloud9.model.BadRequestException;
import com.implemica.storage.model.CarStorage;
import com.implemica.storage.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public CarStorage getCarNumber(Long carId){
        log.info("Getting number of car by id = {}", carId);
        return storageRepository.findById(carId).get();
    }

    public void save(CarStorage carStorage) {
        CarStorage newCart = storageRepository.save(carStorage);
        log.info("Add number of cars in the storage for car = {}", newCart.getId());
    }
    public void delete(Long carId) {
        storageRepository.deleteById(carId);
    }

    public void deleteCarAmount(Long carId, Long amountToDelete) {
        try {
            CarStorage carStorage = storageRepository.findById(carId).get();

            carStorage.setCarNumber(carStorage.getCarNumber()-amountToDelete);
            log.info("Deleting car amount {} by id = {}, new amount = {}", amountToDelete, carId, carStorage.getCarNumber());
            storageRepository.save(carStorage);
        }catch (NoSuchElementException e){
            throw new BadRequestException("The car does not exist."+e.getMessage());
        }
    }
}
