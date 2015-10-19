package com.ivonhoe.parser.io;

import com.ivonhoe.parser.engine.Engine;
import util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ivonhoe on 15-5-28.
 */
public abstract class IReader {

    protected static final ExecutorService mThreadPool = Executors.newFixedThreadPool(1);

    protected Engine mEngine;

    protected ArrayList<InputStream> mOnlineStream = new ArrayList<InputStream>();

    private boolean isProduceStart;
    private boolean isProduceComplete;

    public void read(final InputStream inputStream) {
        if (inputStream == null)
            return;

        isProduceStart = false;
        isProduceComplete = false;
        mOnlineStream.add(inputStream);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    onRead(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThreadPool.execute(runnable);
    }

    protected abstract void onRead(final InputStream inputStream) throws IOException;

    protected void onProduceComplete(InputStream inputStream) {
        mOnlineStream.remove(inputStream);

        isProduceComplete = mOnlineStream.isEmpty();
    }

    public boolean isProduceComplete() {
        return isProduceStart && isProduceComplete;
    }

    public void startProduce() {
        isProduceStart = true;
    }
}
