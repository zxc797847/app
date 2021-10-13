package com.avic.demo.sebeiglzx.bean;

public class PddBean {
    private String pddh;

    public PddBean(String pddh) {
        super();
        this.pddh = pddh;

    }
    public String getPddh() {
        return pddh;
    }

    public void setPddh(String pddh) {
        this.pddh = pddh;
    }
    @Override
    public String toString() {
        return this.pddh;
    }

}
