package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.OrderMapper;
import com.yinxiang.raspberry.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public List<Order> getOrder(String status) {
        return orderMapper.getOrder(status);
    }
}
