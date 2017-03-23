package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-22.
 */

public class ChatMessageReceiveLab {
    private static ChatMessageReceiveLab instance;
    private ArrayList<ChatMessageReceiveVO> chatMessageList;

    private ChatMessageReceiveLab() {
        chatMessageList = new ArrayList<ChatMessageReceiveVO>();
    }

    public static ChatMessageReceiveLab getInstance() {
        if (instance == null) {
            instance = new ChatMessageReceiveLab();
        }
        return instance;
    }

    public ArrayList<ChatMessageReceiveVO> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(ArrayList<ChatMessageReceiveVO> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }
}
