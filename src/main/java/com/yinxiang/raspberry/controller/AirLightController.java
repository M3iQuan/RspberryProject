package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.AirLight;
import com.yinxiang.raspberry.bean.Query;
import com.yinxiang.raspberry.bean.Test;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "测试接口")
public class AirLightController {

    @RequestMapping(value = "/device/airlight/test",  method = RequestMethod.GET)
    public String test(@RequestParam(value = "name", required = false, defaultValue = "test") String name){
        return name;
    }

    @RequestMapping(value = "/device/{area}/edit", method = RequestMethod.GET)
    public String area(@PathVariable(value = "area") String area){
        return area;
    }

    @RequestMapping(value = "/device/json", method = RequestMethod.POST)
    public String receiveJson(@RequestBody Test test){
        System.out.println("table name:" + test.getTable());
        System.out.print("queryString: ");
        for(Query query : test.getQueryString()){
            System.out.println(query.getName() + " " + query.getCaption() + " " + query.getBuilder() + " " + query.getLinkOpt() + query.getValue());
        }
        System.out.println("pageSize: " + test.getPageSize());
        System.out.println("pageNumber: " + test.getPageNumber());
        return "200";
    }
}
