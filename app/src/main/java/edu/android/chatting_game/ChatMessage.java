package edu.android.chatting_game;

import java.util.ArrayList;

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

static class ChatMessageLab {
    private static ChatMessageLab instance;
    private ArrayList<ChatMessage> list;

    private ChatMessageLab(){
        list = new ArrayList<>();
        makeDummylist();
    }
    public ChatMessageLab getInstance(){
        if (instance == null) {
            instance = new ChatMessageLab();
        }
        return instance;
    }

    public ArrayList<ChatMessage> getList(){
        return list;
    }
    void makeDummylist(){
        ChatMessage chatMessage1 = new ChatMessage("message 1");
        list.add(chatMessage1);

        ChatMessage chatMessage2 = new ChatMessage("message 2");
        list.add(chatMessage2);
    }

}
}
