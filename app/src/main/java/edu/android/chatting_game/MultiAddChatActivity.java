package edu.android.chatting_game;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MultiAddChatActivity extends AppCompatActivity
        implements MultiAddChatRecyclerViewFragment.MultiAddChatSendProfile{

    private static final String TAG = "edu.android.chatting";
    private Button btnBack, btnMultiChat;
    private TextView textCount;
    private CheckBox multiChatBox;

    private int count;
    private int position;
    private String name, phone;

    private ArrayList<Boolean> selectedList;
    private ArrayList<Friend> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_add_chat);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frameLayout);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnMultiChat = (Button) findViewById(R.id.addChatBtn);
        textCount = (TextView) findViewById(R.id.textCount);
        multiChatBox = (CheckBox) findViewById(R.id.checkBoxMultiAddChat);

        textCount.setText(String.valueOf(count));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(fragment == null){
            fragment = new MultiAddChatFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.frameLayout, fragment);
            transaction.commit();
        }

        //TODO: 다중채팅으로 넘어가기
        btnMultiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiAddChatActivity.this, ChatRoomActivity.class);

//                Toast.makeText(MultiAddChatActivity.this, "name = " + name, Toast.LENGTH_SHORT).show();
                Toast.makeText(MultiAddChatActivity.this, "phone = " + phone, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    @Override
    public void multichatsendprofile(String name, String phone, int position, int count, ArrayList<Boolean> selectedList) {
        textCount.setText(String.valueOf(count));
        this.selectedList = selectedList;
        this.position = position;
        this.name = name;
        this.phone = phone;
    }
}
