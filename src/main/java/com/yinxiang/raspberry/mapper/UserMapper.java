package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.*;
import com.yinxiang.raspberry.model.Role;
import com.yinxiang.raspberry.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);

    List<Role> getUserRolesByUid(Integer id);

    int register(User user);

    int deleteUserByName(@Param("username") String username);

    @Select("select u.id from user u where u.username = #{username} and password = #{password}")
    Long login(User user);

    @Update("update user set password=#{newPassword} where username=#{username}")
    void ChangePassword(@Param("username")String username,@Param("newPassword")String newPassword);

    List<User> getUserByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords, @Param("username") String username,@Param("description") String description,@Param("rolename") String rolename);

    Long getCountByKeywords(@Param("keywords") String keywords, @Param("username") String username, @Param("description") String description,@Param("rolename") String rolename);

    int updateUser(@Param("user") User user);

}
