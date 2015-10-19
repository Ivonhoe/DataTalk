package com.ivonhoe.parser.JD;

import com.ivonhoe.parser.IParser;
import com.ivonhoe.parser.JD.unit.WebCommentUnit;
import com.ivonhoe.parser.Unit;
import com.ivonhoe.parser.app.category.custom.CustomParser;
import com.ivonhoe.parser.engine.Engine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Log;

import java.util.ArrayList;

/**
 * @author ivonhoe
 */
public class JDWebParser extends IParser {

    private int mCommentsCount = 0;

    public JDWebParser(Engine engine) {
        mEngine = engine;
    }

    @Override
    public ArrayList<Unit> onParse(String source) {
        //Log.d("parse source:" + source);
        if (!source.startsWith("jsonp") /*|| !source.startsWith("fetchJSON")*/) {
            return null;
        }
        ArrayList<Unit> parseResult = null;
        String jsonString = null;
        try {
            jsonString = getJSONString(source);
            if (jsonString == null) {
                return parseResult;
            }
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("comments")) {
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                parseResult = getWebCommentUnit(jsonArray);
            } else {
                return parseResult;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return parseResult;
    }

    private ArrayList<Unit> getWebCommentUnit(JSONArray jsonArray) throws JSONException {
        ArrayList<Unit> mWebComments = new ArrayList<Unit>();

        for (int i = 0; i < jsonArray.length(); i++) {
            WebCommentUnit commentUnit = new WebCommentUnit();
            JSONObject object = jsonArray.getJSONObject(i);
            commentUnit.setNickname(object.getString("nickname"));
            commentUnit.setContent(object.getString("content"));
            commentUnit.setCreationTime(object.getString("creationTime"));
            commentUnit.setScore(object.getInt("score"));
            commentUnit.setReferenceName(object.getString("referenceName"));
            commentUnit.setUsefulVoteCount(object.getInt("usefulVoteCount"));
            commentUnit.setUselessVoteCount(object.getInt("uselessVoteCount"));
            if (object.has("productColor")) {
                commentUnit.setProductColor(object.getString("productColor"));
            }
            if (object.has("userProvince")) {
                commentUnit.setUserProvince(object.getString("userProvince"));
            }
            if (object.has("title")) {
                commentUnit.setTitle(object.getString("title"));
            }
            if (object.has("commentTags")) {
                JSONArray array = object.getJSONArray("commentTags");
                commentUnit.setCommentTags(getCommentTags(array));
            }
            if (object.has("userLevelName")) {
                commentUnit.setUserLevelName(object.getString("userLevelName"));
            }
            mWebComments.add(commentUnit);
            mCommentsCount++;
        }
        return mWebComments;
    }

    public static String getJSONString(String source) {
        if (source == null) {
            return null;
        }
        int index = source.indexOf("(");
        if (index >= 0) {
            return source.substring(index + 1, source.length() - 1);
        }
        return null;
    }

    private String[] getCommentTags(JSONArray jsonArray) throws JSONException {
        if (jsonArray.length() < 1) {
            return null;
        }
        String[] tags = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            tags[i] = jsonObject.getString("name");
        }
        return tags;
    }
}
