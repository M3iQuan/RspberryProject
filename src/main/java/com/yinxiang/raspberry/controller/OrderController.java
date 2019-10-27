package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.model.Order;
import com.yinxiang.raspberry.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/getOrder",method = RequestMethod.POST)
    public List<Order> getOrder(String status) {
        return orderService.getOrder(status);
    }

    @RequestMapping(value = "/getOrderByDid",method = RequestMethod.POST)
    public Map<String,Object> getOrderByDid(String did) {
        return orderService.getOrderByDid(did);
    }
}
