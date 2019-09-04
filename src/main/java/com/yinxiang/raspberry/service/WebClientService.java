package com.yinxiang.raspberry.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {
    private final WebClient webClient;

    public WebClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://192.168.56.178:8080").build();
    }

    public String getJson(Long dev_id, int fan_speed) {
        return this.webClient.get().uri("/fanControl?dev_id={?}&fan_speed={?}", dev_id, fan_speed).retrieve().bodyToMono(String.class).block();
    }
}
