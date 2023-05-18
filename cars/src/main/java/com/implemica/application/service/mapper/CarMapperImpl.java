package com.implemica.application.service.mapper;

import com.implemica.application.domain.Car;
import com.implemica.application.service.dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-27T16:56:44+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    @Override
    public Car toEntity(CarDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();
        System.out.println("dto = "+dto);

        car.setId( dto.getId() );
        car.setCarBrand( dto.getCarBrand() );
        car.setCarModel( dto.getCarModel() );
        car.setCarBodyType( dto.getCarBodyType() );
        car.setCarImageUrl( dto.getCarImageUrl() );
        car.setCarYear( dto.getCarYear() );
        car.setCarEngineVolume( dto.getCarEngineVolume() );
        car.setCarGearboxType( dto.getCarGearboxType() );
        car.setCarDescription( dto.getCarDescription() );
        System.out.println("car = "+car);
        return car;
    }

    @Override
    public CarDTO toDto(Car entity) {
        if ( entity == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();
        System.out.println("car = "+entity);
        carDTO.setId( entity.getId() );
        carDTO.setCarBrand( entity.getCarBrand() );
        carDTO.setCarModel( entity.getCarModel() );
        carDTO.setCarBodyType( entity.getCarBodyType() );
        carDTO.setCarImageUrl( entity.getCarImageUrl() );
        carDTO.setCarYear( entity.getCarYear() );
        carDTO.setCarEngineVolume( entity.getCarEngineVolume() );
        carDTO.setCarGearboxType( entity.getCarGearboxType() );
        carDTO.setCarDescription( entity.getCarDescription() );
        System.out.println("carDTO = "+carDTO);
        return carDTO;
    }

    @Override
    public List<Car> toEntity(List<CarDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Car> list = new ArrayList<Car>( dtoList.size() );
        for ( CarDTO carDTO : dtoList ) {
            list.add( toEntity( carDTO ) );
        }

        return list;
    }

    @Override
    public List<CarDTO> toDto(List<Car> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CarDTO> list = new ArrayList<CarDTO>( entityList.size() );
        for ( Car car : entityList ) {
            list.add( toDto( car ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Car entity, CarDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getCarBrand() != null ) {
            entity.setCarBrand( dto.getCarBrand() );
        }
        if ( dto.getCarModel() != null ) {
            entity.setCarModel( dto.getCarModel() );
        }
        if ( dto.getCarBodyType() != null ) {
            entity.setCarBodyType( dto.getCarBodyType() );
        }
        if ( dto.getCarImageUrl() != null ) {
            entity.setCarImageUrl( dto.getCarImageUrl() );
        }
        if ( dto.getCarYear() != null ) {
            entity.setCarYear( dto.getCarYear() );
        }
        if ( dto.getCarEngineVolume() != null ) {
            entity.setCarEngineVolume( dto.getCarEngineVolume() );
        }
        if ( dto.getCarGearboxType() != null ) {
            entity.setCarGearboxType( dto.getCarGearboxType() );
        }
        if ( dto.getCarDescription() != null ) {
            entity.setCarDescription( dto.getCarDescription() );
        }
    }
}
