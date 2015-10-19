package com.ivonhoe.parser.app.category.lewa;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.AppInfo;
import com.ivonhoe.parser.engine.Engine;

import java.util.ArrayList;

/**
 * @ivonhoe on 15-6-9.
 */
public class LewaParser extends IParser {

    int lineCount = 0;

    public LewaParser(Engine engine) {
        this.mEngine = engine;
        mCore = 1;
    }

    @Override
    public ArrayList<Unit> onParse(String source) {
        // 因为是加入线程池队列里执行，所以读取的顺序和文件中的顺序是相反的
        ArrayList<Unit> list = new ArrayList<Unit>();
        AppInfo info = new AppInfo(source);
        info.setSourceName("lewa");
        info.setValue(++lineCount);
        list.add(info);
        return list;
    }
}
