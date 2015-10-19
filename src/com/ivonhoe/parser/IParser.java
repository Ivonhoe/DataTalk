package com.ivonhoe.parser;

import com.ivonhoe.parser.engine.Engine;
import util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ivonhoe on 15-5-28.
 */
public abstract class IParser {
    protected static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    protected static final ExecutorService mThreadPool = Executors
            .newFixedThreadPool(CORE_POOL_SIZE);

    protected Engine mEngine;

    /**
     * Each time the number of consumption
     */
    protected int NUM_PER_CONSUME = 1000;

    protected int mCore = CORE_POOL_SIZE;

    private ArrayList<Consumer> mParseConsumer = new ArrayList<Consumer>();

    public abstract ArrayList<Unit> onParse(String source);

    public void start() {
        for (int i = 0; i < mCore; ++i) {
            mParseConsumer.add(0, new Consumer());
            mThreadPool.execute(mParseConsumer.get(0));
        }
    }

    public synchronized boolean consumeFinish() {

        return true;
    }

    private ParseListener mListener;

    public void setParseListener(ParseListener listener) {
        mListener = listener;
    }

    public interface ParseListener {
        void onParseComplete();
    }

    public void complete(Consumer consumer) {
        mParseConsumer.remove(consumer);
        if (mParseConsumer.isEmpty())
            mListener.onParseComplete();
    }

    public class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                List<Object> queue = mEngine.getProductPool();
                if (queue == null) {
                    return;
                }

                List<Object> temp = new ArrayList<Object>();
                synchronized (queue) {
                    while (queue.size() == 0 && !mEngine.isProduceComplete()) {
                        Log.d("Product poll is empty, queue size:" + queue.size() +
                                ", produce completed:" + mEngine.isProduceComplete() +
                                ", consumer wait!");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }

                    int size = queue.size();
                    int k = Math.max(0, size - NUM_PER_CONSUME);
                    for (int i = size - 1; i >= k; i--) {
                        temp.add(queue.get(i));
                        queue.remove(i);
                    }

                    queue.notifyAll();
                }

                int parsedCount = 0;
                for (Object str : temp) {
                    ArrayList<Unit> result = onParse((String) str);
                    if (result != null) {
                        parsedCount += result.size();
                        mEngine.addParsedUnit(result);
                    }
                }
                Log.d("Parse line:" + temp.size() + ",get result:" + parsedCount +
                        ", debug time:" + (System.currentTimeMillis() - mEngine.mDebugTime) +
                        ", current thread:" + Thread.currentThread());

                temp.clear();
                if (mEngine.isProduceComplete() && queue.isEmpty()) {
                    complete(this);
                    Thread t = Thread.currentThread();
                    t.interrupt();
                    return;
                }
            }
        }
    }
}
