package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-06.
 */

public class Friend {
    private static final String TAG = "edu.adroid.chatting";
    private String phone, fName, pic_res, status_msg, relation_code, pic_url;

    public Friend() {
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

    public String getPic_url() {
        pic_url = "http://192.168.11.11:8081/Test3/uploadDirectory/" + getPic_res();
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

} // end class Friend
