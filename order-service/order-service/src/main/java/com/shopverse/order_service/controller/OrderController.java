package com.shopverse.order_service.controller;

import com.shopverse.order_service.model.Order;
import com.shopverse.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order placeOrder(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestHeader("Authorization") String authHeader) {
        return service.placeOrder(productId, quantity, authHeader);
    }
}
