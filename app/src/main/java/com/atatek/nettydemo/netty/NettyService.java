package com.atatek.nettydemo.netty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.atatek.nettydemo.utils.ThreadPoolFactory;


/**
 *
 */
public class NettyService extends Service {


    public NettyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NettyClient.getInstance().close();
    }

    private void connect() {
        if (!NettyClient.getInstance().getConnectStatus()) {
            ThreadPoolFactory.getInstance().obtainNettyServicePool().execute(() -> NettyClient.getInstance().start());
        }
    }


}
