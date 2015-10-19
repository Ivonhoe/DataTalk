package com.ivonhoe.parser.engine;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.io.IReader;
import com.ivonhoe.parser.io.IWriter;
import com.ivonhoe.parser.io.LineReader;
import util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ivonhoe on 15-5-28.
 */
public class Engine implements IParser.ParseListener {

    ArrayList<File> mSourceFile = new ArrayList<File>();

    IReader mDataProducer;
    IParser mDataParser;
    IWriter mDataWriter;
    Container mContainer;

    public long mDebugTime;

    public static Counter mCounter = new Counter();

    public static class Counter {
        static int count = 0;

        static void add() {
            ++count;
        }

        public static int get() {
            return count;
        }
    }

    public Engine() {
        mContainer = new Container();
    }

    public void setSourcePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("path:" + path + " is not exist");
            return;
        }

        mSourceFile.clear();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File temp : listFiles) {
                if (!temp.isDirectory())
                    mSourceFile.add(temp);
            }
        } else {
            mSourceFile.add(file);
        }
        Log.d("TO DO source file:" + mSourceFile.size());
    }

    protected IReader createReader() {
        mDataProducer = new LineReader(this);
        return mDataProducer;
    }

    public synchronized void setReader(IReader reader) {
        mDataProducer = reader;
    }

    private IReader getDataProducer() {
        if (mDataProducer == null) {
            synchronized (Engine.class) {
                if (mDataProducer == null) {
                    mDataProducer = createReader();
                }
            }
        }
        return mDataProducer;
    }

    public InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setParser(IParser parser) {
        mDataParser = parser;
        mDataParser.setParseListener(this);
    }

    public void start() {
        mDebugTime = System.currentTimeMillis();

        for (File file : mSourceFile) {
            InputStream inputStream = getInputStream(file);
            Log.e("+++ add source file:" + file.getAbsolutePath() + ",input:" + inputStream);
            getDataProducer().read(inputStream);
        }

        if (mDataParser != null && !mSourceFile.isEmpty()) {
            mDataParser.start();
        } else {
            synchronized (mCounter) {
                mCounter.add();
                mCounter.notifyAll();
            }
        }
    }

    public void produce(List<Object> data) {
        if (mContainer != null) {
            mContainer.addProduct(data);
        }
    }

    public List<Object> getProductPool() {
        if (mContainer != null)
            return mContainer.mProductPool;

        return null;
    }

    public void addParsedUnit(List<Unit> data) {
        if (data == null) {
            return;
        }
        mContainer.addParsedUnit(data);
    }

    public boolean isProduceComplete() {
        return getDataProducer().isProduceComplete();
    }

    public void setWriter(IWriter writer) {
        this.mDataWriter = writer;
    }

    @Override
    public void onParseComplete() {
        if (mDataParser.consumeFinish()) {
            if (mDataWriter != null) {
                mDataWriter.onWrite(mContainer.mConsumedPool);
                mDataWriter.close();
            }

            Log.d("******PARSE FINISH*******, produce :" + mContainer.mConsumedPool.size() +
                    ",use time:" + (System.currentTimeMillis() - mDebugTime));
            synchronized (mCounter) {
                mCounter.add();
                mCounter.notifyAll();
            }
        }
    }

    public List<Unit> getParseResult() {
        return mContainer.mConsumedPool;
    }

    class Container {
        private final int MAX_SIZE = 10000;
        /**
         * 生产者的产品容器
         */
        private LinkedList<Object> mProductPool = new LinkedList<Object>();

        /**
         * 消费者处理数据后的容器
         */
        private List<Unit> mConsumedPool = new ArrayList<Unit>();

        private boolean addProduct(String product) {
            boolean result;
            synchronized (mProductPool) {

                while (mProductPool.size() >= MAX_SIZE) {
                    Log.d("Product poll is full, time:" + (System.currentTimeMillis() - mDebugTime)
                            + ", size:" + mProductPool.size());
                    try {
                        mProductPool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                result = mProductPool.add(product);

                mProductPool.notifyAll();
            }

            return result;
        }

        private void addProduct(List<Object> data) {
            while (!data.isEmpty()) {
                synchronized (mProductPool) {
                    while (mProductPool.size() >= MAX_SIZE) {
                        Log.d("Product poll is full, debug time:" +
                                (System.currentTimeMillis() - mDebugTime)
                                + ", product pool size:" + mProductPool.size() +
                                ", produce wait !");
                        try {
                            mProductPool.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    getDataProducer().startProduce();

                    for (int i = 0; i < Math.min(MAX_SIZE, data.size()) &&
                            mProductPool.size() < MAX_SIZE; i++) {
                        if (!data.isEmpty()) {
                            mProductPool.add(data.get(0));
                            data.remove(0);
                        }
                    }
                    mProductPool.notifyAll();
                    Log.w("Produce:" + data.size() + ", mProductPool size:" + mProductPool.size());
                }
            }
        }

        private synchronized void addParsedUnit(List<Unit> data) {
            mConsumedPool.addAll(data);
            Log.w("mConsumedPool size:" + mConsumedPool.size());
        }
    }
}
