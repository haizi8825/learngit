package com.atatek.nettydemo;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atatek.nettydemo.netty.NettyService;
import com.atatek.nettydemo.utils.ThreadPoolFactory;

public class MainActivity extends AppCompatActivity {

    private Intent nettyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nettyService =new Intent(this, NettyService.class);
        startService(nettyService);
    }

    @Override
    protected void onDestroy() {
        stopService(nettyService);
        super.onDestroy();
        ThreadPoolFactory.getInstance().releaseAllThreads();
    }
}
