package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-13.
 */

public class ChatRoomLab {
    private static ChatRoomLab instance;
    private ArrayList<ChatRoomVO> chatRoomVOList;

    private ChatRoomLab() {
        chatRoomVOList = new ArrayList<ChatRoomVO>();
    }
    public static ChatRoomLab getInstance(){
        if (instance == null) {
            instance = new ChatRoomLab();
        }
        return instance;
    }

    public ArrayList<ChatRoomVO> getChatRoomVOList() {
        return chatRoomVOList;
    }

    public void setChatRoomVOList(ArrayList<ChatRoomVO> chatRoomVOList) {
        this.chatRoomVOList = chatRoomVOList;
    }
}
