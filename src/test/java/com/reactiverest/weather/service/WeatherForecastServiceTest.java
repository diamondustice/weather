package com.reactiverest.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactiverest.weather.dto.WeatherDto;
import com.reactiverest.weather.entity.Period;
import com.reactiverest.weather.entity.Properties;
import com.reactiverest.weather.entity.Weather;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class WeatherForecastServiceTest {

    public static MockWebServer mockBackEnd;

    private WeatherForecastService client;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        //String baseUrl = "https://api.weather.gov/gridpoints/MLB/33,70/forecast";
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        client = new WeatherForecastService(baseUrl);
    }

    @Test
    void whenGetWeatherForecastToday_thenReturnOnlyDaytimeForecast() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Weather mockWeather = new Weather(getProperties());

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockWeather))
                .addHeader("Content-Type", "application/json"));

        Mono<WeatherDto> weatherDtoMono = client
                .getWeatherForecastToday();

        StepVerifier.create(weatherDtoMono)
                .expectNextMatches(weatherDto -> weatherDto.getDaily().get(0).getDay_name()
                        .equals("Columbus Day"))
                .verifyComplete();

        // Want to make sure that the MockWebServer was sent the correct HttpRequest.
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/gridpoints/MLB/33,70/forecast", recordedRequest.getPath());

    }

    @NotNull
    private static Properties getProperties() {

        String dateToday = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        System.out.println(dateToday);

        Properties properties = new Properties();
        properties.setPeriods(List.of(
                new Period(1, "Evening", "70", "F", "Cloudy", "false", "2023-10-09T06:00:00-04:00", "2023-10-09T18:00:00-04:00"),
                new Period(2, "Columbus Day", "71", "F", "Rainy", "true", dateToday+"T06:00:00-04:00", dateToday+"T18:00:00-04:00"),
                new Period(3, "Columbus Day double", "71", "F", "Rainy", "true", dateToday+"T06:00:00-04:00", dateToday+"T18:00:00-04:00"),
                new Period(4, "Tuesday", "72", "F", "Sunny", "false", "2023-10-09T06:00:00-04:00", "2023-10-09T18:00:00-04:00"),
                new Period(5, "Evening", "73", "F", "Mostly Cloudy", "false", "2023-10-09T06:00:00-04:00", "2023-10-09T18:00:00-04:00")
        ));
        return properties;
    }

}