package com.reactiverest.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Period {

    private int number;
    private String name;
    private String temperature;
    private String temperatureUnit;
    private String shortForecast;
    private String isDaytime;
    private String startTime;
    private String endTime;
}
