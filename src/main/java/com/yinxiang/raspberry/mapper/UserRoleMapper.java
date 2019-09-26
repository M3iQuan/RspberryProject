package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {
    void addRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    void deleteRoleByUid(@Param("uid") Integer uid);


    //可能需要一个获取全部角色的sql，用来在添加角色时，可以供选择。
    int getRidByName(String nameZh);

    String[] getAllRole();

    int deleteUserAreaByUid(@Param("uid") Integer uid);

    //int addUserAreaByAreaname(@Param("uid") Integer uid,@Param("areaname") String areaname);
}
