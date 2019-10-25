package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> getOrder(@Param("status") String status);

}
