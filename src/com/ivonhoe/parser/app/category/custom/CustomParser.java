package com.ivonhoe.parser.app.category.custom;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.AppInfo;
import com.ivonhoe.parser.engine.Engine;

import java.util.ArrayList;

/**
 * @author ivonhoe on 15-6-10.
 */
public class CustomParser extends IParser {

    public CustomParser(Engine engine) {
        this.mEngine = engine;
        mCore = 1;
    }

    @Override
    public ArrayList<Unit> onParse(String source) {
        ArrayList<Unit> mAppList = new ArrayList<Unit>();
        String[] splits = source.split(",");
        if (splits.length == 2) {
            AppInfo info = new AppInfo(splits[0]);
            info.setCategory(splits[1]);
            info.setValue(0);
            mAppList.add(info);
        }
        return mAppList;
    }
}
