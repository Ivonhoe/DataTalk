package com.ivonhoe.parser.JD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ivonhoe.parser.JD.unit.WebCommentUnit;
import com.ivonhoe.parser.Parser;

import java.io.InputStream;
import java.util.ArrayList;

public class WebCommentParser extends Parser {

    private ArrayList<WebCommentUnit> mWebComments = new ArrayList<WebCommentUnit>();

    private int mCommentsCount = 0;
    private long startTime = -1;
    private long endTime = -1;
    private boolean isParsing = false;
    private boolean hasMistake = false;

    public void WebCommentsParser() {
    }

    public void onParser(String source) {
        // TODO Auto-generated method stub
        try {
            String jsonString = SimpleCommentParserFactory.getJSONString(source);
            if (jsonString == null) {
                hasMistake = true;
                return;
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("comments")) {
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                getWebCommentUnit(jsonArray);
            } else {
                hasMistake = true;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ArrayList<WebCommentUnit> getWebCommentUnit(JSONArray jsonArray) throws JSONException {
        mWebComments.clear();
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

    @Override
    public void onParser(InputStream inputStream) {

    }
}
