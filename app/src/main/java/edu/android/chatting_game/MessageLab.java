package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-25.
 */

public class MessageLab {
    private static MessageLab instance;
    private ArrayList<MessageVO> messageList;

    private MessageLab() {
        messageList = new ArrayList<MessageVO>();
    }

    public static MessageLab getInstance() {
        if (instance == null) {
            instance = new MessageLab();
        }
        return instance;
    }

    public ArrayList<MessageVO> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageVO> messageList) {
        this.messageList = messageList;
    }
}
