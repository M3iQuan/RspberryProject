package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Type;
import org.apache.ibatis.annotations.Param;

public interface TypeMapper {
    Type findDataById(@Param("type_id") Long type_id);
}
