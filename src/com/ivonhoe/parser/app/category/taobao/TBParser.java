package com.ivonhoe.parser.app.category.taobao;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.AppInfo;
import com.ivonhoe.parser.engine.Engine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author ivonhoe on 15-5-29.
 */
public class TBParser extends IParser {

    Hashtable<String, String> gameCategory = new Hashtable<String, String>();

    public TBParser(Engine engine) {
        mEngine = engine;

        gameCategory.put("休闲益智", "我的游戏");
        gameCategory.put("网络游戏", "我的游戏");
        gameCategory.put("跑酷竞速", "我的游戏");
        gameCategory.put("动作冒险", "我的游戏");
        gameCategory.put("飞行射击", "我的游戏");
        gameCategory.put("扑克棋牌", "我的游戏");
        gameCategory.put("经营策略", "我的游戏");
        gameCategory.put("角色扮演", "我的游戏");
        gameCategory.put("体育竞技", "我的游戏");
        gameCategory.put("辅助工具", "我的游戏");
    }

    @Override
    public ArrayList<Unit> onParse(String source) {
        try {
            ArrayList<Unit> result = new ArrayList<Unit>();
            JSONObject jsonObject = new JSONObject(source);
            JSONObject data = (JSONObject) jsonObject.get("data");

            JSONArray content = data.getJSONArray("content");

            for (int i = 0; i < content.length(); i++) {
                JSONObject infoItem = (JSONObject) content.get(i);
                String packageName = infoItem.getString("packageName");
                String appName = infoItem.getString("name");

                long categoryId = infoItem.getLong("categoryId");
                String category = infoItem.getString("categoryName");
                if (gameCategory.containsKey(category)) {
                    category = gameCategory.get(category);
                }

                String downloadUrl = infoItem.getString("downloadUrl");
                long monthDownload = infoItem.getLong("downloads");

                if (monthDownload > 10000) {
                    AppInfo tbAppInfo = new AppInfo(packageName);
                    tbAppInfo.setSourceName("taobao");
                    tbAppInfo.setAppName(appName);
                    tbAppInfo.setCategoryId(categoryId);
                    tbAppInfo.setDownloadUrl(downloadUrl);
                    tbAppInfo.setMonthDownloadCount(monthDownload);
                    tbAppInfo.setCategory(category);
                    tbAppInfo.setValue(monthDownload);

                    result.add(tbAppInfo);
                }
            }
            return result;
        } catch (JSONException e) {
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
