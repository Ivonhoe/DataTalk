package com.ivonhoe.parser.android;

import com.ivonhoe.parser.io.ExlWriter;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.android.unit.Style;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivonhoe on 15-1-25.
 */
public class Processor {
    ArrayList<ArrayList<Style>> preRange;

    Processor() {
        preRange = new ArrayList<ArrayList<Style>>();
    }


    /*
    * 将相同家族的style放在一个list中去
    * */
    public void findFamily(Style style) {
        for (int i = 0; i < preRange.size(); i++) {
            ArrayList<Style> familyList = preRange.get(i);
            Style family = familyList.get(0);
            boolean isFamily = style.isFamily(family);
            if (isFamily) {
                familyList.add(style);
                return;
            }
        }

        ArrayList<Style> newFamily = new ArrayList<Style>();
        newFamily.add(style);
        preRange.add(newFamily);
    }

    /*
    * 在家族中为每一个style找到它的父节点
    * */
    public void findParent() {
        for (int i = 0; i < preRange.size(); i++) {

            ArrayList<Style> family = preRange.get(i);
            for (Style style : family) {
                findParent(style, family);
            }
        }
    }

    public void findParent(Style child, List<Style> family) {
        if (child.getParentName().length() < 1) {
            return;
        }
        for (Style parent : family) {
            if (child.isParent(parent)) {
                child.addParent(parent);
                return;
            }
        }
    }

    /*
    * 遍历家族树，并将字节点按照层序遍历
    * */
    public void printFamilyTree() {
        int count = 0;
        ExlWriter exlWriter = new StyleWriter((new File("./data/THEME.xls")));
        for (int i = 0; i < preRange.size(); i++) {
            ArrayList<Style> familyList = preRange.get(i);

            for (Style style : familyList) {
                boolean isAncestor = style.getParent() == null ? true : false;
                if (isAncestor) {
                    List<Unit> result = style.levelOrderChildren();
                    exlWriter.onWrite(result);
                    count += result.size();
                }
            }
        }
        exlWriter.close();
    }
}
