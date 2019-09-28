package com.yinxiang.raspberry.bean;

import java.util.List;

public class Test {
    String table;

    List<Query> queryString;

    Integer pageSize;

    Integer pageNumber;

    public Test() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Query> getQueryString() {
        return queryString;
    }

    public void setQueryString(List<Query> queryString) {
        this.queryString = queryString;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
