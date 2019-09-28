package com.yinxiang.raspberry.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.yinxiang.raspberry.model.Device;

import java.util.List;

@Mapper
public interface DeviceMapper {

    List<Device> getDeviceByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("keywords") String keywords,@Param("id") String id,@Param("latitude") Double latitude,@Param("longitude") Double longitude,@Param("description") String description,@Param("statusname") String statusname,@Param("type") String type,@Param("areaname")String areaname,@Param("areanames") List<String> areanames);

    Long getCountByKeywords(@Param("keywords") String keywords,@Param("id") String id,@Param("latitude") Double latitude,@Param("longitude") Double longitude,@Param("description") String description,@Param("statusname") String statusname,@Param("type") String type,@Param("areaname")String areaname,@Param("areanames") List<String> areanames);

    int updateDevice(@Param("device") Device device);
    int updateDevice_status(@Param("device") Device device);

    List<Device> getWrongDeviceById(String device_id);
    List<String> getAllType();
    List<String> getAllArea();

    int addDevice(@Param("device") Device device);
    int addDevice_status(@Param("device_id") String device_id,@Param("statusname") String statusname);
}
