package com.yinxiang.raspberry.Utils;


import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import java.util.concurrent.TimeUnit;

public class InfluxDbUtils {
    /**
     * url
     */
    private String url;
    /**
     * user
     */
    private String username;
    /**
     * password
     */
    private String password;
    /**
     * database
     */
    private String database;
    /**
     * 保存策略
     */
    private String retentionPolicy;
    /**
     * InfluxDB实例
     */
    private InfluxDB influxDB;
    /**
     *
     */
    public static String policyNamePix = "intellControlPolicy_";

    public InfluxDbUtils(String username, String password, String url, String database, String rententionPolicy){
        this.username = username;
        this.password = password;
        this.url = url;
        this.database = database;
        this.retentionPolicy = rententionPolicy == null || "".equals(rententionPolicy) ? "autogen" : rententionPolicy;
        this.influxDB = influxDB();
    }

    private InfluxDB influxDB(){
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(url, username, password);
            influxDB.setDatabase(database);
            influxDB.setRetentionPolicy(retentionPolicy);
            //第一个参数是point的个数，第二个参数是时间(毫秒),如果满足2000个point或者5000毫秒，则发送一
            // 次插入请求
            influxDB.enableBatch(2000, 5000, TimeUnit.MILLISECONDS);
            //influxDB.enableBatch(BatchOptions.DEFAULTS);
        }
        return influxDB;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }
}
