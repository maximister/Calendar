package ru.mirea.maximister.client;


import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.mirea.maximister.dto.EventDto;
import ru.mirea.maximister.dto.UserDto;

@RequiredArgsConstructor
public class EventClient {
    private static final String INTERNAL_TOKEN = "amogus";
    private final WebClient webClient;

    public EventClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
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


    public EventDto getEventById(String eventId) {
        try {
            return webClient.get()
                    .uri("/api/v2/events/{eventId}", eventId)
                    .retrieve()
                    .bodyToMono(EventDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw e;
        }
    }
}
