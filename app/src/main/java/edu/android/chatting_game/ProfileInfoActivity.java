package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileInfoActivity
        extends AppCompatActivity {

    private ImageView imageView;
    private TextView textName, textPhone, textMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        imageView = (ImageView) findViewById(R.id.imageView_ProfileInfo);
        textName = (TextView) findViewById(R.id.textName);
        textPhone = (TextView) findViewById(R.id.textPhone);
        textMsg = (TextView) findViewById(R.id.textMsg);

        // TODO: 2017-03-07 RecyclerViewFriendsFragment에서 정보 제대로 오는 지 확인
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int imageId = extra.getInt(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID);
            String name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            String phoneNumber = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER);
            String message = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            imageView.setImageResource(imageId);
            textName.setText(name);
            textPhone.setText(phoneNumber);
            textMsg.setText(message);
        }


    }
}
