package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-23.
 */

public class BackgroundChangeVO {
    private int color;

    public BackgroundChangeVO(){}

    public BackgroundChangeVO(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private static BackgroundChangeVO instance = null;

    public static synchronized BackgroundChangeVO getInstance(){
        if(null == instance){
            instance = new BackgroundChangeVO();
        }
        return instance;
    }
}
