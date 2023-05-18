package com.implemica.application.repository;

import com.implemica.application.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    void deleteAllByCar_Id(Long id);

    List<Option> getAllByCar_Id(Long id);
}
