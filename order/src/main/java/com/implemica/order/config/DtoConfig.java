package com.implemica.order.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Defines services for converting dto to entity.
 */
@Configuration
public class DtoConfig {

    /**
     * Method to define ModelMapper. ModelMapper, is an object-to-object
     * framework that converts objects from one representation to another.
     * @return customized ModelMapper object
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
