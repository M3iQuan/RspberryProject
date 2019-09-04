package com.yinxiang.raspberry.bean;

public class Sensor {

    private String name;

    private int value;

    private String sensor_table;

    private String sensor_column;

    private String unit;

    public Sensor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSensor_table() {
        return sensor_table;
    }

    public void setSensor_table(String sensor_table) {
        this.sensor_table = sensor_table;
    }

    public String getSensor_column() {
        return sensor_column;
    }

    public void setSensor_column(String sensor_column) {
        this.sensor_column = sensor_column;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
