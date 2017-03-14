package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatMessageLab {
    private static ChatMessageLab instance;
    private ArrayList<ChatMessage> chatMessageList;

    private ChatMessageLab() {
        chatMessageList = new ArrayList<>();
        makeDummyList();
    }
    public static ChatMessageLab getInstance(){
        if (instance == null) {
            instance = new ChatMessageLab();
        }
        return instance;
    }

    public ArrayList<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    private void makeDummyList(){
//        for (int i = 0; i < 30; i++) {
            ChatMessage c1 = new ChatMessage("1. hello");
            chatMessageList.add(c1);
//            ChatMessage c2 = new ChatMessage("2. hello stranger");
//            chatMessageList.add(c2);
//            ChatMessage c3 = new ChatMessage("3. what's your name");
//            chatMessageList.add(c3);

//            ChatMessage chatMessage = new ChatMessage();
//            String msg = "Hello" + i;
//            chatMessage.setMessage(msg);
//            chatMessageList.add(chatMessage);
//        }
    }
}
