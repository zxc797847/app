package com.avic.demo.sebeiglzx.bean;

public class SbjldwBean {
    private String key;
    private String name;
    private String value;

    public SbjldwBean(String key,String name,String value){
        super();
        this.key=key;
        this.name=name;
        this.value=value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
