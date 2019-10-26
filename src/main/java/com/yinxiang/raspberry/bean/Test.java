package com.yinxiang.raspberry.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Test {
    private List<Map<String, Object>> temperature_and_humidity_data = new ArrayList<>();
    private List<Map<String, Object>> air_light_data = new ArrayList<>();
    private List<Map<String, Object>> water_data = new ArrayList<>();
    private List<Map<String, Object>> protector_data = new ArrayList<>();

    public Test() {
    }

    public List<Map<String, Object>> getTemperature_and_humidity_data() {
        return temperature_and_humidity_data;
    }

    public void setTemperature_and_humidity_data(List<Map<String, Object>> temperature_and_humidity_data) {
        this.temperature_and_humidity_data = temperature_and_humidity_data;
    }

    public List<Map<String, Object>> getAir_light_data() {
        return air_light_data;
    }

    public void setAir_light_data(List<Map<String, Object>> air_light_data) {
        this.air_light_data = air_light_data;
    }

    public List<Map<String, Object>> getWater_data() {
        return water_data;
    }

    public void setWater_data(List<Map<String, Object>> water_data) {
        this.water_data = water_data;
    }

    public List<Map<String, Object>> getProtector_data() {
        return protector_data;
    }

    public void setProtector_data(List<Map<String, Object>> protector_data) {
        this.protector_data = protector_data;
    }
}
