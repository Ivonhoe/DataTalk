package com.ivonhoe.parser.android;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.ivonhoe.parser.Parser;
import com.ivonhoe.parser.android.unit.Style;
import com.ivonhoe.parser.android.unit.StyleItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ivonhoe on 15-1-23.
 */
public class DOMParser extends Parser {

    private ArrayList<Style> mStyles;

    public DOMParser() {
        mStyles = new ArrayList<Style>();
    }

    public ArrayList<Style> getStyles() {
        return mStyles;
    }

    public void setStyles(ArrayList<Style> mStyles) {
        this.mStyles = mStyles;
    }

    @Override
    public void onParser(InputStream is) {
        if (is == null)
            return;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();
            NodeList styleList = root.getElementsByTagName("style");

            for (int i = 0; i < styleList.getLength(); i++) {
                Element element = (Element) styleList.item(i);
                String styleName = element.getAttribute("name");
                String parentName = element.getAttribute("parent");

                Style style = new Style(styleName, parentName);
                mStyles.add(style);
                //JL.d("style:" + style);
                //JL.d("name:" + element.getAttribute("name") + ",");

                NodeList itemList = element.getElementsByTagName("item");

                for (int j = 0; j < itemList.getLength(); j++) {
                    Element item = (Element) itemList.item(j);
                    String itemName = item.getAttribute("name");
                    String itemValue = item.getTextContent();

                    StyleItem styleItem = new StyleItem(itemName, itemValue);
                    style.addItem(styleItem);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
