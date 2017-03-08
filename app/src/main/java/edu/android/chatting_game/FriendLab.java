package edu.android.chatting_game;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-06.
 */

public class FriendLab {
    private static final int[] IDS = {
            R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9,
    };
    private ArrayList<Friend> friendList;
    private static FriendLab instance;

    private FriendLab() {
        friendList = new ArrayList<Friend>();
        makeDummyList();
    }
    public static FriendLab getInstance() {
        if (instance == null) {
            instance = new FriendLab();
        }
        return instance;
    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    private void makeDummyList() {
        for (int i = 0; i < 100; i++) {
            Friend friends = new Friend();
            friends.setName("Name " + i);
            friends.setPhoneNumber(i + "-" + i);
            friends.setMessage("it's me, Name" + i + "!");
            friends.setImageId(IDS[i % IDS.length]);
            friendList.add(friends);
        }
    }
}
