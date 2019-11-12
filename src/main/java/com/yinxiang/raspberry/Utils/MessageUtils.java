package com.yinxiang.raspberry.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类是静态工具类，用于处理消息的
 */
public class MessageUtils {

    /**
     * 将json的字符串转成Map<String,Object>
     * @param content
     * @return
     */
    public static Map<String,Object> jsonString2Map(String content){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = null;
        try{
            data = mapper.readValue(content,Map.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return  data;
    }

    /**
     * 将map转成Json字符串
     * @param data Map<String,Object>
     * @return
     */
    public static String Map2jsonString(Map<String, Object> data) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try{
            result =  mapper.writeValueAsString(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 将时间格式yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static String date2Stamp(String time) {
        String result = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            result = String.valueOf(date.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
