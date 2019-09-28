package com.yinxiang.raspberry.bean;

import java.util.List;

public class Query {
    String name;

    String caption;

    String linkOpt;

    String builder;

    List<String> value;

    public Query() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLinkOpt() {
        return linkOpt;
    }

    public void setLinkOpt(String linkOpt) {
        this.linkOpt = linkOpt;
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
