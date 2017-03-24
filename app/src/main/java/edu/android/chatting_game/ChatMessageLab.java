package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatMessageLab {
    private static ChatMessageLab instance;
    private ArrayList<ChatMessageVO> chatMessageVOList;

    private ChatMessageLab() {
        chatMessageVOList = new ArrayList<ChatMessageVO>();
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

    public  ArrayList<ChatMessageVO> setChatMessageVOList(ArrayList<ChatMessageVO> chatMessageVOList) {
        this.chatMessageVOList = chatMessageVOList;
        return chatMessageVOList;
    }
}
