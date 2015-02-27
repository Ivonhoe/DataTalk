package com.ivonhoe.parser.android;

import com.ivonhoe.parser.Parser;
import com.ivonhoe.parser.android.unit.Style;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ivonhoe on 15-1-23.
 */
public class Main {

    private static String[] files = {
            "./data/android-18/xml/styles.xml",
            "./data/android-18/xml/styles_device_defaults.xml",
            "./data/android-18/xml/themes.xml",
            "./data/android-18/xml/themes_device_defaults.xml"
    };

    public static void main(String[] args) {
        ArrayList<Parser> parsers = new ArrayList<Parser>();
        for (int i = 0; i < files.length; i++) {
            DOMParser domParser = new DOMParser();
            InputStream inputStream = domParser.getInputStream(files[i]);

            domParser.onParser(inputStream);
            parsers.add(domParser);
        }

        int count = 0;
        // pre range the styles,one family one list
        Processor processor = new Processor();
        for (int i = 0; i < parsers.size(); i++) {
            ArrayList<Style> styles = ((DOMParser) parsers.get(i)).getStyles();
            count += styles.size();
            for (Style style : styles) {
                processor.findFamily(style);
            }
        }
        //JL.d("-----org.ivonhoe.parser count:"+count);

        processor.findParent();
        processor.printFamilyTree();
    }
}
