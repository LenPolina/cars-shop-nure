package com.implemica.application.repository;

import com.implemica.application.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Car entity.
 */

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    String QUERY_TO_GET_URL = "SELECT car_image_url FROM car WHERE id = :id";
    String QUERY_L = "SELECT * FROM car WHERE (car_brand = :brand or '1' = :brand) and (car_body_type = :body or '1' = :body) and (car_gearbox_type = :gearbox or '1' = :gearbox)";

    /**
     * Gets an image url by the car id.
     * @param id car id to find link to the image
     * @return link to the image
     */
    @Query(value = QUERY_TO_GET_URL, nativeQuery = true)
    String getCarImageUrlById(@Param("id") Long id);

    @Query(value = QUERY_L, nativeQuery = true)
    List<Car> findWith(@Param("brand")String brand, @Param("body")String bodyType, @Param("gearbox")String gearbox);
}
