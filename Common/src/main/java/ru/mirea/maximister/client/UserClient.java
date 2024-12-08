package ru.mirea.maximister.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.mirea.maximister.dto.UserDto;



@Slf4j
public class UserClient {
    private static final String INTERNAL_TOKEN = "amogus";
    private final WebClient webClient;

    public UserClient() {
         this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8180")
                .filter(addHeaderFilter())
                .build();
    }

    private ExchangeFilterFunction addHeaderFilter() {
        return (request, next) -> {
            return next.exchange(
                    ClientRequest.from(request)
                            .header("X-Internal-Service-Token", INTERNAL_TOKEN)
                            .build()
            );
        };
    }


    public UserDto getUserByUid(long uid) {
        try {
            return webClient.get()
                    .uri("/api/v2/users/{uid}", uid)
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public UserDto getUserByEmail(String email) {
        try {
            return webClient.get()
                    .uri("/api/v2/users/email/{email}", email)
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw e;
        }
    }
}

