package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    void addRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    void deleteRoleByUid(@Param("uid") Integer uid);


    int getRidByName(String nameZh);

    String[] getAllRole();

    int deleteUserAreaByUid(@Param("uid") Integer uid, @Param("areanames")List<String> areanames);

    //int addUserAreaByAreaname(@Param("uid") Integer uid,@Param("areaname") String areaname);

}
