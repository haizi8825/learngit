package com.atatek.nettydemo.netty;

public interface NettyConstans {
    int PROTOCOL_VERSION=1;
    int HEARTBEAT=1001;
    int REGISTER=1002;
    int BUSINESS_OPEN_DOOR=2001;//开门业务
    int BUSINESS_SYNC_VISTOR=2002;//同步访客列表
}
