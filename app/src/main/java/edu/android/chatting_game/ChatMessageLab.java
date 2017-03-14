package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatMessageLab {
    private static ChatMessageLab instance;
    private ArrayList<ChatMessageVO> chatMessageVOList;

    private ChatMessageLab() {
        chatMessageVOList = new ArrayList<>();
        makeDummyList();
    }
    public static ChatMessageLab getInstance(){
        if (instance == null) {
            instance = new ChatMessageLab();
        }
        return instance;
    }

    public ArrayList<ChatMessageVO> getChatMessageVOList() {
        return chatMessageVOList;
    }

    private void makeDummyList(){
//        for (int i = 0; i < 30; i++) {
            ChatMessageVO c1 = new ChatMessageVO("1. hello");
            chatMessageVOList.add(c1);
//            ChatMessageVO c2 = new ChatMessageVO("2. hello stranger");
//            chatMessageVOList.add(c2);
//            ChatMessageVO c3 = new ChatMessageVO("3. what's your name");
//            chatMessageVOList.add(c3);

//            ChatMessageVO chatMessage = new ChatMessageVO();
//            String msg = "Hello" + i;
//            chatMessage.setMessage(msg);
//            chatMessageVOList.add(chatMessage);
//        }
    }
}
