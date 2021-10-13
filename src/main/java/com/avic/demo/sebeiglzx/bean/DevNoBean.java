package com.avic.demo.sebeiglzx.bean;

/**
 * 货架号实体
 */
 public class DevNoBean {
    private String id;
    private String hjh;
    private String hjhmc;

    public DevNoBean(String id, String hjh, String hjhmc) {
        super();
        this.id = id;
        this.hjh = hjh;
        this.hjhmc = hjhmc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHjh() {
        return hjh;
    }

    public void setHjh(String hjh) {
        this.hjh = hjh;
    }

    public String getHjhmc() {
        return hjhmc;
    }

    public void setHjhmc(String hjhmc) {
        this.hjhmc = hjhmc;
    }

    @Override
    public String toString() {
        return this.hjh;
    }
}
