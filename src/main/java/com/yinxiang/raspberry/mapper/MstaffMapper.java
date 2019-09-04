package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.yinxiang.raspberry.model.Mstaff;

@Mapper
public interface MstaffMapper {
    Mstaff loadUserByUsername(String LoginName);
}
