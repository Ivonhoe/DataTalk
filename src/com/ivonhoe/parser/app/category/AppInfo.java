package com.ivonhoe.parser.app.category;

import com.ivonhoe.parser.Unit;
import util.sort.ElemType;

import java.util.ArrayList;
import java.util.List;

/**
 * @ivonhoe on 15-6-9.
 */
public class AppInfo implements Unit, ElemType {

    private String mPackageName;

    private String mDownloadUrl;

    private String mAppName;

    private long mDownloadCount;

    private long mWeekDownloadCount;

    private long mMonthDownloadCount;

    private String mCategory;

    private long mCategoryId;

    private int mRanking;

    private long mValue;

    private List<String> mAppTags;

    private String mSourceName;

    public AppInfo(String packageName) {
        mPackageName = packageName;
        mAppTags = new ArrayList<String>();
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public long getDownloadCount() {
        return mDownloadCount;
    }

    public void setDownloadCount(long mDownloadCount) {
        this.mDownloadCount = mDownloadCount;
    }

    public long getWeekDownloadCount() {
        return mWeekDownloadCount;
    }

    public void setWeekDownloadCount(long mWeekDownloadCount) {
        this.mWeekDownloadCount = mWeekDownloadCount;
    }

    public long getMonthDownloadCount() {
        return mMonthDownloadCount;
    }

    public void setMonthDownloadCount(long mMonthDownloadCount) {
        this.mMonthDownloadCount = mMonthDownloadCount;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public int getRanking() {
        return mRanking;
    }

    public void setRanking(int ranking) {
        this.mRanking = ranking;
    }

    public boolean addTag(String tag) {
        return mAppTags.add(tag);
    }

    public List<String> getAppTags() {
        return mAppTags;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public void setSourceName(String sourceName) {
        this.mSourceName = sourceName;
    }

    @Override
    public long getValue() {
        return mValue;
    }

    @Override
    public void setValue(long value) {
        mValue = value;
    }

}
