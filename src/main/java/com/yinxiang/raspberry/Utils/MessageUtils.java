package com.yinxiang.raspberry.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
}
