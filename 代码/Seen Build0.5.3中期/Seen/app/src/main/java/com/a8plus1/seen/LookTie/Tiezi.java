package com.a8plus1.seen.LookTie;


import android.graphics.Bitmap;

class Tiezi {
    private  String content;
    private String nickname;
    private String time;
    private Bitmap headIamge,Image1,Image2,Image3;
    private int type;
    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;

    Tiezi(String content, String nickname, String time, Bitmap headIamge, int type){
        setContent(content);
        setNickname(nickname);
        setHeadIamge(headIamge);
        setTime(time);
        setType(type);
    }
     Tiezi(String content, Bitmap Image1, Bitmap Image2, Bitmap Image3, int type){
        setContent(content);
        setImage1(Image1);
        setImage2(Image2);
        setImage3(Image3);
        setType(type);
    }
    public  String getContent(){return content;}

    public void setContent(String content){this.content=content;}



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Bitmap getHeadIamge() {
        return headIamge;
    }
    public void setHeadIamge(Bitmap headIamge) {
        this.headIamge = headIamge;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Bitmap getImage1() {
        return Image1;
    }
    public void setImage1(Bitmap headIamge) {
        this.Image1 = headIamge;
    }
    public Bitmap getImage2() {
        return Image2;
    }
    public void setImage2(Bitmap headIamge) {
        this.Image2 = headIamge;
    }
    public Bitmap getImage3(){return Image3;}
    public void setImage3(Bitmap headIamge) {
        this.Image3 = headIamge;
    }
}
