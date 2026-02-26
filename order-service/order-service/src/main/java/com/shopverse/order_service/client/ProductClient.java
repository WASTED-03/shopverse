package com.shopverse.order_service.client;

import com.shopverse.order_service.dto.ProductResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClient {

    private final RestTemplate restTemplate;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductResponse getProduct(Long productId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductResponse> response = restTemplate.exchange(
                "http://PRODUCT-SERVICE/api/products/" + productId,
                HttpMethod.GET,
                entity,
                ProductResponse.class);

        return response.getBody();
    }
}
