package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.yinxiang.raspberry.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface RoleMapper {
        List<Role> getAllRole();

        void addUserArea(@Param("user_id") int user_id, @Param("area_id")int area_id);

        int getRidByUserId(@Param("user_id")int user_id);
}
