package com.avic.demo.sebeiglzx.bean;

import java.io.Serializable;

/*
* RFID实体类*/
public class InitProRfidFkbBean implements Serializable {

    /** 主键 */
    private java.lang.Integer id;
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
    private java.lang.String sfQs;
    /** 是否审核 */
    private java.lang.String shbz;
    /** 仓库ID */
    private java.lang.Integer ckId;
    /** 仓库名称 */
    private java.lang.String ckmc;
    /** 标签号 */
    private java.lang.String bqh;
    /** RFID卡号 */
    private java.lang.String rfidTid;
    /** 入库单号 */
    private java.lang.String rkdh;
    /** 入库子表ID */
    private java.lang.Integer rkzbId;
    /** 入库日期 */
    private java.util.Date rkrq;
    /** 生产线ID */
    private java.lang.String scxId;
    /** 生产线名称 */
    private java.lang.String scxMc;
    /** 产品ID */
    private java.lang.Integer cpId;
    /** 产品编码 */
    private java.lang.String cpbm;
    /** 批次号 */
    private java.lang.String pch;
    /** 产品名称 */
    private java.lang.String cpmc;
    /** 产品型号 */
    private java.lang.String cpxh;
    /** 产品规格 */
    private java.lang.String cpgg;
    /** 产品图号 */
    private java.lang.String cpth;
    /** 货架号 */
    private java.lang.String hjh;
    /** 计量单位 */
    private java.lang.String jldw;
    /** 数量 */
    private java.math.BigDecimal sl;

    /** 合格数量 */
    private java.math.BigDecimal hgsl;

    /** 整包数 */
    private java.lang.Integer zbs;
    /** 零包装数量 */
    private java.lang.Integer lbzsl;
    /** 整盒包装数 */
    private java.lang.Integer zhbzs;
    /** 卡状态 */
    private java.lang.String kzt;
    /** 油封期限 */
    private java.lang.Integer yfqx;
    /** 是否在油封期 */
    private java.lang.String isYfq;
    /** 备注 */
    private java.lang.String bz;

    private java.lang.Boolean isCheck;

    /** RFID卡号EPC */
    private java.lang.String rfidEpc;

    /** 试验件数量 */
    private java.math.BigDecimal syjsl;

    /**
     * 盘点单号
     */
    private java.lang.String pddh;

    /** 装运号 */
    private java.lang.String zyh;

    private java.lang.Boolean curCheck;

    private java.lang.String lph;
    private java.lang.String gylxh;
    private java.lang.String jgz;//加工者
    private java.lang.String jgzgh;//加工工号
    private String dqgx;//当前工序

    private String useLength;
    private String clph;
    private String dbh;
    private String orderNo;


    public String getClph() {
        return clph;
    }

    public void setClph(String clph) {
        this.clph = clph;
    }

    public String getUseLength() {
        return useLength;
    }

    public void setUseLength(String useLength) {
        this.useLength = useLength;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
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

    public java.lang.String getSfQs() {
        return sfQs;
    }

    public void setSfQs(java.lang.String sfQs) {
        this.sfQs = sfQs;
    }

    public java.lang.String getShbz() {
        return shbz;
    }

    public void setShbz(java.lang.String shbz) {
        this.shbz = shbz;
    }

    public java.lang.Integer getCkId() {
        return ckId;
    }

    public void setCkId(java.lang.Integer ckId) {
        this.ckId = ckId;
    }

    public java.lang.String getCkmc() {
        return ckmc;
    }

    public void setCkmc(java.lang.String ckmc) {
        this.ckmc = ckmc;
    }

    public java.lang.String getBqh() {
        return bqh;
    }

    public void setBqh(java.lang.String bqh) {
        this.bqh = bqh;
    }

    public java.lang.String getRfidTid() {
        return rfidTid;
    }

    public void setRfidTid(java.lang.String rfidTid) {
        this.rfidTid = rfidTid;
    }

    public java.lang.String getRkdh() {
        return rkdh;
    }

    public void setRkdh(java.lang.String rkdh) {
        this.rkdh = rkdh;
    }

    public java.lang.Integer getRkzbId() {
        return rkzbId;
    }

    public void setRkzbId(java.lang.Integer rkzbId) {
        this.rkzbId = rkzbId;
    }

    public java.util.Date getRkrq() {
        return rkrq;
    }

    public void setRkrq(java.util.Date rkrq) {
        this.rkrq = rkrq;
    }

    public java.lang.String getScxId() {
        return scxId;
    }

    public void setScxId(java.lang.String scxId) {
        this.scxId = scxId;
    }

    public java.lang.String getScxMc() {
        return scxMc;
    }

    public void setScxMc(java.lang.String scxMc) {
        this.scxMc = scxMc;
    }

    public java.lang.Integer getCpId() {
        return cpId;
    }

    public void setCpId(java.lang.Integer cpId) {
        this.cpId = cpId;
    }

    public java.lang.String getCpbm() {
        return cpbm;
    }

    public void setCpbm(java.lang.String cpbm) {
        this.cpbm = cpbm;
    }

    public java.lang.String getPch() {
        return pch;
    }

    public void setPch(java.lang.String pch) {
        this.pch = pch;
    }

    public java.lang.String getCpmc() {
        return cpmc;
    }

    public void setCpmc(java.lang.String cpmc) {
        this.cpmc = cpmc;
    }

    public java.lang.String getCpxh() {
        return cpxh;
    }

    public void setCpxh(java.lang.String cpxh) {
        this.cpxh = cpxh;
    }

    public java.lang.String getCpgg() {
        return cpgg;
    }

    public void setCpgg(java.lang.String cpgg) {
        this.cpgg = cpgg;
    }

    public java.lang.String getCpth() {
        return cpth;
    }

    public void setCpth(java.lang.String cpth) {
        this.cpth = cpth;
    }

    public java.lang.String getHjh() {
        return hjh;
    }

    public void setHjh(java.lang.String hjh) {
        this.hjh = hjh;
    }

    public java.lang.String getJldw() {
        return jldw;
    }

    public void setJldw(java.lang.String jldw) {
        this.jldw = jldw;
    }

    public java.math.BigDecimal getSl() {
        return sl;
    }

    public void setSl(java.math.BigDecimal sl) {
        this.sl = sl;
    }

    public java.lang.Integer getZbs() {
        return zbs;
    }

    public void setZbs(java.lang.Integer zbs) {
        this.zbs = zbs;
    }

    public java.lang.Integer getLbzsl() {
        return lbzsl;
    }

    public void setLbzsl(java.lang.Integer lbzsl) {
        this.lbzsl = lbzsl;
    }

    public java.lang.Integer getZhbzs() {
        return zhbzs;
    }

    public void setZhbzs(java.lang.Integer zhbzs) {
        this.zhbzs = zhbzs;
    }

    public java.lang.String getKzt() {
        return kzt;
    }

    public void setKzt(java.lang.String kzt) {
        this.kzt = kzt;
    }

    public java.lang.Integer getYfqx() {
        return yfqx;
    }

    public void setYfqx(java.lang.Integer yfqx) {
        this.yfqx = yfqx;
    }

    public java.lang.String getIsYfq() {
        return isYfq;
    }

    public void setIsYfq(java.lang.String isYfq) {
        this.isYfq = isYfq;
    }

    public java.lang.String getBz() {
        return bz;
    }

    public void setBz(java.lang.String bz) {
        this.bz = bz;
    }

    public java.lang.Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(java.lang.Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public java.lang.String getPddh() {
        return pddh;
    }

    public void setPddh(java.lang.String pddh) {
        this.pddh = pddh;
    }

    public java.lang.String getRfidEpc() {
        return rfidEpc;
    }

    public void setRfidEpc(java.lang.String rfidEpc) {
        this.rfidEpc = rfidEpc;
    }

    public java.math.BigDecimal getSyjsl() {
        return syjsl;
    }

    public void setSyjsl(java.math.BigDecimal syjsl) {
        this.syjsl = syjsl;
    }

    public java.lang.String getZyh() {
        return zyh;
    }

    public void setZyh(java.lang.String zyh) {
        this.zyh = zyh;
    }

    public java.lang.Boolean getCurCheck() {
        return curCheck;
    }

    public void setCurCheck(java.lang.Boolean curCheck) {
        this.curCheck = curCheck;
    }

    public java.lang.String getLph() {
        return lph;
    }

    public void setLph(java.lang.String lph) {
        this.lph = lph;
    }

    public java.math.BigDecimal getHgsl() {
        return hgsl;
    }

    public void setHgsl(java.math.BigDecimal hgsl) {
        this.hgsl = hgsl;
    }

    public java.lang.String getGylxh() {
        return gylxh;
    }

    public void setGylxh(java.lang.String gylxh) {
        this.gylxh = gylxh;
    }

    public java.lang.String getJgz() {
        return jgz;
    }

    public void setJgz(java.lang.String jgz) {
        this.jgz = jgz;
    }

    public java.lang.String getJgzgh() {
        return jgzgh;
    }

    public void setJgzgh(java.lang.String jgzgh) {
        this.jgzgh = jgzgh;
    }

    public String getDqgx() {
        return dqgx;
    }

    public void setDqgx(String dqgx) {
        this.dqgx = dqgx;
    }

    public String getDbh() {
        return dbh;
    }

    public void setDbh(String dbh) {
        this.dbh = dbh;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
