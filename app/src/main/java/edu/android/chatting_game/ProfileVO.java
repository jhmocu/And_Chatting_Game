package edu.android.chatting_game;

/**
 * Created by user on 2017-03-08.
 */

public class ProfileVO {
    private String phone;
    private String name;
    private String pic_res;
    private String states_msg;

    public ProfileVO() {}

    public ProfileVO(String phone, String name, String pic_res, String states_msg) {
        this.phone = phone;
        this.name = name;
        this.pic_res = pic_res;
        this.states_msg = states_msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_res() {
        return pic_res;
    }

    public void setPic_res(String pic_res) {
        this.pic_res = pic_res;
    }

    public String getStates_msg() {
        return states_msg;
    }

    public void setStates_msg(String states_msg) {
        this.states_msg = states_msg;
    }
}
