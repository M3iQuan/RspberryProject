package com.yinxiang.raspberry.bean;

public class TotalDevices {

    private Long totalNum;

    private Long offLineNum;

    private Long errNum;

    private Long totalData;

    public TotalDevices() {
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getOffLineNum() {
        return offLineNum;
    }

    public void setOffLineNum(Long offLineNum) {
        this.offLineNum = offLineNum;
    }

    public Long getErrNum() {
        return errNum;
    }

    public void setErrNum(Long errNum) {
        this.errNum = errNum;
    }

    public Long getTotalData() {
        return totalData;
    }

    public void setTotalData(Long totalData) {
        this.totalData = totalData;
    }
}
