package com.yinxiang.raspberry.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Test {
    private HashSet<String> onLineSet = new HashSet<>();

    public Test() {
    }

    public HashSet<String> getOnLineSet() {
        return onLineSet;
    }

    public void setOnLineSet(HashSet<String> onLineSet) {
        this.onLineSet = onLineSet;
    }


}
