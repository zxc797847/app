package com.avic.demo.sebeiglzx.bean;
/**
 * 连接蓝牙设备
 */

public class ConnectBlueBean {

    public String name;
    public String address;
    public String type;
    public String rssivalue;
    public boolean isCheck;
    public boolean isConnect;

    public ConnectBlueBean() {
        super();
    }

    public ConnectBlueBean(String name, String address, String type, String rssivalue, boolean isCheck, boolean isConnect) {
        super();
        this.name = name;
        this.address = address;
        this.type = type;
        this.rssivalue = rssivalue;
        this.isCheck = isCheck;
        this.isConnect = isConnect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRssivalue() {
        return rssivalue;
    }

    public void setRssivalue(String rssivalue) {
        this.rssivalue = rssivalue;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }
}
