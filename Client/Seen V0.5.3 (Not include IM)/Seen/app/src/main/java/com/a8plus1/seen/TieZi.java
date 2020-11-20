package com.a8plus1.seen;



public class TieZi {

    //帖子ID
    private String tieZiId;
    //发帖人Id
    private String userID;
    //发帖人昵称
    private String userNickName;
    //标题
    private String title;
    //内容
    private String context;

    //图片
    private String picString;
    //已阅量
    private int watchCount;
    //点赞量
    private int goodCount;
    //发表时间
    private String firstTime;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    private String image1;

    public TieZi(String tieZiId, String userID, String userNickName, String title, String context, int watchCount, int goodCount, String firstTime ) {
        this.tieZiId = tieZiId;
        this.userID = userID;
        this.userNickName = userNickName;
        this.title = title;
        this.context = context;
        this.watchCount = watchCount;
        this.goodCount = goodCount;
        this.firstTime = firstTime;

    }

    public TieZi(String tieZiId, String userID, String userNickName, String title, String context, String picString, int watchCount, int goodCount, String firstTime , String image1) {
        this.tieZiId = tieZiId;
        this.userID = userID;
        this.userNickName = userNickName;
        this.title = title;
        this.context = context;
        this.picString = picString;
        this.watchCount = watchCount;
        this.goodCount = goodCount;
        this.firstTime = firstTime;
        this.image1 = image1;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getTieZiId() {
        return this.tieZiId;
    }

    public void setTieZiId(String tieZiId) {
        this.tieZiId = tieZiId;
    }

    public String getPicString() {
        return picString;
    }

    public void setPicString(String picString) {
        this.picString = picString;
    }

    @Override
    public boolean equals(Object obj) {
        return ((TieZi)obj).getTieZiId().equals( this.getTieZiId());
    }
}
