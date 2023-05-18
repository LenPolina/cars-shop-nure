package com.implemica.application.service.mapper;

import com.implemica.application.domain.Car;
import com.implemica.application.service.dto.CarDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
public interface CarMapper extends EntityMapper<CarDTO, Car> {}
