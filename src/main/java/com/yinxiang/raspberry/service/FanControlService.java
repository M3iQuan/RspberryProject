package com.yinxiang.raspberry.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FanControlService {

    /*public FanControlService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://192.168.56.178:8080").build();
    }*/

    public String getJson(Long dev_id, String ip, int fan_speed) {
        String msg = null;
        WebClient webClient = WebClient.builder().baseUrl("http://" + ip + ":8080").build();
        try {
            msg = webClient.get().uri("/fanControl?dev_id={?}&fan_speed={?}", dev_id, fan_speed).retrieve().bodyToMono(String.class).block();
        }catch (Exception e) {
            if(e instanceof java.net.ConnectException){
                msg = webClient.get().uri("/fanControl?dev_id={?}&fan_speed={?}", dev_id, fan_speed).retrieve().bodyToMono(String.class).block();
            }
        }finally{
            return msg;
        }
    }
}
