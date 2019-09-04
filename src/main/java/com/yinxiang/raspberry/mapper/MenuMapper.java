package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.yinxiang.raspberry.model.Menu;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> getAllMenus();

    List<Menu> getMenusByHrId();//List<Menu> getMenusByHrId(Long hrId);

    int addMenuUser(@Param("mid")int mid, @Param("uid")int uid);

    int getMidByName(String menuname);

    int deleteMenuUserByUid(int uid);
}
