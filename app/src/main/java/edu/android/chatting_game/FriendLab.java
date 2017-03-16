package edu.android.chatting_game;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-06.
 */
public class FriendLab {
    //    private static final int[] IDS = {
//            R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9,
//    };
    private static final String TAG = "edu.android.chatting";
    private ArrayList<Friend> friendList;
    private static FriendLab instance;

    private FriendLab() {
        friendList = new ArrayList<Friend>();
        Log.i(TAG, "FriendLab() 생성자 호출");
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



