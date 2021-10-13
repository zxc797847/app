package com.avic.demo.sebeiglzx.bean;

import java.io.Serializable;

/**
 * ��ӷ�Ʒʵ��
 * 
 * @author JamesWu
 * 
 */
public class addJyfpBean implements Serializable{
	private String zrr;
	private String zrrgh;
	private String fpyy;
	private java.math.BigDecimal fpsl;
	private String fpgx;
	private String bqh;
	
	private Boolean isCheck;
	
	
	public String getZrr() {
		return zrr;
	}
	public void setZrr(String zrr) {
		this.zrr = zrr;
	}
	public String getZrrgh() {
		return zrrgh;
	}
	public void setZrrgh(String zrrgh) {
		this.zrrgh = zrrgh;
	}
	public String getFpgx() {
		return fpgx;
	}
	public void setFpgx(String fpgx) {
		this.fpgx = fpgx;
	}
	public java.math.BigDecimal getFpsl() {
		return fpsl;
	}
	public void setFpsl(java.math.BigDecimal fpsl) {
		this.fpsl = fpsl;
	}
	public String getFpyy() {
		return fpyy;
	}
	public void setFpyy(String fpyy) {
		this.fpyy = fpyy;
	}
	
	public Boolean getIsCheck() {
		return isCheck;
	}
	
	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getBqh() {
		return bqh;
	}
	public void setBqh(String bqh) {
		this.bqh = bqh;
	}
	

	
}
