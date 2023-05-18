package com.implemica.cart.service;

import com.implemica.cart.model.Cart;
import com.implemica.cart.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final Logger log = LoggerFactory.getLogger(CartService.class);

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartCars(String username){
        log.info("Getting all cars from cart user = {}", username);
        return cartRepository.getCarsIdByUserId(username);
    }

    public void save(Cart cart) {
        Cart newCart = cartRepository.save(cart);
        log.info("Add car to cart = {}", newCart.getId());
    }
    public void delete(Long cartId, String remoteUser) {
        cartRepository.deleteByUserAndCarId(remoteUser, cartId);
    }

    public void deleteCart(Cart cart) {
        cartRepository.deleteById(cart.getId());
    }

    public void deleteByUser(String userId) {
        cartRepository.deleteByUser(userId.toLowerCase());
        log.info("Delete cart  for user = {}", userId.toLowerCase());
    }
}
