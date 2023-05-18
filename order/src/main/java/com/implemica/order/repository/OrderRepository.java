package com.implemica.order.repository;


import com.implemica.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndUser(Long id, Long user);

    List<Order> findOrdersByUser(Long id);
}
