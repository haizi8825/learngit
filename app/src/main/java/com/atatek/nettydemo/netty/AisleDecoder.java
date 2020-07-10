package com.atatek.nettydemo.netty;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码器
 *
 */
public class AisleDecoder extends ByteToMessageDecoder {


    private static final int HEADER_LENGTH = 12;

//    /**
//     * @param maxFrameLength      单个包最大的长度
//     * @param lengthFieldOffset   表示数据长度字段开始的偏移量
//     * @param lengthFieldLength   数据长度字段的所占的字节数
//     * @param lengthAdjustment    lengthAdjustment + 数据长度取值 = 数据长度字段之后剩下包的字节数
//     * @param initialBytesToStrip 从整个包第一个字节开始，向后忽略的字节数
//     * @param failFast            为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异
//     */
//    public AisleDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
//        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
//    }
//
//    @Override
//    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        //在这里调用父类的方法,实现指得到想要的部分,我在这里全部都要,也可以只要body部分
//        in = (ByteBuf) super.decode(ctx, in);
//
//        if (in == null) {
//            return null;
//        }
//        if (in.readableBytes() < HEADER_LENGTH) {
//            throw new Exception("字节数不足");
//        }
//        //读取version字段
//        int version = in.readInt();
//        //读取contentLength字段
//        int contentLength = in.readInt();
//        //读取serviceID字段
//        int serviceID = in.readInt();
//        if (in.readableBytes() != contentLength) {
//            throw new Exception("标记的长度不符合实际长度");
//        }
//        //读取body
//        byte[] bytes = new byte[contentLength];
//        LOGGER.debug("bytes.length={}", bytes.length);
//        in.readBytes(bytes);
//        String base64Str = new String(bytes, StandardCharsets.UTF_8);
//        LOGGER.debug("base64Str:{}", base64Str);
//        return new AisleMessage(new AisleMsgHeader(version, contentLength, serviceID), base64Str);
//    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 长度不足，退出
        if (in.readableBytes() < HEADER_LENGTH) {
            return;
        }
        // 获取协议的版本
        int version = in.readInt();
        // 获取消息长度
        int contentLength = in.readInt();
        // 获取serviceID
        int serviceID = in.readInt();
        // 组装协议头
        AisleMsgHeader header = new AisleMsgHeader(version, contentLength, serviceID);
        // 长度不足重置读index，退出
        if (in.readableBytes() < contentLength) {
            in.setIndex(in.readerIndex() - HEADER_LENGTH, in.writerIndex());
            return;
        }
        AisleMessage message;
        if (contentLength > 0) {
            byte[] content = new byte[contentLength];
            // 读取消息内容
            in.readBytes(content);
            // 组装bean
            message = new AisleMessage(header, new String(content, StandardCharsets.UTF_8));
        } else {
            message = new AisleMessage(header, null);
        }

        out.add(message);
    }
}
