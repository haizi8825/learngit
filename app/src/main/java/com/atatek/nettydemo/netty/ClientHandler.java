package com.atatek.nettydemo.netty;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Gson gson;

    public ClientHandler() {
        gson = new Gson();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.i("whb", "channelActive");
        AisleMessage aisleMessage = buildMessage();
        ctx.writeAndFlush(aisleMessage);
        NettyClient.getInstance().setConnectStatus(true);
    }

    private AisleMessage buildMessage() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("equipmentNo", Build.SERIAL);
        String content = gson.toJson(jsonObject);
        try {
            content = Base64.encodeToString(content.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AisleMsgHeader aisleMsgHeader = new AisleMsgHeader(NettyConstans.PROTOCOL_VERSION, content.getBytes().length, NettyConstans.REGISTER);
        AisleMessage aisleMessage = new AisleMessage(aisleMsgHeader, content);
        return aisleMessage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Log.i("whb", "channelRead");
        AisleMessage aisleMessage = (AisleMessage) msg;  //直接转化成协议消息实体
        if (aisleMessage == null || aisleMessage.getHeader() == null) return;
        AtomicReference<String> reqId = new AtomicReference<>("");
        AtomicInteger reqBussnessType = new AtomicInteger();
        Disposable disposable = Observable.just(aisleMessage)
//                filter(obj -> {
//                    int serviceID = obj.getHeader().getServiceID();
//                    if (serviceID == NettyConstans.BUSINESS_OPEN_DOOR || serviceID == NettyConstans.BUSINESS_SYNC_VISTOR) {
//                        return true;
//                    }
//                    return false;
//                })
//                .map(obj -> obj.getContent())
//                .filter(s -> !TextUtils.isEmpty(s))
                .map(message -> {
                    int serviceID = message.getHeader().getServiceID();
                    if(serviceID!=NettyConstans.BUSINESS_OPEN_DOOR&&serviceID!=NettyConstans.BUSINESS_SYNC_VISTOR){
                        return false;
                    }
                    byte[] bytes = Base64.decode(message.getContent(), Base64.NO_WRAP);
                    String str = new String(bytes, "UTF-8");
                    NettyResponse nettyResponse = gson.fromJson(str, NettyResponse.class);
                    reqId.getAndSet(nettyResponse.getReqID());
                    reqBussnessType.getAndSet(serviceID);
                    if (Build.SERIAL.equals(nettyResponse.getEquipmentNo())) {

                        nettyResponse.setCode(0);
                        String content = gson.toJson(nettyResponse);
                        content = Base64.encodeToString(content.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
                        AisleMsgHeader aisleMsgHeader = new AisleMsgHeader(NettyConstans.PROTOCOL_VERSION, content.getBytes(StandardCharsets.UTF_8).length, serviceID);
                        AisleMessage aisle = new AisleMessage(aisleMsgHeader, content);
                        ctx.writeAndFlush(aisle);
                    }
                    return true;
                }).subscribe(aBoolean -> {

                }, throwable -> {
                    Log.e("whb","channelRead异常：" + throwable.getMessage());
                    NettyResponse nettyResponse = new NettyResponse();
                    nettyResponse.setEquipmentNo(Build.SERIAL);
                    nettyResponse.setReqID(reqId.get());
                    nettyResponse.setCode(-1);
                    String content = gson.toJson(nettyResponse);
                    content = Base64.encodeToString(content.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
                    AisleMsgHeader aisleMsgHeader = new AisleMsgHeader(NettyConstans.PROTOCOL_VERSION, content.getBytes(StandardCharsets.UTF_8).length, NettyConstans.BUSINESS_OPEN_DOOR);
                    AisleMessage message = new AisleMessage(aisleMsgHeader, content);
                    ctx.writeAndFlush(message);
                });
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                Log.i("whb", "发送心跳");
                //向服务端发送心跳
                AisleMessage heartBeat = buildHeartMessage();
                ctx.writeAndFlush(heartBeat);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.i("whb", "channelInactive");
        ctx.close();
        NettyClient.getInstance().close();
        NettyClient.getInstance().start();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
        Log.i("whb", "netty异常：" + cause.getMessage());
        ctx.close();

    }


    private AisleMessage buildHeartMessage() {

        AisleMsgHeader aisleMsgHeader = new AisleMsgHeader(NettyConstans.PROTOCOL_VERSION, 1, NettyConstans.HEARTBEAT);
        AisleMessage aisleMessage = new AisleMessage(aisleMsgHeader, "1");

        return aisleMessage;
    }

}
