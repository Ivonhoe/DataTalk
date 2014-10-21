import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebCommentParser extends CommentParser {

    private ArrayList<WebCommentUnit> mWebComments = new ArrayList<WebCommentUnit>();

    private int mCommentsCount = 0;
    private long startTime = -1;
    private long endTime = -1;
    private boolean isParsing = false;
    private boolean hasMistake = false;
    private CommentWriter mCommentWriter;

    public void WebCommentsParser() {
    }

    @Override
    public void onStart(CommentWriter writer) {
        // TODO Auto-generated method stub
        System.out.println("WebCommentParser start! ");
        isParsing = true;
        hasMistake = false;
        startTime = System.currentTimeMillis();
        mCommentWriter = writer;
    }

    @Override
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
    public void onSave() {
        // TODO Auto-generated method stub
        if (hasMistake) {
            Log.d("hasMistake,return");
            return;
        }
        mCommentWriter.write(mWebComments);
    }

    @Override
    public void onEnd() {
        // TODO Auto-generated method stub
        endTime = System.currentTimeMillis();
        System.out.println("WebCommentParser end! Use time: " + (endTime - startTime) + "ms."
                + "mCommentsCount: " + mCommentsCount);
        isParsing = false;
        if(mCommentWriter instanceof ExlWriter){
            mCommentWriter.close();
        }
        mCommentWriter = null;
    }

}
