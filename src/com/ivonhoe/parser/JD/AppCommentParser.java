/*
package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.JD.nouse.SimpleCommentParserFactory;
import com.ivonhoe.parser.JD.unit.AppCommentUnit;

import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppCommentParser {

    private static StringBuffer mBuffer = new StringBuffer();

    private static void onCompile(String line) {
        try {
            FileWriter fileWriter = new FileWriter(SimpleCommentParserFactory.filePath + "out_"
                    + SimpleCommentParserFactory.getCurrentFileName(), true);

            Pattern pattern = Pattern
                    .compile("\\{\"replyCount\":\\d*,\".*?\":\\d*,\".*?\":\\d*,\".*?\":.*?,\"score\":\\d,\"attribute\":.*?]],");
            Matcher matcher = pattern.matcher(line);
            boolean result = matcher.find();
            while (result) {
                mBuffer.append(line, matcher.start(), matcher.end());
                System.out.println(mBuffer.toString());
                AppCommentUnit con = new AppCommentUnit(mBuffer.toString());
                // fileWriter.write("userId="+con.userId+"\r\n");
                fileWriter.write("totalCount=" + con.getTotalCount() + "\r\n");
                fileWriter.write("score=" + con.getScore() + "\r\n");
                fileWriter.write("comment=" + con.getComment() + "\r\n");
                fileWriter.write("color=" + con.getColor() + "\r\n");
                fileWriter.write("mode=" + con.getMode() + "\r\n");
                fileWriter.write("buyTime=" + con.getBuyTime() + "\r\n");
                fileWriter.write("---------------------------" + "\r\n");
                result = matcher.find();
                mBuffer.setLength(0);
            }
            fileWriter.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
*/
