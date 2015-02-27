package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.Writer;
import com.ivonhoe.parser.JD.unit.WebCommentUnit;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TextWriter extends Writer {

    private int count =0;
    
    @Override
    public void onWrite(List<Unit> list) {
        // TODO Auto-generated method stub
        FileWriter fileWriter;
        try {
            fileWriter = new
                    FileWriter(SimpleCommentParserFactory.filePath + "out_" +
                            SimpleCommentParserFactory.getCurrentFileName() + ".txt", true);
            for (int i = 0; i < list.size(); i++) {
                count++;
                WebCommentUnit commentUnit = (WebCommentUnit)list.get(i);
                fileWriter.write(commentUnit.getContent() + "\r\n");
                fileWriter.write(commentUnit.getCommentTags() + "\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }
}
