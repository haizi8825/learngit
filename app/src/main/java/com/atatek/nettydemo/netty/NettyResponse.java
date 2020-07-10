package com.atatek.nettydemo.netty;

public class NettyResponse {

    /**
     * equipmentNo : BD900011111
     * reqID : 200f6696c3ba4812849eface8d065d61
     */

    private String equipmentNo;
    private String reqID;
    private int code ;//0 成功 ，其他失败

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
