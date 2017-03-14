package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatMessageVO {
    private String message;

    public ChatMessageVO(){}

    public ChatMessageVO(String messge) {
        this.message = messge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
