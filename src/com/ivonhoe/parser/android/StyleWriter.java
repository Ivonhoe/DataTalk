package com.ivonhoe.parser.android;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.ivonhoe.parser.ExlWriter;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.android.unit.Style;
import com.ivonhoe.parser.android.unit.StyleItem;

import java.io.File;
import java.util.List;

/**
 * Created by ivonhoe on 15-1-27.
 */
public class StyleWriter extends ExlWriter {

    public StyleWriter(File file) {
        super(file);
    }

    @Override
    public void onWrite(List<Unit> list) {
        String sheetName = ((Style) list.get(0)).getName();

        try {
            if (mWorkbook == null) {
                return;
            }

            WritableSheet sheet = mWorkbook.getSheet(sheetName);

            if (sheet == null) {
                sheet = mWorkbook.createSheet(sheetName, mWorkbook.getSheets().length);
                jxl.SheetSettings sheetSet = sheet.getSettings();
                sheetSet.setProtected(false);
            }

            int beginRow = 0;
            int maxRow = 0;
            int column = 0;

            for (int i = 0; i < list.size(); i++) {
                Style style = (Style) list.get(i);
                if (column > 255) {
                    column = column - 256;
                    beginRow = maxRow + 1;
                }
                sheet.addCell(new Label(column, beginRow, style.getName(), wcfCenter));
                List<StyleItem> items = style.getItems();
                int row = beginRow + 1;

                for (StyleItem item : items) {
                    sheet.addCell(new Label(column, row, item.getName() + "/" + item.getValue(),
                            wcfLeft));
                    row++;
                }
                maxRow = Math.max(maxRow, row);
                column++;
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
