package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatMessage {
    private String message;

    public ChatMessage(){}

    public ChatMessage(String messge) {
        this.message = messge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
