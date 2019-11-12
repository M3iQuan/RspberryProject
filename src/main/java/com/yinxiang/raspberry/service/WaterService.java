package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.Water;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class WaterService {
    @Autowired
    InfluxDbUtils influxDbUtils;
    private String table = "water_status";


    /**1
     * 通过Map构造数据点Point
     * @param data Map中包含所需的数据
     * @return
     */
    public Point constructPoint(Map<String, Object> data) {
        return Point.measurement(table)//指定表
                .tag("device_id", (String) data.get("device_id")) //设备号增加索引
                .tag("area_id", (String) data.get("area_id")) //区域号增加索引
                .addField("status", (String) data.get("status")) //水浸状态
                .time(Long.parseLong((String) data.get("create_time")), TimeUnit.SECONDS) //时间
                .build();
    }


    /**2
     * 通过对象构造数据点Point
     * @param water
     * @return
     */
    public Point constructPoint(Water water) {
        return Point.measurementByPOJO(water.getClass())
                .addFieldsFromPOJO(water)
                .build();
    }


    /**3
     * 获取单个设备的历史水浸数据数目
     * @param device_id
     * @return Long
     */
    public Long findCountById(String device_id){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc ")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return new Long(resultMapper.toPOJO(result, Water.class).size());
    }


    /**4
     * 获取单个设备的历史水浸数据并且可分页
     * @param device_id 设备号
     * @param pageSize 页面大小
     * @param currentPage 当前页面
     * @return
     */
    public List<Water> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc limit " + pageSize + " offset " + (currentPage-1)*pageSize)
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, Water.class);
    }


    /**5
     * 获取所有设备的历史水浸数据数目, 可以用来统计数据库中有多少数据
     * @return Long
     */
    public Long findAllCount(){
        Long count = 0L;
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT COUNT(status) from " + table)
                .forDatabase(influxDbUtils.getDatabase())
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        try{
            count = (long)Double.parseDouble(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString());
        }catch (Exception e){}
        finally {
            return count;
        }
    }


    /**6
     * @param area_ids 查找的所有区域
     * 获取所有设备的最新水浸数据数目
     * @return
     */
    public Long findAllCountLatest(List<String> area_ids){
        StringBuilder sql = new StringBuilder();
        sql.append("area_id='" + area_ids.get(0) + "'");
        for (int i = 1; i < area_ids.size(); i++) {
            sql.append(" or area_id='" + area_ids.get(i) + "'");
        }
        Query query = new Query("SELECT * from " + table + " where " + sql.toString() + " group by device_id order by time desc limit 1");
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        return new Long(result.getResults().get(0).getSeries().size());
    }


    /**7
     * 获取所有设备的最新水浸数据并且分页
     * @param area_ids 查询所在区域的地方
     * @param pageSize 页面大小
     * @param currentPage 页面号
     * @return
     */
    public List<Water> findAllLatestDataByPage(List<String> area_ids, Integer pageSize, Integer currentPage){
        StringBuilder sql = new StringBuilder();
        sql.append("area_id='" + area_ids.get(0) + "'");
        for (int i = 1; i < area_ids.size(); i++) {
            sql.append(" or area_id='" + area_ids.get(i) + "'");
        }
        Query query = new Query("SELECT * from " + table + " where " + sql.toString() + " group by device_id order by time desc limit 1");
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, Water.class).subList((currentPage - 1) * pageSize, currentPage * pageSize); //from 从0开始， to 是不包含的
    }


    /**8
     * 获取单个设备的最新水浸数据
     * @param device_id 设备号
     * @return
     */
    public List<Water> findLatestDataById(String device_id){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc limit 1")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, Water.class);
    }

}
