package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-23.
 */

public class FontChangeVO {
    private float textSize;

    public FontChangeVO(){}

    public FontChangeVO(float textSize) {
        this.textSize = textSize;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }


    private static FontChangeVO instance = null;

    public static synchronized FontChangeVO getInstance(){
        if(null == instance){
            instance = new FontChangeVO();
        }
        return instance;
    }
}
