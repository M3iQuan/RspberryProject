package com.yinxiang.raspberry.controller;


import com.yinxiang.raspberry.model.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReLoginController {
    @RequestMapping("/login_p")
    public Result login() {
        Result result = new Result();
        result.setStatus(200);
        result.setDetail("尚未登录");
        return result;
    }
}
