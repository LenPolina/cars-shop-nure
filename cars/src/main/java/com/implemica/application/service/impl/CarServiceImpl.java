package com.implemica.application.service.impl;



import com.implemica.application.domain.Car;
import com.implemica.application.repository.CarRepository;
import com.implemica.application.service.CarService;
import com.implemica.application.service.dto.CarDTO;
import com.implemica.application.service.mapper.CarMapperImpl;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;
    private static final String BASE_URL_FOR_PRICE = "http://localhost:8080/price/";

    private final CarMapperImpl carMapper;

    private final RestTemplate restTemplate;

    public CarServiceImpl(CarRepository carRepository, CarMapperImpl carMapper, RestTemplate restTemplate) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public CarDTO save(CarDTO carDTO) {
        log.debug("Request to save Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public CarDTO update(CarDTO carDTO) {
        log.debug("Request to update Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public String getCarImageUrlById(Long id) throws BadRequestException {
        if (carRepository.existsById(id)) {
            String imageUrl = carRepository.getCarImageUrlById(id);
            log.info("Fetching car image url = {} by id = {}", imageUrl, id);
            return imageUrl;
        }
        throw new BadRequestException("Car does not exist");
    }

    @Override
    public List<CarDTO> findFilteredCars(String brand, String bodyType, String gearbox, String minPrice, String maxPrice) {
        List<Car> cars = carRepository.findWith(brand, bodyType, gearbox);
        List<CarDTO> carDTOS;

        if (minPrice!=null||maxPrice!=null) {
            carDTOS = getCarDTOS(cars, minPrice, maxPrice);
        }
        else {
            carDTOS = cars.stream().map(carMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }

        return carDTOS;
    }

    private LinkedList<CarDTO> getCarDTOS(List<Car> car, String min_price, String max_price) {
        return car
            .stream()
            .filter(it -> {
                return resultOfFilters(min_price, max_price, it.getId());
            })
            .map(carMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private boolean resultOfFilters(String minPrice, String maxPrice, Long it) {
        BigDecimal price = restTemplate.getForObject(BASE_URL_FOR_PRICE + "get/for/" + it, BigDecimal.class);

        BigDecimal min_price;
        BigDecimal max_price;

        if (minPrice!=null&&maxPrice!=null){
          min_price = new BigDecimal(minPrice);
          max_price = new BigDecimal(maxPrice);
            return ((price.compareTo(min_price) == 1) && (price.compareTo(max_price) == -1));
        }
        else if (minPrice!=null){
            min_price = new BigDecimal(minPrice);
            return (price.compareTo(min_price) == 1);
        }
        else {
            max_price = new BigDecimal(maxPrice);
            return (price.compareTo(max_price) == -1);
        }
    }

    @Override
    public Optional<CarDTO> partialUpdate(CarDTO carDTO) {
        log.debug("Request to partially update Car : {}", carDTO);

        return carRepository
            .findById(carDTO.getId())
            .map(existingCar -> {
                carMapper.partialUpdate(existingCar, carDTO);

                return existingCar;
            })
            .map(carRepository::save)
            .map(carMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> findAll() {
        log.debug("Request to get all Cars");
        System.out.println("Request to get all Cars" + carRepository.findAll());
        return carRepository.findAll().stream().map(carMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarDTO> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id).map(carMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
