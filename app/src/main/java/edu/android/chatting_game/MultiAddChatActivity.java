package edu.android.chatting_game;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private String name;
    private String phone;

        private ArrayList<Boolean> selectedList;
        private ArrayList<Integer> positions = new ArrayList<>();
        private ArrayList<String> phones = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

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

//        phone = list.get(position).getFriend_phone();


        //TODO: 다중채팅으로 넘어가기 // ArrayList로 번호만 넘기기
        btnMultiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiAddChatActivity.this, ChatRoomActivity.class);
//                intent.putExtra("otherPhones", phone);
                for (int p : positions){
                    Log.i(TAG, " size: " + positions.size());
                    Friend vo = FriendLab.getInstance().getFriendList().get(p);
                    phones.add(vo.getPhone());
                }
                intent.putExtra("otherPhones", phones);
                Toast.makeText(MultiAddChatActivity.this, "phones = " + phones, Toast.LENGTH_LONG).show();
                Log.i(TAG, "ㅋㅇㅌ:" + count + "////phones:" + phones + "position:" + positions);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void multichatsendprofile(String name, String phone, int count, ArrayList<Boolean> selectedList, ArrayList<Integer> positions) {
        textCount.setText(String.valueOf(count));
        this.selectedList = selectedList;
        this.count = count;
        this.positions = positions;
        this.name = name;
//        this.phone = phone;
    }
}
