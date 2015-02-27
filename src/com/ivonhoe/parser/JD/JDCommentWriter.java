package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.Unit;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import com.ivonhoe.parser.ExlWriter;
import com.ivonhoe.parser.JD.unit.WebCommentUnit;

import java.io.File;
import java.util.List;

/**
 * Output result to excel or text
 *
 * @author Fan.Yang.NB
 */
public class JDCommentWriter extends ExlWriter {

    private static final String XLS_NAME = "JDComments.xls";
    public static final int TYPE_TEXT = 0x00;
    public static final int TYPE_EXL = 0x01;

    public static final int TYPE_ANDAPP = 0x10;
    public static final int TYPE_WEB = 0x11;

    private WritableWorkbook mWorkbook = null;
    private WritableFont normalFont;
    private WritableFont boldFont;
    private WritableCellFormat wcfCenter;
    private WritableCellFormat wcfLeft;
    private int writedRow = 0;

    public JDCommentWriter(File file) {
        super(file);
    }

    @Override
    public void onWrite(List<Unit> list) {
        // TODO Auto-generated method stub
        exportToSheet(SimpleCommentParserFactory.getCurrentFileName(),
                SimpleCommentParserFactory.TITLE_STRINGS, list);
    }

    private void exportToSheet(String sheetName, String[] title, List<Unit> listContent) {
        try {
            if (mWorkbook == null) {
                return;
            }

            WritableSheet sheet = mWorkbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("null,sheetName=" + sheetName);
                sheet = mWorkbook.createSheet(sheetName, 0);
                jxl.SheetSettings sheetset = sheet.getSettings();
                sheetset.setProtected(false);

                for (int i = 0; i < title.length; i++) {
                    sheet.addCell(new Label(i, 0, title[i], wcfCenter));
                    writedRow = 1;
                }
            }

            for (int i = 0; i < listContent.size(); i++) {
                WebCommentUnit unit = (WebCommentUnit) listContent.get(i);
                sheet.addCell(new Label(0, writedRow, unit.getNickname(), wcfLeft));// nickname
                sheet.addCell(new Label(1, writedRow, unit.getUserLevelName(), wcfLeft));// userLevelName
                sheet.addCell(new Label(2, writedRow, unit.getProductColor(), wcfLeft));// productColor
                sheet.addCell(new Label(3, writedRow, unit.getUserProvince(), wcfLeft));// userProvince
                sheet.addCell(new Label(4, writedRow, unit.getTitle(), wcfLeft));// title
                sheet.addCell(new Label(5, writedRow, unit.getReferenceName(), wcfLeft));// referenceName
                sheet.addCell(new Label(6, writedRow, unit.getContent(), wcfLeft));// content
                sheet.addCell(new Label(7, writedRow, unit.getCreationTime(), wcfLeft));// creationTime
                sheet.addCell(new Label(8, writedRow, unit.getCommentTags(), wcfLeft));// commentTags
                sheet.addCell(new Label(9, writedRow,
                        String.valueOf(unit.getScore()), wcfLeft));// score
                sheet.addCell(new Label(10, writedRow, String.valueOf(unit.getUsefulVoteCount()),
                        wcfLeft));// usefulVoteCount
                sheet.addCell(new Label(11, writedRow, String.valueOf(unit.getUselessVoteCount()),
                        wcfLeft));// uselessVoteCount
                writedRow++;
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
