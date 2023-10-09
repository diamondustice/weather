package com.reactiverest.weather.handler;

import com.reactiverest.weather.dto.WeatherDto;
import com.reactiverest.weather.service.WeatherForecastService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class WeatherForecastHandler {

    private final WeatherForecastService weatherForecastService;

    public WeatherForecastHandler(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    public Mono<ServerResponse> getWeatherToday(ServerRequest request) {

        String office = request.pathVariable("office");
        String gridX = request.pathVariable("gridX");
        String gridY = request.pathVariable("gridY");

        Mono<WeatherDto> weatherToday = weatherForecastService.getWeatherForecastToday(office, gridX, gridY);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(weatherToday, WeatherDto.class);
    }

}
