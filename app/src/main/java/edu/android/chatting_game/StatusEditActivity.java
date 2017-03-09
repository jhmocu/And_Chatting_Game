package edu.android.chatting_game;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatusEditActivity extends AppCompatActivity {

    private String image;
    private String name;
    private String statusMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_edit);


//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            image = extras.getString("myProfile");
//            name = extras.getString("myName");
//            statusMsg = extras.getString("myStatusMsg");
//        }
//
//        ProfileImageEdit imageFragment = new ProfileImageEdit();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString("image", image);
//
//        ProfileTextEdit textFragment = new ProfileTextEdit();
//        Bundle bundle2 = new Bundle();
//        bundle2.putString("name", name);
//        bundle2.putString("statusMsg", statusMsg);

    }
}
