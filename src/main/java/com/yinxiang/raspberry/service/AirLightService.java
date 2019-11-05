package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.AirLight;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class AirLightService {
    @Autowired
    InfluxDbUtils influxDbUtils;
    private String table = "air_light";


    /**1
     * 通过Map构造数据点Point
     * @param data Map中包含所需的数据
     * @return
     */
    public Point constructPoint(Map<String, Object> data) {
        return Point.measurement(table)//指定表
                .tag("device_id", data.get("device_id").toString()) //设备号增加索引
                .tag("area_id", data.get("area_id").toString()) //区域号增加索引
                .addField("luminance", Long.parseLong(data.get("luminance").toString())) //光照强度
                .addField("pm2_5", Long.parseLong(data.get("pm2_5").toString())) //pm2.5
                .addField("pm10", Long.parseLong(data.get("pm10").toString()))  //pm10
                .time(Long.parseLong((String) data.get("create_time")), TimeUnit.SECONDS) //时间
                .build();
    }


    /**
     * 2
     * 通过对象构造数据点Point
     * @param airLight
     * @return
     */
    public Point constructPoint(AirLight airLight) {
        return Point.measurementByPOJO(airLight.getClass())
                .addFieldsFromPOJO(airLight)
                .build();
    }


    /**3
     * 获取单个设备的历史空气光照数据数目
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
        return new Long(resultMapper.toPOJO(result, AirLight.class).size());
    }


    /**
     * 4
     * 单个设备的历史空气光照数据并且可分页，并且也可以搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    public Map<String, Object> findDataByIdAndPage(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        int currentPage = Integer.parseInt(data.get("currentPage").toString());
        int pageSize = Integer.parseInt(data.get("pageSize").toString());
        sql.append("(device_id = '" + data.get("device_id").toString() + "')");
        String queryString = addCondition(data);
        sql.append(queryString);
        Query query = new Query("SELECT * from " + table + " where " + sql + " order by time desc");
        QueryResult queryResult = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AirLight> tempAndHums =  resultMapper.toPOJO(queryResult, AirLight.class);
        int count = tempAndHums.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", tempAndHums.subList(from, to)); //from 从0开始， to 是不包含的
        return  result;
    }

    /**5
     * 获取所有设备的空气光照数据数目, 可以用来统计数据库中有多少数据
     * @return Long
     */
    public Long findAllCount(){
        Long count = 0L;
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT COUNT(luminance) from " + table)
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
     * 获取所有设备的最新空气光照数据数目
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
     * 获取用户所在区域的所有设备的最新空气光照数据并且分页，可搜索
     * @param data Map<String, Object> = {"device_id":"", "currentPage":"", "pageSize":"", "queryString":""/[]}
     * @param area_ids List<String> 用户所在的区域ID集合
     * @return
     */
    public Map<String, Object> findAllLatestDataByPage(Map<String, Object> data , List<String> area_ids){
        Map<String, Object> result = new HashMap<>();
        int currentPage = Integer.parseInt(data.get("currentPage").toString());
        int pageSize = Integer.parseInt(data.get("pageSize").toString());
        StringBuilder sql = new StringBuilder();
        sql.append("(area_id='" + area_ids.get(0) + "'");
        for (int i = 1; i < area_ids.size(); i++) {
            sql.append(" or area_id='" + area_ids.get(i) + "'");
        }
        sql.append(") and (device_id =~ /" + data.get("device_id").toString() + "/)"); //实现设备号的模糊搜索
        String queryString = addCondition(data);
        sql.append(queryString);
        Query query = new Query("SELECT * from " + table + " where " + sql.toString() + " group by device_id order by time desc limit 1");
        QueryResult queryResult = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AirLight> airLights = resultMapper.toPOJO(queryResult, AirLight.class);
        int count = airLights.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", airLights.subList(from, to)); //from 从0开始， to 是不包含的
        return result;
    }


    /**8
     * 获取单个设备的最新空气光照数据
     * @param device_id 设备号
     * @return
     */
    public List<AirLight> findLatestDataById(String device_id){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc limit 1")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, AirLight.class);
    }


    /**
     * 对查询条件进行拼接
     * @param data Map<String, Object> = {"queryString":[], "device_id":"",...} queryString = [{"name":"","value":""}, {"name":"", "value1":"", "value2":""}]
     * @return
     */
    public String addCondition(Map<String,Object> data){
        StringBuilder sql = new StringBuilder();
        if (data.get("queryString") instanceof List) {
            List<Map<String, Object>> queryString = (ArrayList)data.get("queryString");
            Iterator<Map<String,Object>> iterator = queryString.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> query = iterator.next();
                if("time".equals(query.get("name"))){  //时间
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (time >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (time <= " + Double.parseDouble(query.get("value2").toString()) + ")");
                }else if ("luminance".equals(query.get("name"))) {  //光照强度
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (luminance >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (luminance <= " + Double.parseDouble(query.get("value2").toString()) + ")");
                } else if ("pm2_5".equals(query.get("name"))) {  //pm2.5
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (pm2_5 >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (pm2_5 <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("pm10".equals(query.get("name"))) {  //pm10
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (pm10 >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (pm10 <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                }
            }
        }
        return sql.toString();
    }

}
