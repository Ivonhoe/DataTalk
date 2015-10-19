package com.ivonhoe.parser.app.category.shouji360;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.AppInfo;
import com.ivonhoe.parser.engine.Engine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Log;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by ivonhoe on 15-6-3.
 */
public class QHParser extends IParser {

    Hashtable<String, String> gameCategory = new Hashtable<String, String>();

    public QHParser(Engine engine) {
        mEngine = engine;

        gameCategory.put("主题壁纸", "手机美化");
        gameCategory.put("便捷生活", "生活休闲");
        gameCategory.put("儿童亲子", "育儿亲子");
        gameCategory.put("出行导航", "旅游出行");
        gameCategory.put("办公商务", "办公商务");
        gameCategory.put("影音视听", "影音播放");
        gameCategory.put("摄影摄像", "摄影图像");
        gameCategory.put("教育学习", "考试学习");
        gameCategory.put("新闻阅读", "新闻阅读");
        gameCategory.put("系统安全", "系统工具");
        gameCategory.put("购物优惠", "网上购物");
        gameCategory.put("运动健康", "健康运动");
        gameCategory.put("通讯社交", "通讯社交");
        gameCategory.put("金融理财", "金融理财");
    }

    @Override
    public ArrayList<Unit> onParse(String source) {
        try {
            JSONObject data = new JSONObject(source);
            JSONArray infoArray = data.getJSONArray("data");
            String category = data.getString("tag1");
            if (gameCategory.containsKey(category)) {
                category = gameCategory.get(category);
            }
            ArrayList<Unit> infos = new ArrayList<Unit>();

            for (int i = 0; i < infoArray.length(); i++) {
                JSONObject infoItem = (JSONObject) infoArray.get(i);
                String apkId = infoItem.getString("apkid");
                String downloadUrl = infoItem.getString("down_url");
                String apkName = infoItem.getString("name");
                long downloadCount = infoItem.getLong("download_times");
                long weekDownloadCount = infoItem.getLong("week_download_count");

                if (downloadCount >= 20000 && weekDownloadCount >= 100) {
                    AppInfo appInfo = new AppInfo(apkId);
                    appInfo.setSourceName("360");
                    appInfo.setDownloadUrl(downloadUrl);
                    appInfo.setAppName(apkName);
                    appInfo.setDownloadCount(downloadCount);
                    appInfo.setWeekDownloadCount(weekDownloadCount);
                    appInfo.setCategory(category);
                    appInfo.setValue(downloadCount);

                    infos.add(appInfo);
                }
            }
            return infos;
        } catch (JSONException e) {
            return null;
        }
    }
}
