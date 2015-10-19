package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.engine.Engine;
import com.ivonhoe.parser.io.IWriter;
import com.ivonhoe.parser.io.LineReader;

/**
 * @author ivonhoe
 */
public class JDComment {

    public static void main(String[] args) {
        JDComment jdComment = new JDComment();
        Engine jdEngine = jdComment.initJDComment();
        jdEngine.start();
    }

    Engine jdEngine;

    private Engine initJDComment() {
        if (jdEngine != null) {
            return jdEngine;
        }
        jdEngine = new Engine();
        jdEngine.setReader(new LineReader(jdEngine, "gbk"));
        jdEngine.setSourcePath("data/JD/web");
        jdEngine.setParser(new JDWebParser(jdEngine));
        IWriter jdWrite = new JDCommentWriter("data/JD/out/JDComments.xls");
        jdEngine.setWriter(jdWrite);
        return jdEngine;
    }
}
