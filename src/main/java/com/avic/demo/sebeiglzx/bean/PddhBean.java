package com.avic.demo.sebeiglzx.bean;
/**
 * 盘点单号实体
 */
 public class PddhBean {
    private String pddh;
    private Integer count;

    public PddhBean(String pddh, Integer count) {
        super();
        this.pddh = pddh;
        this.count = count;
    }

    public String getPddh() {
        return pddh;
    }

    public void setPddh(String pddh) {
        this.pddh = pddh;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return this.pddh;
    }

}
