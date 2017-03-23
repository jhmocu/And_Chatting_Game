package edu.android.chatting_game;

/**
 * Created by user on 2017-03-21.
 */

public class ChatMessageReceiveVO {
    private String my_phone;
    private String chat_member;
    private String checked;
    private String chatroom_name;
    private String msg;
    private String chat_date;

    public ChatMessageReceiveVO() {

    }

    public ChatMessageReceiveVO(String my_phone, String chat_member, String checked, String chatroom_name, String msg, String chat_date) {
        this.my_phone = my_phone;
        this.chat_member = chat_member;
        this.checked = checked;
        this.chatroom_name = chatroom_name;
        this.msg = msg;
        this.chat_date = chat_date;
    }

    public String getMy_phone() {
        return my_phone;
    }

    public void setMy_phone(String my_phone) {
        this.my_phone = my_phone;
    }

    public String getChat_member() {
        return chat_member;
    }

    public void setChat_member(String chat_member) {
        this.chat_member = chat_member;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getChatroom_name() {
        return chatroom_name;
    }

    public void setChatroom_name(String chatroom_name) {
        this.chatroom_name = chatroom_name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getChat_date() {
        return chat_date;
    }

    public void setChat_date(String chat_date) {
        this.chat_date = chat_date;
    }

    /**Log 확인용*/
    @Override
    public String toString() {
        String str = "my_phone: " + my_phone + "|chat_member: " + chat_member + "|checked: " + checked +
                "|chatroom_name: " + chatroom_name + "|msg: " + msg + "|chat_date: " + chat_date;
        return str;
    }
}
