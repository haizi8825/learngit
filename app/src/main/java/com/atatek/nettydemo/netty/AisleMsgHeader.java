package com.atatek.nettydemo.netty;

public class AisleMsgHeader {

    // 协议版本
    private int version;
    // 消息内容长度
    private int contentLength;
    // 服务ID
    private int serviceID;

    public AisleMsgHeader(int version, int contentLength, int serviceID) {
        this.version = version;
        this.contentLength = contentLength;
        this.serviceID = serviceID;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
}
