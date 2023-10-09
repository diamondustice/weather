package com.reactiverest.weather.exception;

public class WeatherAPIException extends RuntimeException {

    public WeatherAPIException(String message, Throwable cause) {

        super(message, cause);
    }

    public WeatherAPIException(String message) {

        super(message);
    }

}
