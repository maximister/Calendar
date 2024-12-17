package ru.mirea.maximister.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(JwtValidationFilter.class);
    private final WebClient.Builder webClientBuilder;

    public JwtValidationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);

            if (token != null) {
                return validateToken(token, exchange)
                        .flatMap(valid -> {
                            if (Boolean.TRUE.equals(valid)) {
                                return chain.filter(exchange);
                            } else {
                                return promptUserRegistration(exchange);
                            }
                        });
            } else {
                return promptUserRegistration(exchange);
            }
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Mono<Boolean> validateToken(String token, ServerWebExchange exchange) {
        var res =  webClientBuilder.build()
                .post()
                .uri("http://localhost:8180/api/v2/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                //.onStatus(HttpStatus::isError, response -> Mono.just(false))
                .bodyToMono(Boolean.class);
        log.info("received result for token {}", token);

        return res;
    }

    private Mono<Void> promptUserRegistration(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().set(
                HttpHeaders.LOCATION, "http://localhost:8180/api/v2/auth/signin"
        );
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}