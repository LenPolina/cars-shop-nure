package com.implemica.carprice.controller;


import com.implemica.carprice.model.Price;
import com.implemica.carprice.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/price")
public class PriceController {
    private final Logger log = LoggerFactory.getLogger(PriceController.class);

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Price> getPrice(@PathVariable("id") Long carId){
        Price carPrice = priceService.getCarPrice(carId);
        return new ResponseEntity<>(carPrice, HttpStatus.OK);
    }
    @GetMapping(value = "/get/for/{id}")
    public ResponseEntity<BigDecimal> getCarPrice(@PathVariable("id") Long carId){
        BigDecimal carPrice = priceService.getCarPrice(carId).getCarPrice();
        return new ResponseEntity<>(carPrice, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addCarPrice(@RequestBody Price price){
        priceService.save(price);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping(value = "/edit")
    public ResponseEntity<String> editCarPrice(@RequestBody Price price){
        priceService.save(price);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping (value = "/delete/{id}")
    public ResponseEntity<String> deleteCarPrice(@PathVariable("id") Long carId){
        priceService.delete(carId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
