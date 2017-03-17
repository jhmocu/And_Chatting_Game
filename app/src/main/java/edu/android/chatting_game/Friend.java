package edu.android.chatting_game;

import android.graphics.Bitmap;

/**
 * Created by stu on 2017-03-06.
 */

public class Friend {
    private static final String TAG = "edu.adroid.chatting";
    private String phone, fName, pic_res, status_msg, relation_code;
    /***/
    private Bitmap pic_bitmap;


    public Friend() {
    }

    public Friend(String phone, String fName, String pic_res, String status_msg, String relation_code) {
        this.phone = phone;
        this.fName = fName;
        this.pic_res = pic_res;
        this.status_msg = status_msg;
        this.relation_code = relation_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPic_res() {

        return pic_res;
    }

    public void setPic_res(String pic_res) {
        this.pic_res = pic_res;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public String getRelation_code() {
        return relation_code;
    }

    public void setRelation_code(String relation_code) {
        this.relation_code = relation_code;
    }

    public Bitmap getPic_bitmap() {
        return pic_bitmap;
    }

    public void setPic_bitmap(Bitmap pic_bitmap) {
        this.pic_bitmap = pic_bitmap;
    }

} // end class Friend
