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

public class Profile_My_info
        extends AppCompatActivity {
    private static final String TAG = "edu.android.chatting";

    public static final int REQ_CODE_SAVE = 1002;

    public static final String KEY_IMG = "key_my_img";
    public static final String KEY_NAME = "key_my_name";
    public static final String KEY_MSG = "key_my_statusMsg";

    private ImageView myProfileImg;
    private ImageButton btnEditInfo;
    private TextView myName, myStatusMsg;

    private String name, statusMsg, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__my_info);

        Log.i(TAG, "Profile_My_info// onCreate()");

        myProfileImg = (ImageView) findViewById(R.id.imageView_ProfileInfo);
        btnEditInfo = (ImageButton) findViewById(R.id.editInfo);
        myName = (TextView) findViewById(R.id.myName);
        myStatusMsg = (TextView) findViewById(R.id.myStatusMsg);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageUrl = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEURL);
            name = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            statusMsg = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(this).load(uri).resize(500, 500).centerCrop().into(myProfileImg);
            myName.setText(name);
            myStatusMsg.setText(statusMsg);
        }

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusEdit();
            }
        });
    }

    private void statusEdit() {
        Intent intent = new Intent(this, StatusEditActivity.class);
//        intent.putExtra(KEY_IMG, imageUrl);
        intent.putExtra("uri", Uri.parse(imageUrl));
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_MSG, String.valueOf(statusMsg));
        startActivityForResult(intent, REQ_CODE_SAVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SAVE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            name = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            statusMsg = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            Uri imageUri = (Uri) extras.get("uri");
            Log.i("uri", "Profile_My_info// onActivityResult()// uri= " + imageUri);
            // TODO: 2017-03-18 현재 창 업데이트
            myName.setText(name);
            myStatusMsg.setText(statusMsg);
            myProfileImg.setImageURI(imageUri);

            Toast.makeText(this, "변경사항 넘김" + name + statusMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
