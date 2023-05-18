package com.implemica.cart.controller;

import com.implemica.cart.model.Cart;
import com.implemica.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "/getCars")
    public ResponseEntity<List<Cart>> getCars(@RequestBody String username){
        List<Cart> cartCars = cartService.getCartCars(username);
        return new ResponseEntity<>(cartCars, HttpStatus.CREATED);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addCarToCart(@RequestBody Cart cart){
        cartService.save(cart);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping (value = "/delete/{id}")
    public ResponseEntity<String> deleteCarFromCart(@PathVariable("id") Long cartId, HttpServletRequest request){
        cartService.delete(cartId, request.getRemoteUser());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @org.springframework.kafka.annotation.KafkaListener(topics = "CleanUserCart", groupId = "cars")
    public ResponseEntity<String> deleteCart(String userId){
        cartService.deleteByUser(userId);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
}
