package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatRoomVO {
    private String phone, chatroom_name, last_msg, chat_date, checked_read, member_count;

    public ChatRoomVO(){}

    public ChatRoomVO(String phone, String chatroom_name) {
        this.phone = phone;
        this.chatroom_name = chatroom_name;
    }

    public ChatRoomVO(String phone, String chatroom_name, String last_msg, String chat_date, String checked_read, String member_count) {
        this.phone = phone;
        this.chatroom_name = chatroom_name;
        this.last_msg = last_msg;
        this.chat_date = chat_date;
        this.checked_read = checked_read;
        this.member_count = member_count;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChatroom_name() {
        return chatroom_name;
    }

    public void setChatroom_name(String chatroom_name) {
        this.chatroom_name = chatroom_name;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getChat_date() {
        return chat_date;
    }

    public void setChat_date(String chat_date) {
        this.chat_date = chat_date;
    }

    public String getChecked_read() {
        return checked_read;
    }

    public void setChecked_read(String checked_read) {
        this.checked_read = checked_read;
    }

    public String getMember_count() {
        return member_count;
    }

    public void setMember_count(String member_count) {
        this.member_count = member_count;
    }

    @Override
    public String toString() {
        String str = "phone:" + phone + "|chatroom_name:" + chatroom_name + "|last_msg:" + last_msg;

        return str;
    }
}
