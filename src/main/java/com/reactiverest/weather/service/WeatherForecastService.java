package com.reactiverest.weather.service;

import com.reactiverest.weather.dto.WeatherDto;
import com.reactiverest.weather.entity.Weather;
import com.reactiverest.weather.exception.WeatherAPIException;
import com.reactiverest.weather.utils.AppUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WeatherForecastService {

    private final WebClient webClient;

    public WeatherForecastService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.weather.gov")
                .build();
    }

    public WeatherForecastService(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public Mono<WeatherDto> getWeatherForecastToday() {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/gridpoints/MLB/33,70/forecast").build())
                .retrieve()
                .bodyToMono(Weather.class)
                .flatMapMany(weather -> Flux.fromIterable(weather.getProperties().getPeriods()))
                .filter(AppUtils::isToday)
                .take(1)
                .switchIfEmpty(Mono.error(new WeatherAPIException("No forecast available for today.")))
                .map(AppUtils::dayToDto)
                .collectList()
                .map(WeatherDto::new);
    }
}
