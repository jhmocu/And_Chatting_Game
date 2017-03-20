package edu.android.chatting_game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.sephiroth.android.library.picasso.Picasso;

public class ProfileInfoActivity
        extends AppCompatActivity {
    private static final String TAG = "edu.android.chatting";

    private ImageView imageView;
    private TextView textName, textPhone, textMsg;
    private ImageButton btnCall, btnMessage; // 수정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        Log.i(TAG, "ProfileInfoActivity/onCreate()");

        imageView = (ImageView) findViewById(R.id.imageView_ProfileInfo);
        textName = (TextView) findViewById(R.id.textName);
        textPhone = (TextView) findViewById(R.id.textPhone);
        textMsg = (TextView) findViewById(R.id.textMsg);

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnMessage = (ImageButton) findViewById(R.id.btnMessage);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String imageUrl = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEURL);
            String name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            String phoneNumber = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER);
            String message = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(this).load(uri).resize(500, 500).centerCrop().into(imageView);
            textName.setText(name);
            textPhone.setText(phoneNumber);
            textMsg.setText(message);
        }

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBtnCall();
                }
            });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnMessage();
            }
        });
    }

    private void onClickBtnCall() { // 수정
        if(textPhone != null) {
            String phoneNo = textPhone.getText().toString();
            Uri uri = Uri.parse("tel: " + phoneNo);
            Intent i = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(i);
        } else {
            Toast.makeText(this, "번호 입력이 안되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void onClickBtnMessage() {
        // TODO: 2017-03-14 친구 프로필에서 채팅 연결하기
        Intent intent = new Intent(ProfileInfoActivity.this, ChatRoomActivity.class);

        startActivity(intent);
    }
}
