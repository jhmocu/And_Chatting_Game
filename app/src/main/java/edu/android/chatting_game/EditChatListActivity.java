package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class EditChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chat_list);
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.frameEditChatList);
        if (frag == null) {
            frag = new EditChatListFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.frameEditChatList, frag);
            transaction.commit();
        }

    }
}
