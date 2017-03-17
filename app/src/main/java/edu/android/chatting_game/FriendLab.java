package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-06.
 */
public class FriendLab {

    private static final String TAG = "edu.android.chatting";
    private ArrayList<Friend> friendList;
    private static FriendLab instance;

    private FriendLab() {
        friendList = new ArrayList<Friend>();
    }

    public static FriendLab getInstance() {
        if (instance == null) {
            instance = new FriendLab();
        }
        return instance;
    }

    public void setFriendList(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }


}// end class FriendLab



