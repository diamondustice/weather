package com.reactiverest.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDay {

    private String day_name;
    private String temp_high_celsius;
    private String forecast_blurp;

}
