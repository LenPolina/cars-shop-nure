package com.implemica.order.controller;

import com.implemica.order.model.OrderDTO;
import com.implemica.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/order")
public class OrderController {

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private final OrderService orderService;

    @GetMapping(value = "/get")
    public ResponseEntity<List<OrderDTO>> getOrders(HttpServletRequest request) throws ExecutionException, InterruptedException, TimeoutException {
        List<OrderDTO> orders = orderService.getOrders(request.getRemoteUser());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping(value = "/get/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id, @RequestBody String username) throws ExecutionException, InterruptedException, TimeoutException {
        OrderDTO cartCars = orderService.getOrder(id, username);

        return new ResponseEntity<>(cartCars, HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO order) throws ExecutionException, InterruptedException, TimeoutException {
        OrderDTO orderDTO = orderService.save(order);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id){
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "HandleOrder", groupId = "orders")
    public ResponseEntity<String> handleOrder(Long id){
        String result = orderService.handleOrder(id);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
