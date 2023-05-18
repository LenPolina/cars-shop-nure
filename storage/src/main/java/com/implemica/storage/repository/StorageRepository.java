package com.implemica.storage.repository;


import com.implemica.storage.model.CarStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<CarStorage, Long> {

}
