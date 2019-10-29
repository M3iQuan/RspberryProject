package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Message;
import com.yinxiang.raspberry.model.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //接收消息的url实际上是/app/hello
    @MessageMapping("/hello")
    //把消息发到url为/topic/hello
    @SendTo("/topic/hello")
    public Message sayHello(Message message, Principal principal) throws Exception{
        System.out.println(principal.toString());
        System.out.println(UserUtils.getCurrentUser().getId());
        System.out.println(message.getName() + " : " + message.getContent());
        return message;
    }

    @MessageMapping("/test")
    @SendToUser("/user/app/test")
    public Message test(Message message, Principal principal) {
        System.out.println(principal);
        return message;
        //simpMessagingTemplate.convertAndSendToUser(principal.getName() , "/app/test" , message.getContent());

    }

    @MessageMapping("/xiaji")
    @SendTo("/topic/xiaji")
    public com.yinxiang.raspberry.model.Message greeting() throws Exception {
        com.yinxiang.raspberry.model.Message message = new com.yinxiang.raspberry.model.Message();
        message.setContent("下机了!");
        message.setName("超时或者挤下线");
        return message;
    }
}
