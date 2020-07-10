package com.atatek.nettydemo.netty;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {
    private EventLoopGroup nioEventLoopGroup;
    private volatile static NettyClient nettyClient;


    private volatile Channel channel;

    private boolean isConnect = false;
    private Gson gson;
    private Bootstrap bootstrap;

    private volatile boolean connectStatus;

    public NettyClient() {
        gson = new Gson();
    }

    public static NettyClient getInstance() {
        if (nettyClient == null) {
            synchronized (NettyClient.class) {
                if (nettyClient == null) {
                    nettyClient = new NettyClient();
                }
            }
        }
        return nettyClient;
    }

    public void start() {
        nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new IdleStateHandler(0, 50, 0));
                        p.addLast(new AisleDecoder());
                        p.addLast(new AisleEncoder());
                        p.addLast(new ClientHandler());
                    }
                });
        doConnect();
    }

    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
//        Log.i("whb", "正在连接，当前线程是" + Thread.currentThread().getName());
        String socketHost ="192.168.1.100";
        String socketPort = "8088";
        if (TextUtils.isEmpty(socketHost) || TextUtils.isEmpty(socketPort)) return;
        try {
            Integer port = Integer.valueOf(socketPort);
            ChannelFuture future = bootstrap.connect(socketHost, port);
            future.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    if (channel != null && channel.isActive()) {
                        channel.closeFuture();
                    }
                    channel = channelFuture.channel();
                } else {
                    future.channel();
                    setConnectStatus(false);
                    SystemClock.sleep(500);
                    doConnect();
                }
            });
        } catch (Exception e) {
            Log.e("whb",e.getMessage());
        }
    }


    public Channel getChannel() {
        return channel;
    }

    public synchronized boolean getConnectStatus() {
        return connectStatus;
    }


    public void close() {
        try {
            if (nioEventLoopGroup != null) {
                nioEventLoopGroup.shutdownGracefully();
                setConnectStatus(false);
                nioEventLoopGroup = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void setConnectStatus(boolean b) {
        connectStatus = b;
    }

}
