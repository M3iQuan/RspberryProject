package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.TempAndHum;
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
public class TempAndHumService {
    private String table = "temp";
    @Autowired
    InfluxDbUtils influxDbUtils;


    /**1
     * 通过Map构造数据点Point
     * @param data Map中包含所需的数据
     */
    public Point constructPoint(Map<String, Object> data) {
        return Point.measurement("temp")
                .tag("device_id", (String) data.get("device_id"))
                .tag("area_id", (String) data.get("area_id"))
                .addField("temperature",Double.parseDouble((String)data.get("temperature")))
                .addField("humidity",Double.parseDouble((String)data.get("humidity")))
                .addField("fan_state",Long.parseLong((String)data.get("fan_state")))
                .addField("fan_speed",Long.parseLong((String)data.get("fan_speed")))
                .addField("auto_flag",Long.parseLong((String)data.get("auto_flag")))
                .time(Long.parseLong((String)data.get("create_time")), TimeUnit.SECONDS)
                .build();
    }


    /**2
     * 通过对象构造数据点Point
     * @param tempAndHum
     * @return
     */
    public Point constructPoint(TempAndHum tempAndHum) {
        return Point.measurementByPOJO(tempAndHum.getClass())
                .addFieldsFromPOJO(tempAndHum)
                .build();
    }


    /**3
     * 获取单个设备的历史温湿度数据数目
     * @param device_id 设备号
     * @return
     */
    public Long findCountById(String device_id) {
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return new Long(resultMapper.toPOJO(result, TempAndHum.class).size());
    }


    /**
     * 4
     * 单个设备的历史温湿度数据并且可分页，并且也可以搜索
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
        List<TempAndHum> tempAndHums =  resultMapper.toPOJO(queryResult, TempAndHum.class);
        int count = tempAndHums.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", tempAndHums.subList(from, to)); //from 从0开始， to 是不包含的
        return  result;
    }


    /**5
     * 获取所有设备的历史温湿度数据数目, 可以用来统计数据库中有多少数据
     * @return Long
     */
    public Long findAllCount(){
        Long count = 0L;
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT COUNT(temperature) from " + table)
                .forDatabase(influxDbUtils.getDatabase())
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        try { //如果表中没有数据，则没有表，会报异常
            count = (long) Double.parseDouble(result.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString());
        } catch (Exception e){}
        finally {
            return count;
        }
    }


    /**6
     * @param area_ids 所查找的区域ID集合
     * 获取所有设备的最新温湿度数据数目
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
     * 获取用户所在区域的所有设备的最新温湿度数据并且分页，可搜索
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
        List<TempAndHum> tempAndHums =  resultMapper.toPOJO(queryResult, TempAndHum.class);
        int count = tempAndHums.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", tempAndHums.subList(from, to)); //from 从0开始， to 是不包含的
        return result;
    }


    /**8
     * 获取单个设备的最新温湿度数据
     * @param device_id 设备号
     * @return
     */
    public List<TempAndHum> findLatestDataById(String device_id){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc limit 1")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, TempAndHum.class);
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
                }else if ("temperature".equals(query.get("name"))) {  //温度
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (temperature >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (temperature <= " + Double.parseDouble(query.get("value2").toString()) + ")");
                } else if ("humidity".equals(query.get("name"))) {  //湿度
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (humidity >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (humidity <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("fan_state".equals(query.get("name"))) {  //风扇状态
                    sql.append(" and (fan_state=" + Long.parseLong(query.get("value").toString()) + ")");
                } else if ("fan_speed".equals(query.get("name"))) {  //风扇速度
                    if((!"".equals(query.get("value"))))
                        sql.append(" and (fan_speed=" + Long.parseLong(query.get("value").toString()) + ")");
                } else {  //自动控制状态
                    sql.append(" and (auto_flag=" + Long.parseLong(query.get("value").toString()) + ")");
                }
            }
        }
        return sql.toString();
    }

}
