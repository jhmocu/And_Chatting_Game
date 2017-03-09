package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class ChatRoomActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        MessageListFragment messageListFragment = new MessageListFragment();
        SendMessageFragment sendMessageFragment = new SendMessageFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.sendMessageFrame, sendMessageFragment);
        transaction.add(R.id.messageListFrame, messageListFragment);
        transaction.commit();





    }
}
