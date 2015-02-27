package com.ivonhoe.parser;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import java.io.File;
import java.io.IOException;

/**
 * Output result to excel or text
 *
 * @author Fan.Yang.NB
 */
public abstract class ExlWriter extends Writer{

    private static final String XLS_NAME = "JDComments.xls";

    protected WritableWorkbook mWorkbook = null;
    protected WritableFont normalFont;
    protected WritableFont boldFont;
    protected WritableCellFormat wcfCenter;
    protected WritableCellFormat wcfLeft;

    public ExlWriter(File file) {
        initWorkbook(file);
    }

    private void initWorkbook(File file) {
        try {
            if (mWorkbook != null || file == null) {
                return;
            }
            mWorkbook = Workbook.createWorkbook(file);

            normalFont = new WritableFont(WritableFont.ARIAL, 10);
            boldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            // 用于标题居中
            wcfCenter = new WritableCellFormat(boldFont);
            wcfCenter.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcfCenter.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcfCenter.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcfCenter.setWrap(false); // 文字是否换行

            // 用于正文居左
            wcfLeft = new WritableCellFormat(normalFont);
            wcfLeft.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
            wcfLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcfLeft.setAlignment(Alignment.LEFT); // 文字水平对齐
            wcfLeft.setWrap(false); // 文字是否换行

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        try {
            if (mWorkbook != null) {
                mWorkbook.write();
                mWorkbook.close();
            }
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
