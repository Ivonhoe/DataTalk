package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.*;
import org.ivonhoe.parser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan.Yang.NB
 */

public class Main {
    private static Parser mParser = null;
    private static com.ivonhoe.parser.Writer mWriter = null;

    private static List<File> mFilesList = new ArrayList<File>();

    private static void init() {
        mFilesList = SimpleCommentParserFactory.getFileList();

        mWriter = SimpleCommentParserFactory
                .createWriter(SimpleCommentParserFactory.TYPE_EXCEL_WRITER,
                        new File("./data/JD/out.xls"));
    }

    private static void doMain() {
        try {
            for (File file : mFilesList) {
/*                SimpleCommentParserFactory.setCurrentFile(file);
                BufferedInputStream fis = new BufferedInputStream(new
                        FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis,
                        "gbk"), 5 * 1024 * 1024);

                String line = "";
                while ((line = reader.readLine()) != null) {
                    // TODO: write your business
                    if (line.startsWith("jsonp")) {
                        //Create different org.ivonhoe.parser according to different data
                        if (mParser == null || !(mParser instanceof WebCommentParser)) {
                            mParser = SimpleCommentParserFactory
                                    .createParser(SimpleCommentParserFactory.TYPE_WEB_PARSER);
                            mParser.onStart(mWriter);
                        }
                    } else if (line.startsWith("{\"code\":")) {
                        continue;
                    } else {
                        continue;
                    }
                    mParser.onParser(line);
                    mParser.onSave();
                }
                mParser.onEnd();
                mParser = null;*/
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //LTP requirement
        //LTPToolKit.analyzeHttpResponse();

        System.out.println("***********START***********");
        init();
        doMain();
        System.out.println("***********END***********");
    }
}
