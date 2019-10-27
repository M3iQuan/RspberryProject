package com.yinxiang.raspberry.bean;

import java.util.ArrayList;
import java.util.List;

public class Area {
    /**
     * 区域名
     */
    private String area_name;
    private int id;
    private int parentId;
    private String areapath;
    private boolean isparent;
    private Integer result;
    private List<Area> children = new ArrayList<>();

    public Area() {
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getAreapath() {
        return areapath;
    }

    public void setAreapath(String areapath) {
        this.areapath = areapath;
    }

    public boolean isIsparent() {
        return isparent;
    }

    public void setIsparent(boolean isparent) {
        this.isparent = isparent;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Area> getChildren() {
        return children;
    }

    public void setChildren(List<Area> children) {
        this.children = children;
    }


}
