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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.sephiroth.android.library.picasso.Picasso;

public class ProfileInfoActivity
        extends AppCompatActivity {
    private static final String TAG = "edu.android.chatting";

    private ImageView imageView;
    private TextView textName, textPhone, textMsg;
    private ImageButton btnCall, btnMessage; // 수정
    String name;
    String phoneNumber;
    String message;
    String my_phone ;
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
            name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            phoneNumber = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER);
            message = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
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
        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
        Intent intent = new Intent(ProfileInfoActivity.this, ChatRoomActivity.class);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME, name);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER, phoneNumber);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE, message);
        intent.putExtra(my_phone, my_phone);
        startActivity(intent);
        Log.i(TAG, "ProfileInfoActivity/ name:" + name);
        Log.i(TAG, "ProfileInfoActivity/ phoneNum:" + phoneNumber);
        Log.i(TAG, "ProfileInfoActivity/ message:" + message);
        Log.i(TAG, "ProfileInfoActivity/ my_phone:" + my_phone);
    }

    public String readFromFile(String filename) {
        // 파일에서 읽은 문자열을 append할 변수
        StringBuffer buffer = new StringBuffer();

        InputStream in = null; // file input stream
        InputStreamReader reader = null; // 인코딩된 문자열을 읽기 위해서
        BufferedReader br = null; //

        try {
            in = openFileInput(filename);
            reader = new InputStreamReader(in);
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null){
                buffer.append(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("gg", buffer.toString());
        return  buffer.toString();
    }
}
