package com.ivonhoe.parser.io;

import com.ivonhoe.parser.engine.Engine;
import util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ivonhoe on 15-5-28.
 */
public class LineReader extends IReader {

    private String mEncode;

    public LineReader(Engine engine) {
        mEngine = engine;
        mEncode = "utf-8";
    }

    public LineReader(Engine engine, String encode) {
        mEngine = engine;
        mEncode = encode;
    }

    protected void onRead(InputStream inputStream) throws IOException {
        List<Object> readResult = new ArrayList<Object>();
        BufferedInputStream bufferedStream = null;
        if (!(inputStream instanceof BufferedInputStream)) {
            bufferedStream = new BufferedInputStream(inputStream);
        } else {
            bufferedStream = (BufferedInputStream) inputStream;
        }
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(bufferedStream, mEncode), 5 * 1024 * 1024);

            String line;
            while ((line = reader.readLine()) != null) {
                readResult.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Log.e("**** produce:" + inputStream);
            mEngine.produce(readResult);

            //Log.e("**** on complete:" + inputStream);
            onProduceComplete(inputStream);
            inputStream.close();
        }
    }
}
