package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/xiaji")
    @SendTo("/topic/xiaji")
    public Message greeting() throws Exception {
        Message message = new Message();
        message.setContent("下机了!");
        message.setName("超时或者挤下线");
        return message;
    }
}


