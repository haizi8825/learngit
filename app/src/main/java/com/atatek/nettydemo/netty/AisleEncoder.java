package com.atatek.nettydemo.netty;


import android.text.TextUtils;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class AisleEncoder extends MessageToByteEncoder<AisleMessage> {



    @Override
    protected void encode(ChannelHandlerContext tcx, AisleMessage msg,
                          ByteBuf out) throws Exception {
        if(msg == null){
            throw new Exception("msg is null");
        }

        if(TextUtils.isEmpty(msg.getContent())){
            out.writeInt(NettyConstans.PROTOCOL_VERSION);
            out.writeInt(0);
            out.writeInt(msg.getHeader().getServiceID());
        }else {
            byte[] contentBytes = msg.getContent().getBytes(StandardCharsets.UTF_8);
            out.writeInt(NettyConstans.PROTOCOL_VERSION);
            out.writeInt(contentBytes.length);
            out.writeInt(msg.getHeader().getServiceID());
            // 写入消息主体信息
            out.writeBytes(contentBytes);
        }
    }

}
