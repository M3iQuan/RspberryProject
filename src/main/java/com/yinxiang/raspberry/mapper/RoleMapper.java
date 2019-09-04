package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.yinxiang.raspberry.model.Role;

import java.util.List;

@Mapper
public interface RoleMapper {
        List<Role> getAllRole();


}
