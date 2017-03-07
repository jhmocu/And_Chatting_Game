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
        // TODO: 2017-03-07 RecyclerViewFriendsFragment에서 정보 제대로 오는 지 확인
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String name = extra.getString(RecyclerViewFriendsFragment.KEY_EXTRA_NAME);
            int imageId = extra.getInt(RecyclerViewFriendsFragment.KEY_EXTRA_IMAGEID);

            textName.setText(name);
            imageView.setImageResource(imageId);
        }


    }
}
