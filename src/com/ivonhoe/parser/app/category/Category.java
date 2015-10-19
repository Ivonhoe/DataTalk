package com.ivonhoe.parser.app.category;

import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.custom.CustomParser;
import com.ivonhoe.parser.app.category.lewa.LewaParser;
import com.ivonhoe.parser.app.category.shouji360.QHParser;
import com.ivonhoe.parser.app.category.taobao.TBParser;
import com.ivonhoe.parser.app.category.taobao.TBTextWriter;
import com.ivonhoe.parser.engine.Engine;
import com.ivonhoe.parser.huffman.HuffmanCode;
import util.Log;
import util.sort.ElemType;
import util.sort.InsertSort;

import java.util.*;

/**
 * @author ivonhoe on 15-5-29.
 */
public class Category {

    public static void main(String[] args) {
        Category category = new Category();
        category.run();

        HuffmanCode huffmanCode = new HuffmanCode();
        HashMap<Character, byte[]> charCodeMap = huffmanCode.loadFile("data/app/category");
        if (charCodeMap == null) {
            return;
        }
        huffmanCode.setTargetFile("data/app/huffman");
        huffmanCode.encode(charCodeMap);

        HashMap<String, Character> characterHashMap = new HashMap<String, Character>();
        for (Map.Entry<Character, byte[]> entry : charCodeMap.entrySet()) {
            String key = HuffmanCode.getString(entry.getValue());
            Character character = entry.getKey();
            characterHashMap.put(key, character);
        }
        huffmanCode.decode(characterHashMap);
    }

    /**
     * 处理淘宝应用分类数据
     */
    Engine tbEngine;
    /**
     * 处理奇虎360应用分类数据
     */
    Engine qhEngine;
    /**
     * 处理Lewa的提供应用使用频率数据
     */
    Engine lewaEngine;
    /**
     * 处理淘宝游戏分类数据
     */
    Engine gameEngine;
    /**
     * 处理自己添加的应用分类信息
     */
    Engine customEngine;

    private void setupEngines() {
        tbEngine = new Engine();
        tbEngine.setSourcePath("data/app/taobao");
        tbEngine.setParser(new TBParser(tbEngine));
        //TBTextWriter writer = new TBTextWriter("data/app/TaobaoCategory");
        //engine.setWriter(writer);

        qhEngine = new Engine();
        qhEngine.setSourcePath("data/app/360");
        qhEngine.setParser(new QHParser(qhEngine));
        //QHTextWriter writer1 = new QHTextWriter("data/app/360Category");
        //qhEngine.setWriter(writer1);

        // no writer and no parser, just reader
        lewaEngine = new Engine();
        lewaEngine.setSourcePath("data/app/lewa");
        lewaEngine.setParser(new LewaParser(lewaEngine));

        gameEngine = new Engine();
        gameEngine.setSourcePath("data/app/game");
        gameEngine.setParser(new TBParser(gameEngine));

        // read and parse "/data/app/custom/custom", app sort by myself
        customEngine = new Engine();
        customEngine.setSourcePath("data/app/custom");
        customEngine.setParser(new CustomParser(customEngine));
    }

    private void startEngines() {
        tbEngine.start();
        qhEngine.start();
        lewaEngine.start();
        gameEngine.start();
        customEngine.start();
    }

    private List<AppInfo> getTopApp(List<Unit> list, int topSize) {
        List<AppInfo> tbAppList = sort(list);
        return getTopList(tbAppList, topSize);
    }

    private HashMap<String, AppInfo> getUnion(List<List<AppInfo>> appLists) {
        // 计算以上4个集合的并集
        HashMap<String, AppInfo> unionMap = new HashMap<String, AppInfo>();
        for (List<AppInfo> list : appLists) {
            for (AppInfo info : list) {
                if (!unionMap.containsKey(info.getPackageName())) {
                    unionMap.put(info.getPackageName(), info);
                }
            }
        }

        return unionMap;
    }

    private void extraUnion(HashMap<String, AppInfo> union, List<AppInfo> list) {
        for (AppInfo info : list) {
            if (!union.containsKey(info.getPackageName())) {
                union.put(info.getPackageName(), info);
            }
        }
    }

    private void run() {
        setupEngines();
        startEngines();

        synchronized (Engine.mCounter) {
            while (Engine.Counter.get() < 5) {
                try {
                    Engine.mCounter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Engine.mCounter.notifyAll();
        }

        List<AppInfo> tbAppList = getTopApp(tbEngine.getParseResult(), 5000);
        List<AppInfo> qhAppList = getTopApp(qhEngine.getParseResult(), 5000);
        List<AppInfo> lewaAppList = getTopApp(lewaEngine.getParseResult(), 3000);
        List<AppInfo> gameAppList = getTopApp(gameEngine.getParseResult(), 5000);
        List<AppInfo> customAppList = getTopApp(customEngine.getParseResult(),
                customEngine.getParseResult().size());

        if (tbAppList == null || qhAppList == null || lewaAppList == null || gameAppList == null ||
                customAppList == null) {
            return;
        }
        Log.e("TB app:" + tbAppList.size() + ",QH app:" + qhAppList.size() + ",LEWA app:" +
                lewaAppList.size() + ", game app:" + gameAppList.size());

        // 三个集合的交集
        List<AppInfo> common1 = getCommon(tbAppList, qhAppList);
        List<AppInfo> common2 = getCommon(tbAppList, lewaAppList);
        List<AppInfo> common3 = getCommon(qhAppList, lewaAppList);
        // 计算游戏集合和Lewa的交集
        List<AppInfo> common4 = getCommon(gameAppList, lewaAppList);

        List<List<AppInfo>> commonLists = new ArrayList<List<AppInfo>>();
        commonLists.add(common1);
        commonLists.add(common2);
        commonLists.add(common3);
        commonLists.add(common4);

        // 获得以上集合的并集
        HashMap<String, AppInfo> union = getUnion(commonLists);

        // 添加淘宝的top3000
        List<AppInfo> top3000 = getTopList(tbAppList, 3000);
        extraUnion(union, top3000);
        // 添加游戏的top1000
        List<AppInfo> top1000 = getTopList(gameAppList, 1000);
        extraUnion(union, top1000);

        extraUnion(union, customAppList);

        Log.d("common1:" + common1.size() + ",common2:" + common2.size() + ",common3:" +
                common3.size() + ", commonMap:" + union.size());

        List<Unit> result = new ArrayList<Unit>();
        Iterator iterator = union.keySet().iterator();
        while (iterator.hasNext()) {
            result.add(union.get(iterator.next()));
        }

        CategoryWriter categoryWriter = new CategoryWriter("data/app/category");
        categoryWriter.onWrite(result);
    }

    /**
     * 获得两个集合的交集,把list1的值放入并集中
     */
    private List<AppInfo> getCommon(List<AppInfo> list1, List<AppInfo> list2) {
        ArrayList<AppInfo> common = new ArrayList<AppInfo>();
        for (AppInfo appInfo : list2) {
            String key = appInfo.getPackageName();
            for (AppInfo appInfo1 : list1) {
                if (key != null && key.equals(appInfo1.getPackageName())) {
                    common.add(appInfo1);
                    break;
                }
            }
        }
        return common;
    }

    private List<AppInfo> sort(List<Unit> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Unit unit = list.get(0);
        if (unit instanceof ElemType) {
            ElemType[] array = new ElemType[list.size()];
            int index = 0;
            for (Unit unit1 : list) {
                array[index] = (ElemType) list.get(index);
                index++;
            }

            long time = System.currentTimeMillis();
            // 直接插入排序
            //InsertSort.binaryInsert(array);

            // 希尔排序
            InsertSort.shellSort(array);

            // 冒泡排序
            //SwapSort.bubbleSort(array);

            // 快速排序
            //SwapSort.QuickSort(array);

            ArrayList<AppInfo> result = new ArrayList<AppInfo>();
            for (int i = array.length - 1; i >= 0; i--) {
                result.add((AppInfo) array[i]);
                ((AppInfo) array[i]).setRanking(array.length - i);
            }
            Log.e("排序结束，耗时：" + (System.currentTimeMillis() - time) + ", array大小：" + array.length);
            return result;
        }
        return null;
    }

    private List<AppInfo> getTopList(List<AppInfo> list, int topSize) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        topSize = Math.min(list.size(), topSize);
        List<AppInfo> result = new ArrayList<AppInfo>();
        for (int i = 0; i < topSize; i++) {
            result.add(list.get(i));
        }

        return result;
    }
}
