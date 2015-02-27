package com.ivonhoe.parser.android.unit;

import com.ivonhoe.parser.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ivonhoe on 15-1-23.
 */
public class Style implements Unit {

    private String name;

    private String parentName;

    private Style parent;

    private ArrayList<Style> children;

    private ArrayList<StyleItem> items;

    public Style(String name, String parent) {
        this.name = name;
        this.parentName = parent;
        this.children = new ArrayList<Style>();
        this.items = new ArrayList<StyleItem>();

        int index = parentName.length() > 0 ? -1 : name.lastIndexOf(".");
        if (index > 0) {
            parentName = name.substring(0, index);
        }

        if (parentName != null && parentName.startsWith("@")) {
            this.parentName = parentName.substring(parentName.indexOf("/") + 1);
        } else if (parentName != null && parentName.startsWith("android:")) {
            this.parentName = parentName.substring(parentName.indexOf(":") + 1);
        }
    }

    public String getParentName() {
        return this.parentName;
    }

    public Style getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Style parent) {
        this.parent = parent;
    }

    public void addItem(StyleItem item) {
        items.add(item);
    }

    public ArrayList<StyleItem> getItems() {
        return items;
    }

    public ArrayList<Style> getChildren() {
        return children;
    }

    public void addChild(Style child) {
        children.add(child);
    }

    public String getHead() {
        String string = parentName.length() > 0 ? parentName : name;
        int index = string.indexOf(".");
        if (index > 0) {
            return string.substring(0, index);
        }

        return string;
    }

    public boolean isFamily(Style style) {
        String head1 = this.getHead();
        String head2 = style.getHead();
        //JL.d("head1:" + head1 + ",head2:" + head2);
        return head1.equals(head2);
    }

    // if style is parent return true, else return false;
    public boolean isParent(Style style) {
        if (parentName.equals(style.getName())) {
            return true;
        }
        return false;
    }

    public void addParent(Style parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    /*
    * 层序遍历当前节点及其子节点
    * */
    public List<Unit> levelOrderChildren() {
        Queue queue = new LinkedList();
        List<Unit> result = new ArrayList<Unit>();
        queue.offer(this);
        while (!queue.isEmpty()) {
            Style p = (Style) queue.poll();
            result.add(p);
            // JL.d("Style:" + p.toString());
            for (Style style : p.children) {
                queue.offer(style);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "[name:" + name + ",parentName:" + (parentName != null ? parentName : "null") + "]";
    }
}
