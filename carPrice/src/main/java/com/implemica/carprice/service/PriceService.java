package com.implemica.carprice.service;


import com.implemica.carprice.model.Price;
import com.implemica.carprice.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final Logger log = LoggerFactory.getLogger(PriceService.class);
    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Price getCarPrice(Long carId){
        log.info("Getting all cars from cart user = {}", carId);
        return priceRepository.findById(carId).get();
    }

    public void save(Price price) {
        Price newPrice = priceRepository.save(price);
        log.info("Add car to cart = {}", newPrice.getId());
    }
    public void delete(Long carId) {
        priceRepository.deleteById(carId);
    }
}
