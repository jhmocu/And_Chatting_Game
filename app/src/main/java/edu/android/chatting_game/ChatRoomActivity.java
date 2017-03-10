package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class ChatRoomActivity
        extends AppCompatActivity {
    
    private String title;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.search: break;
            case R.id.chat_room_option1: break;
            case R.id.chat_room_option2: break;
        } //end switch
        return true;
    }

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

        ActionBar actionBar = getSupportActionBar();
        // TODO: 2017-03-10 title: 대화상대로 set 하는 public 메소드 만들기
//        actionBar.hide();
        title = "대화방 이름";
        actionBar.setTitle(title);

    }
}
