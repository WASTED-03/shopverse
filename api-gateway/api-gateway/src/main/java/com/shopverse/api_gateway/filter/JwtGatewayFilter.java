package com.shopverse.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<Object> {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            if (path.contains("/api/users/register") || path.contains("/api/users/login")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String role = claims.get("role", String.class);

                // Early 403 for non-ADMINs attempting write operations on products
                HttpMethod method = exchange.getRequest().getMethod();
                boolean isProductWriteRequest = path.startsWith("/api/products")
                        && (method == HttpMethod.POST
                                || method == HttpMethod.PUT
                                || method == HttpMethod.DELETE);

                if (isProductWriteRequest && !"ADMIN".equals(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}
