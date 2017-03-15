package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-15.
 */

public class FriendVO {
    private String my_phone;
    private String friend_phone;
    private String friend_name;

    public FriendVO(){}

    public FriendVO(String my_phone, String friend_phone, String friend_name) {
        this.my_phone = my_phone;
        this.friend_phone = friend_phone;
        this.friend_name = friend_name;
    }

    public String getMy_phone() {
        return my_phone;
    }

    public void setMy_phone(String my_phone) {
        this.my_phone = my_phone;
    }

    public String getFriend_phone() {
        return friend_phone;
    }

    public void setFriend_phone(String friend_phone) {
        this.friend_phone = friend_phone;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }
}
