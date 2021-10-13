package com.avic.demo.sebeiglzx.bean;

import java.util.Map;

public class PddMxBean {
    /** 主键 */
    private java.lang.Integer id;
    /** 外键 */
    private java.lang.String fid;
    /** 创建人名称 */
    private java.lang.String createName;
    /** 创建人登录名称 */
    private java.lang.String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private java.lang.String updateName;
    /** 更新人登录名称 */
    private java.lang.String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 所属部门 */
    private java.lang.String sysOrgCode;
    /** 所属公司 */
    private java.lang.String sysCompanyCode;
    /** 录入人工号 */
    private java.lang.String llrFsNo;
    /** 是否缺失 */
    private java.lang.String sfqs;
    /** 标签号 */
    private java.lang.String bqh;
    /** RFID_tid号 */
    private java.lang.String rfid_tid;
    /** 设备名称 */
    private java.lang.String cpmc;
    /** 设备编码 */
    private java.lang.String cpbm;
    /** 生产厂家 */
    private java.lang.String sccj;
    /** 使用部门*/
    private java.lang.String sybm;
    /** 设备单价*/
    private java.math.BigDecimal sbdj;
    /** 账面数量 */
    private java.lang.Integer sl;
    /** 采购日期 */
    private java.lang.String cgsj;
    /** 所在位置*/
    private java.lang.String szwz;
    /** 是否已盘 */
    private java.lang.String sfyp;
    /** 设备编号 */
    private java.lang.String sbbh;
    /**设备规格*/
    private java.lang.String cpgg;
    /**设备型号*/
    private java.lang.String cpxh;
    /**设备图片*/
    private java.lang.String sbtp;
    /**设备折损*/
    private java.math.BigDecimal sbzs;
    /**经办人*/
    private java.lang.String jbr;
    /**设备负责人*/
    private java.lang.String sbfzr;
    /**备注*/
    private java.lang.String bz;



    /** RFID_tid号 */
    private java.lang.String rfid_epc;
    private java.lang.Boolean isCheck;




    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getFid() {
        return fid;
    }

    public void setFid(java.lang.String fid) {
        this.fid = fid;
    }

    public java.lang.String getCreateName() {
        return createName;
    }

    public void setCreateName(java.lang.String createName) {
        this.createName = createName;
    }

    public java.lang.String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(java.lang.String createBy) {
        this.createBy = createBy;
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    public java.lang.String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(java.lang.String updateName) {
        this.updateName = updateName;
    }

    public java.lang.String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(java.lang.String updateBy) {
        this.updateBy = updateBy;
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    public java.lang.String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(java.lang.String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public java.lang.String getSysCompanyCode() {
        return sysCompanyCode;
    }

    public void setSysCompanyCode(java.lang.String sysCompanyCode) {
        this.sysCompanyCode = sysCompanyCode;
    }

    public java.lang.String getLlrFsNo() {
        return llrFsNo;
    }

    public void setLlrFsNo(java.lang.String llrFsNo) {
        this.llrFsNo = llrFsNo;
    }

    public java.lang.String getSfqs() {
        return sfqs;
    }

    public void setSfqs(java.lang.String sfqs) {
        this.sfqs = sfqs;
    }


    public java.lang.String getBqh() {
        return bqh;
    }

    public void setBqh(String bqh) {
        this.bqh = bqh;
    }
    public java.lang.String getRfid_tid() {
        return rfid_tid;
    }

    public void setRfid_tid(java.lang.String rfid_tid) {
        this.rfid_tid = rfid_tid;
    }
    public String getRfid_epc() {
        return rfid_epc;
    }

    public void setRfid_epc(String rfid_epc) {
        this.rfid_epc = rfid_epc;
    }
    public java.lang.String getCpmc() {
        return cpmc;
    }

    public void setCpmc(java.lang.String cpmc) {
        this.cpmc = cpmc;
    }
    public java.lang.String getCpbm() {
        return cpbm;
    }

    public void setCpbm(java.lang.String cpbm) {
        this.cpbm = cpbm;
    }

    public java.lang.String getSccj() {
        return sccj;
    }
    public void setSccj(java.lang.String sccj) {
        this.sccj = sccj;
    }

    public java.lang.String getSybm() {
        return sybm;
    }
    public void setSybm(java.lang.String sybm) {
        this.sybm = sybm;
    }

    public java.math.BigDecimal getSbdj() {
        return sbdj;
    }
    public void setSbdj(java.math.BigDecimal sbdj) {
        this.sbdj = sbdj;
    }

    public java.lang.Integer getSl() {
        return sl;
    }
    public void setSl(java.lang.Integer sl) {
        this.sl = sl;
    }

    public java.lang.String getCgsj() {
        return cgsj;
    }
    public void setCgsj(java.lang.String cgsj) {
        this.cgsj = cgsj;
    }

    public java.lang.String getSzwz() {
        return szwz;
    }
    public void setSzwz(java.lang.String szwz) {
        this.szwz = szwz;
    }

    public java.lang.String getSfyp() {
        return sfyp;
    }
    public void setSfyp(java.lang.String sfyp) {
        this.sfyp = sfyp;
    }

    public java.lang.String getSbbh() {
        return sbbh;
    }
    public void setSbbh(java.lang.String sbbh) {
        this.sbbh= sbbh;
    }

    public java.lang.String getCpgg() {
        return cpgg;
    }
    public void setCpgg(java.lang.String cpgg) {
        this.cpgg= cpgg;
    }

    public java.lang.String getCpxh() {
        return cpxh;
    }
    public void setCpxh(java.lang.String cpxh) {
        this.cpxh= cpxh;
    }

    public java.lang.String getSbtp() {
        return sbtp;
    }
    public void setSbtp(java.lang.String sbtp) {
        this.sbtp= sbtp;
    }

    public java.math.BigDecimal getSbzs() {
        return sbzs;
    }
    public void setSbzs(java.math.BigDecimal sbzs) {
        this.sbzs= sbzs;
    }

    public java.lang.String getJbr() {
        return jbr;
    }
    public void setJbr(java.lang.String jbr) {
        this.jbr= jbr;
    }


    public java.lang.String getSbfzr() {
        return sbfzr;
    }
    public void setSbfzr(java.lang.String sbfzr) {
        this.sbfzr= sbfzr;
    }

    public java.lang.String getBz() {
        return bz;
    }
    public void setBz(java.lang.String bz) {
        this.bz= bz;
    }


    public java.lang.Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(java.lang.Boolean isCheck) {
        this.isCheck = isCheck;
    }


}
