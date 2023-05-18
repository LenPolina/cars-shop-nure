package com.implemica.cart.repository;

import com.implemica.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    String QUERY_TO_GET_USER_ID = "SELECT * FROM cart WHERE cart_user = :username";
    String QUERY_TO_DELETE_ALL_BY_USER_ID = "DELETE FROM Cart c WHERE c.cart_user = :username";
    String QUERY_TO_DELETE_CAR_BY_USER_ID_AND_CAR_ID = "DELETE FROM Cart c WHERE c.cart_user = :username and c.car_id = :id";

    @Query(value = QUERY_TO_GET_USER_ID, nativeQuery = true)
    List<Cart> getCarsIdByUserId(@Param("username") String username);
    @Modifying
    @Transactional
    @Query(value = QUERY_TO_DELETE_ALL_BY_USER_ID, nativeQuery = true)
    void deleteByUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = QUERY_TO_DELETE_CAR_BY_USER_ID_AND_CAR_ID, nativeQuery = true)
    void deleteByUserAndCarId(@Param("username") String username, @Param("id") Long id);
}
