package com.yinxiang.raspberry.bean;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "protector", database = "intellControl")
public class AutoReclosingPowerProtector {

    /**
     * 设备号
     */
    @Column(name = "device_id", tag = true)
    private String device_id;

    /**
     * 区域号
     */
    @Column(name = "area_id", tag = true)
    private String area_id;

    /**
     * 时间戳  YY:MM:dd HH:mm:ss
     */
    @Column(name = "time")
    private Instant time;

    /**
     * 分/合闸状态
     */
    @Column(name = "Sta_H")
    private String Sta_H;

    /**
     * 分闸故障原因
     */
    @Column(name = "Sta_L")
    private Long Sta_L;

    /**
     * 实时电压 V
     */
    @Column(name = "U")
    private Long U;

    /**
     * 实时电流 0.1A
     */
    @Column(name = "I")
    private Long I;

    /**
     * 实时漏电流 mA
     */
    @Column(name = "Iz")
    private Long Iz;

    /**
     * 额定电流 0.1A
     */
    @Column(name = "Ie")
    private Long Ie;

    /**
     * 过压动作值 V
     */
    @Column(name = "Uov")
    private Long Uov;

    /**
     * 欠压动作值 V
     */
    @Column(name = "Ulv")
    private Long Ulv;

    /**
     * 漏电动作值 mA
     */
    @Column(name = "Izset")
    private Long Izset;

    /**
     * 过流出现次数
     */
    @Column(name = "CurErrCnt")
    private Long CurErrCnt;

    /**
     * 漏电出现次数
     */
    @Column(name = "LKErrCnt")
    private Long LKErrCnt;

    /**
     * 欠压出现次数
     */
    @Column(name = "VlErrCnt")
    private Long VlErrCnt;

    /**
     * 过压出现次数
     */
    @Column(name = "VhErrCnt")
    private Long VhErrCnt;

    /**
     * 断电出现次数
     */
    @Column(name = "VOffCnt")
    private Long VOffCnt;

    /**
     * 设备地址
     */
    @Column(name = "Addr")
    private Long Addr;

    /**
     * 打开/关闭设备
     */
    @Column(name = "VON_OFF")
    private Long VON_OFF;

    public AutoReclosingPowerProtector() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "AutoReclosingPowerProtector{" +
                "device_id='" + device_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", time=" + time +
                ", Sta_H='" + Sta_H + '\'' +
                ", Sta_L=" + Sta_L +
                ", U=" + U +
                ", I=" + I +
                ", Iz=" + Iz +
                ", Ie=" + Ie +
                ", Uov=" + Uov +
                ", Ulv=" + Ulv +
                ", Izset=" + Izset +
                ", CurErrCnt=" + CurErrCnt +
                ", LKErrCnt=" + LKErrCnt +
                ", VlErrCnt=" + VlErrCnt +
                ", VhErrCnt=" + VhErrCnt +
                ", VOffCnt=" + VOffCnt +
                ", Addr=" + Addr +
                ", VON_OFF=" + VON_OFF +
                '}';
    }
}
