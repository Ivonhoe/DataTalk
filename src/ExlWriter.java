import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Output result to excel or text
 * 
 * @author Fan.Yang.NB
 */
public class ExlWriter extends CommentWriter {

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

    public ExlWriter() {
    }

    private void initWorkbook() {
        try {
            mWorkbook = Workbook.createWorkbook(new File(SimpleCommentParserFactory.filePath
                    + SimpleCommentParserFactory.getCurrentFileName() + ".xls"));
Log.d("SimpleCommentParserFactory.getCurrentFileName()="+SimpleCommentParserFactory.getCurrentFileName());
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
    public void open() {
        // TODO Auto-generated method stub
        initWorkbook();
    }

    @Override
    public void write(List<WebCommentUnit> list) {
        // TODO Auto-generated method stub
        exportToSheet(SimpleCommentParserFactory.getCurrentFileName(),
                SimpleCommentParserFactory.TITLE_STRINGS, list);
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        closeWorkbook();
    }

    private void closeWorkbook() {
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

    private void exportToSheet(String sheetName, String[] title, List<WebCommentUnit> listContent) {
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
                sheet.addCell(new Label(0, writedRow, listContent.get(i).getNickname(), wcfLeft));// nickname
                sheet.addCell(new Label(1, writedRow, listContent.get(i).getUserLevelName(),
                        wcfLeft));// userLevelName
                sheet.addCell(new Label(2, writedRow, listContent.get(i).getProductColor(), wcfLeft));// productColor
                sheet.addCell(new Label(3, writedRow, listContent.get(i).getUserProvince(), wcfLeft));// userProvince
                sheet.addCell(new Label(4, writedRow, listContent.get(i).getTitle(), wcfLeft));// title
                sheet.addCell(new Label(5, writedRow, listContent.get(i).getReferenceName(),
                        wcfLeft));// referenceName
                sheet.addCell(new Label(6, writedRow, listContent.get(i).getContent(), wcfLeft));// content
                sheet.addCell(new Label(7, writedRow, listContent.get(i).getCreationTime(), wcfLeft));// creationTime
                sheet.addCell(new Label(8, writedRow, listContent.get(i).getCommentTags(), wcfLeft));// commentTags
                sheet.addCell(new Label(9, writedRow,
                        String.valueOf(listContent.get(i).getScore()), wcfLeft));// score
                sheet.addCell(new Label(10, writedRow, String.valueOf(listContent.get(i)
                        .getUsefulVoteCount()), wcfLeft));// usefulVoteCount
                sheet.addCell(new Label(11, writedRow, String.valueOf(listContent.get(i)
                        .getUselessVoteCount()), wcfLeft));// uselessVoteCount
                writedRow++;
                Log.d("writedRow=" + writedRow);
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
