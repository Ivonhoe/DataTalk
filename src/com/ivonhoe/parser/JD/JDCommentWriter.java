package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.Unit;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import com.ivonhoe.parser.io.ExlWriter;
import com.ivonhoe.parser.JD.unit.WebCommentUnit;
import util.Log;

import java.io.File;
import java.util.List;

/**
 * Output result to excel or text
 *
 * @author ivonhoe
 */
public class JDCommentWriter extends ExlWriter {

    public static final String[] TITLE_STRINGS = {
            "nickname", "userLevelName", "productColor", "userProvince", "title", "referenceName",
            "content", "creationTime", "commentTags", "score", "usefulVoteCount",
            "uselessVoteCount"
    };

    public static final int TYPE_TEXT = 0x00;
    public static final int TYPE_EXL = 0x01;

    public static final int TYPE_ANDAPP = 0x10;
    public static final int TYPE_WEB = 0x11;

    private int writeRow = 0;

    public JDCommentWriter(String path) {
        super(new File(path));
    }

    public JDCommentWriter(File file) {
        super(file);
    }

    @Override
    public void onWrite(List<Unit> list) {
        // TODO Auto-generated method stub
        exportToSheet("comments", TITLE_STRINGS, list);
    }

    private void exportToSheet(String sheetName, String[] title, List<Unit> listContent) {
        try {
            if (mWorkbook == null) {
                return;
            }

            WritableSheet sheet = mWorkbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = mWorkbook.createSheet(sheetName, mWorkbook.getSheets().length);
                jxl.SheetSettings sheetset = sheet.getSettings();
                sheetset.setProtected(false);

                for (int i = 0; i < title.length; i++) {
                    sheet.addCell(new Label(i, 0, title[i], wcfCenter));
                    writeRow = 1;
                }
            }

            for (int i = 0; i < listContent.size(); i++) {
                WebCommentUnit unit = (WebCommentUnit) listContent.get(i);
                sheet.addCell(new Label(0, writeRow, unit.getNickname(), wcfLeft));// nickname
                sheet.addCell(
                        new Label(1, writeRow, unit.getUserLevelName(), wcfLeft));// userLevelName
                sheet.addCell(
                        new Label(2, writeRow, unit.getProductColor(), wcfLeft));// productColor
                sheet.addCell(
                        new Label(3, writeRow, unit.getUserProvince(), wcfLeft));// userProvince
                sheet.addCell(new Label(4, writeRow, unit.getTitle(), wcfLeft));// title
                sheet.addCell(
                        new Label(5, writeRow, unit.getReferenceName(), wcfLeft));// referenceName
                sheet.addCell(new Label(6, writeRow, unit.getContent(), wcfLeft));// content
                sheet.addCell(
                        new Label(7, writeRow, unit.getCreationTime(), wcfLeft));// creationTime
                sheet.addCell(
                        new Label(8, writeRow, unit.getCommentTags(), wcfLeft));// commentTags
                sheet.addCell(new Label(9, writeRow,
                        String.valueOf(unit.getScore()), wcfLeft));// score
                sheet.addCell(new Label(10, writeRow, String.valueOf(unit.getUsefulVoteCount()),
                        wcfLeft));// usefulVoteCount
                sheet.addCell(new Label(11, writeRow, String.valueOf(unit.getUselessVoteCount()),
                        wcfLeft));// uselessVoteCount
                writeRow++;
            }
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
