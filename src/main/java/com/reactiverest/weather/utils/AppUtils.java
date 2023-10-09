package com.reactiverest.weather.utils;

import com.reactiverest.weather.dto.WeatherDay;
import com.reactiverest.weather.entity.Period;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class AppUtils {

    public static boolean isPeriodToday(Period day) {

       LocalDate date = ZonedDateTime
                .parse(day.getStartTime())
                .toLocalDate();

       return date.isEqual(LocalDate.now());
    }

    public static WeatherDay dayToDto(Period day) {
        return new WeatherDay(day.getName(), toCelsius(day.getTemperature()), day.getShortForecast());

    }

    public static String toCelsius(String tempF){
        double fahrenheit = Double.parseDouble(tempF);
        double celsius = (( 5 *(fahrenheit - 32.0)) / 9.0);
        return String.format("%.1f",celsius);
    }

}
