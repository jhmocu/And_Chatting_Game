package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class AddChatActivity extends AppCompatActivity {
    private TextView textAddChatName;
    private Button btnAddChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.addChatFrame);
        textAddChatName = (TextView) findViewById(R.id.textAddChatName);
        btnAddChat = (Button) findViewById(R.id.btnAddChat);
    } // end onCreate()
} // end class AddChatActivity
