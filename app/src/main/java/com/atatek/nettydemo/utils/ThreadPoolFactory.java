package com.atatek.nettydemo.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolFactory {
    private volatile static ThreadPoolFactory instance;
    private volatile ThreadPoolExecutor doorPool, facePool, fingerPool, cardPool, resultShowPool, faceDataUpdatePool, recordUpdatePool,nettyServicePool,checkUpdatePool;
    private volatile ThreadPoolExecutor cameraDataPool;

    public static ThreadPoolFactory getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolFactory.class) {
                if (instance == null) {
                    instance = new ThreadPoolFactory();
                }
            }
        }
        return instance;
    }

    public ThreadPoolExecutor obtainFaceWorkPool() {
        if (null == facePool) {
            facePool = new ThreadPoolExecutor(1, 1, 0,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return facePool;
    }

    public ThreadPoolExecutor obtainFingerWorkPool() {
        if (null == fingerPool) {
            fingerPool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return fingerPool;
    }

    public  ThreadPoolExecutor obtainCardWorkPool() {
        if (null == cardPool) {
            cardPool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS,new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return cardPool;
    }

    public ThreadPoolExecutor obtainDataUpdatePool() {
        if (null == faceDataUpdatePool) {
            faceDataUpdatePool = new ThreadPoolExecutor(1, 2, 0L,
                    TimeUnit.SECONDS,  new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return faceDataUpdatePool;
    }

    public ThreadPoolExecutor obtainCameraPool() {
        if (null == cameraDataPool) {
            cameraDataPool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return cameraDataPool;
    }

    public ThreadPoolExecutor obtainRecordUploadPool() {
        if (null == recordUpdatePool) {
            recordUpdatePool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return recordUpdatePool;
    }
    public ThreadPoolExecutor obtainDoorPool() {
        if (null == doorPool) {
            doorPool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return doorPool;
    }
    public ThreadPoolExecutor obtainNettyServicePool() {
        if (null == nettyServicePool) {
            nettyServicePool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS,new LinkedBlockingQueue<>(4), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return nettyServicePool;
    }

    public ThreadPoolExecutor obtainCheckUpdatePool() {
        if (null == checkUpdatePool) {
            checkUpdatePool = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.SECONDS,  new LinkedBlockingQueue<>(4), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return checkUpdatePool;
    }

    public  void releaseAllThreads(){
        if(doorPool!=null){
            doorPool.shutdownNow();
            doorPool=null;
        }
        if(facePool!=null){
            facePool.shutdownNow();
            facePool=null;
        }
        if(fingerPool!=null){
            fingerPool.shutdownNow();
            fingerPool=null;
        }
        if(cardPool!=null){
            cardPool.shutdownNow();
            cardPool=null;
        }
        if(resultShowPool!=null){
            resultShowPool.shutdownNow();
            resultShowPool=null;
        }
        if(faceDataUpdatePool!=null){
            faceDataUpdatePool.shutdownNow();
            faceDataUpdatePool=null;
        }
        if(recordUpdatePool!=null){
            recordUpdatePool.shutdownNow();
            recordUpdatePool=null;
        }
        if(nettyServicePool!=null){
            nettyServicePool.shutdownNow();
            nettyServicePool=null;
        }
        if(checkUpdatePool!=null){
            checkUpdatePool.shutdownNow();
            checkUpdatePool=null;
        }
        if(cameraDataPool!=null){
            cameraDataPool.shutdownNow();
            cameraDataPool=null;
        }
        instance=null;
    }
}
