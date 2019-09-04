package com.yinxiang.raspberry.bean;

public class AutoReclosingPowerProtector {
    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间 YY:MM:dd HH:mm:ss
     */
    private String create_time;
    /**
     * 分/合闸状态
     */
    private String Sta_H;
    /**
     * 分闸故障原因
     */
    private Long Sta_L;
    /**
     * 实时电压 V
     */
    private Long U;
    /**
     * 实时电流 0.1A
     */
    private Long I;
    /**
     * 实时漏电流 mA
     */
    private Long Iz;
    /**
     * 额定电流 0.1A
     */
    private Long Ie;
    /**
     * 过压动作值 V
     */
    private Long Uov;
    /**
     * 欠压动作值 V
     */
    private Long Ulv;
    /**
     * 漏电动作值 mA
     */
    private Long Izset;
    /**
     * 过流出现次数
     */
    private Long CurErrCnt;
    /**
     * 漏电出现次数
     */
    private Long LKErrCnt;
    /**
     * 欠压出现次数
     */
    private Long VlErrCnt;
    /**
     * 过压出现次数
     */
    private Long VhErrCnt;
    /**
     * 断电出现次数
     */
    private Long VOffCnt;
    /**
     * 设备地址
     */
    private Long Addr;
    /**
     * 打开/关闭设备
     */
    private Long VON_OFF;

    public AutoReclosingPowerProtector() {
    }


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSta_H() {
        return Sta_H;
    }

    public void setSta_H(String sta_H) {
        Sta_H = sta_H;
    }

    public Long getSta_L() {
        return Sta_L;
    }

    public void setSta_L(Long sta_L) {
        Sta_L = sta_L;
    }

    public Long getU() {
        return U;
    }

    public void setU(Long u) {
        U = u;
    }

    public Long getI() {
        return I;
    }

    public void setI(Long i) {
        I = i;
    }

    public Long getIz() {
        return Iz;
    }

    public void setIz(Long iz) {
        Iz = iz;
    }

    public Long getIe() {
        return Ie;
    }

    public void setIe(Long ie) {
        Ie = ie;
    }

    public Long getUov() {
        return Uov;
    }

    public void setUov(Long uov) {
        Uov = uov;
    }

    public Long getUlv() {
        return Ulv;
    }

    public void setUlv(Long ulv) {
        Ulv = ulv;
    }

    public Long getIzset() {
        return Izset;
    }

    public void setIzset(Long izset) {
        Izset = izset;
    }

    public Long getCurErrCnt() {
        return CurErrCnt;
    }

    public void setCurErrCnt(Long curErrCnt) {
        CurErrCnt = curErrCnt;
    }

    public Long getLKErrCnt() {
        return LKErrCnt;
    }

    public void setLKErrCnt(Long LKErrCnt) {
        this.LKErrCnt = LKErrCnt;
    }

    public Long getVlErrCnt() {
        return VlErrCnt;
    }

    public void setVlErrCnt(Long vlErrCnt) {
        VlErrCnt = vlErrCnt;
    }

    public Long getVhErrCnt() {
        return VhErrCnt;
    }

    public void setVhErrCnt(Long vhErrCnt) {
        VhErrCnt = vhErrCnt;
    }

    public Long getVOffCnt() {
        return VOffCnt;
    }

    public void setVOffCnt(Long VOffCnt) {
        this.VOffCnt = VOffCnt;
    }

    public Long getAddr() {
        return Addr;
    }

    public void setAddr(Long addr) {
        Addr = addr;
    }

    public Long getVON_OFF() {
        return VON_OFF;
    }

    public void setVON_OFF(Long VON_OFF) {
        this.VON_OFF = VON_OFF;
    }
}
