package com.avic.demo.sebeiglzx.bean;

public class SbztBean {
    private String key;
    private String value;
    private String name;

    public  SbztBean(String key,String value,String name){
        super();

        this.key=key;
        this.value=value;
        this.name=name;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
