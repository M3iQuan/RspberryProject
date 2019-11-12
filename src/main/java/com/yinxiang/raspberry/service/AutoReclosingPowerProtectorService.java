package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
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
public class AutoReclosingPowerProtectorService {
    @Autowired
    InfluxDbUtils influxDbUtils;
    private String table = "protector";


    /**1
     * 通过Map构造数据点Point
     * @param data Map中包含所需的数据
     * @return
     */
    public Point constructPoint(Map<String, Object> data) {
        return Point.measurement(table)//指定表
                .tag("device_id", data.get("device_id").toString()) //设备号增加索引
                .tag("area_id", data.get("area_id").toString()) //区域号增加索引
                .addField("Sta_H", data.get("Sta_H").toString())
                .addField("Sta_L", Long.parseLong(data.get("Sta_L").toString()))
                .addField("U", Long.parseLong(data.get("U").toString()))
                .addField("I", Long.parseLong(data.get("I").toString()))
                .addField("Iz", Long.parseLong(data.get("Iz").toString()))
                .addField("Ie", Long.parseLong(data.get("Ie").toString()))
                .addField("Uov", Long.parseLong(data.get("Uov").toString()))
                .addField("Ulv", Long.parseLong(data.get("Ulv").toString()))
                .addField("Izset", Long.parseLong(data.get("Izset").toString()))
                .addField("CurErrCnt", Long.parseLong(data.get("CurErrCnt").toString()))
                .addField("LKErrCnt", Long.parseLong(data.get("LKErrCnt").toString()))
                .addField("VlErrCnt", Long.parseLong(data.get("VlErrCnt").toString()))
                .addField("VhErrCnt", Long.parseLong(data.get("VhErrCnt").toString()))
                .addField("VOffCnt", Long.parseLong(data.get("VOffCnt").toString()))
                .addField("Addr", Long.parseLong(data.get("Addr").toString()))
                .addField("VON_OFF", Long.parseLong(data.get("VON_OFF").toString()))
                .time(Long.parseLong((String) data.get("create_time")), TimeUnit.SECONDS) //时间
                .build();
    }


    /**2
     * 通过对象构造数据点Point
     * @param autoReclosingPowerProtector
     * @return
     */
    public Point constructPoint(AutoReclosingPowerProtector autoReclosingPowerProtector) {
        return Point.measurementByPOJO(autoReclosingPowerProtector.getClass())
                .addFieldsFromPOJO(autoReclosingPowerProtector)
                .build();
    }


    /**3
     * 获取单个设备的历史重合闸数据数目
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
        return new Long(resultMapper.toPOJO(result, AutoReclosingPowerProtector.class).size());
    }


    /**
     * 4
     * 单个设备的历史重合闸数据并且可分页，并且也可以搜索
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
        List<AutoReclosingPowerProtector> tempAndHums =  resultMapper.toPOJO(queryResult, AutoReclosingPowerProtector.class);
        int count = tempAndHums.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", tempAndHums.subList(from, to)); //from 从0开始， to 是不包含的
        return  result;
    }


    /**5
     * 获取所有设备的重合闸水浸数据数目, 可以用来统计数据库中有多少数据
     * @return Long
     */
    public Long findAllCount(){
        Long count = 0L;
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT COUNT(Sta_H) from " + table)
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
     * 获取所有设备的最新重合闸数据数目
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
     * 获取用户所在区域的所有设备的最新重合闸数据并且分页，可搜索
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
        List<AutoReclosingPowerProtector> airLights = resultMapper.toPOJO(queryResult, AutoReclosingPowerProtector.class);
        int count = airLights.size();
        result.put("count", count);
        int from = (currentPage - 1) * pageSize;
        int to = (currentPage * pageSize) < count ? (currentPage * pageSize) : count;
        result.put("result", airLights.subList(from, to)); //from 从0开始， to 是不包含的
        return result;
    }

    /**8
     * 获取单个设备的最新水浸数据
     * @param device_id 设备号
     * @return
     */
    public List<AutoReclosingPowerProtector> findLatestDataById(String device_id){
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * from " + table + " where device_id = $did order by time desc limit 1")
                .forDatabase(influxDbUtils.getDatabase())
                .bind("did", device_id)
                .create();
        QueryResult result = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(result, AutoReclosingPowerProtector.class);
    }


    /**
     * 对查询条件进行拼接
     * @param data Map<String, Object> = {"queryString":[], "device_id":"",...} queryString = [{"name":"","value":""}, {"name":"", "value1":"", "value2":""}]
     * @return
     */
    public String addCondition(Map<String, Object> data) {
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
                } else if ("U".equals(query.get("name")))
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (U >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (U <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("I".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (I >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (I <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("Iz".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (Iz >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (Iz <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("Ie".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1"))))
                        sql.append(" and (Ie >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    if(!("".equals(query.get("value2"))))
                        sql.append(" and (Ie <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                } else if ("Uov".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (Uov >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (Uov <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("Ulv".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (Ulv >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (Ulv <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("Izset".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (Izset >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (Izset <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("CurErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (CurErrCnt >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (CurErrCnt <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("LKErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (LKErrCnt >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (LKErrCnt <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("VlErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (VlErrCnt >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (VlErrCnt <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("VhErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (VhErrCnt >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (VhErrCnt <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("VOffCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (VOffCnt >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (VOffCnt <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("Addr".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (Addr >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (Addr <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("VON_OFF".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        sql.append(" and (VON_OFF >= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                    if(!("".equals(query.get("value2")))){
                        sql.append(" and (VON_OFF <= " + Double.parseDouble(query.get("value1").toString()) + ")");
                    }
                } else if ("Sta_H".equals(query.get("name"))){
                    if((!"".equals(query.get("value"))))
                        sql.append(" and (Sta_H=" + Long.parseLong(query.get("value").toString()) + ")");
                }
            }
        }
        return sql.toString();
    }

}
