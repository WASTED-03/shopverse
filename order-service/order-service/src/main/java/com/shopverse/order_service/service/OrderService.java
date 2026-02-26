package com.shopverse.order_service.service;

import com.shopverse.order_service.client.ProductClient;
import com.shopverse.order_service.dto.ProductResponse;
import com.shopverse.order_service.model.Order;
import com.shopverse.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public Order placeOrder(Long productId, Integer quantity, String authHeader) {

        ProductResponse product = productClient.getProduct(productId, authHeader);

        if (product.stock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.price() * quantity);
        order.setStatus("CREATED");

        return repository.save(order);
    }
}
