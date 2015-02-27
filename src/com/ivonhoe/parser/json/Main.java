package com.ivonhoe.parser.json;

import java.io.*;

/**
 * Created by ivonhoe on 15-1-27.
 */
public class Main {

    public static void main(String[] args)
            throws IOException {

        File f = new File("./data/yingyonghui");
        File[] files = f.listFiles();

        File testFile = new File("./data/yingyonghui/test.txt");
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(testFile));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,
                "utf-8"), 5 * 1024 * 1024);
        JsonParser parser = new JsonParser(reader);
        parser.parse();
    }

}
