package com.yinxiang.raspberry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/status")
public class StatusController {
    @GetMapping("/xiaji1")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String,Object> sendLateStatus() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","这个是超时下机！" );
        map.put("status","430" );
        return map;
    }

    @GetMapping("/xiaji2")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String,Object> sendOutStatus() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","这个是挤下机！" );
        map.put("status","431" );
        return map;
    }
}
