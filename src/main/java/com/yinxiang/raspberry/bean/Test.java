package com.yinxiang.raspberry.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Test {
    private HashSet<String> ns_OnLineSet = new HashSet<>();
    private HashSet<String> ft_OnLineSet = new HashSet<>();
    private HashSet<String> ba_OnLineSet = new HashSet<>();
    private HashSet<String> lh_OnLineSet = new HashSet<>();

    public Test() {
    }

    public HashSet<String> getNs_OnLineSet() {
        return ns_OnLineSet;
    }

    public void setNs_OnLineSet(HashSet<String> ns_OnLineSet) {
        this.ns_OnLineSet = ns_OnLineSet;
    }

    public HashSet<String> getFt_OnLineSet() {
        return ft_OnLineSet;
    }

    public void setFt_OnLineSet(HashSet<String> ft_OnLineSet) {
        this.ft_OnLineSet = ft_OnLineSet;
    }

    public HashSet<String> getBa_OnLineSet() {
        return ba_OnLineSet;
    }

    public void setBa_OnLineSet(HashSet<String> ba_OnLineSet) {
        this.ba_OnLineSet = ba_OnLineSet;
    }

    public HashSet<String> getLh_OnLineSet() {
        return lh_OnLineSet;
    }

    public void setLh_OnLineSet(HashSet<String> lh_OnLineSet) {
        this.lh_OnLineSet = lh_OnLineSet;
    }
}
