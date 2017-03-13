package edu.android.chatting_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile_My_info extends AppCompatActivity {
    public static final int REQ_CODE_SAVE=1002;

    private ImageView myProfileInfo;
    private ImageButton editInfo;
    private TextView myName, myStatusMsg;

    private int imageId;
    private String name, statusMsg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__my_info);

        myProfileInfo = (ImageView) findViewById(R.id.imageView_ProfileInfo);
        editInfo = (ImageButton) findViewById(R.id.editInfo);
        myName = (TextView) findViewById(R.id.myName);
        myStatusMsg = (TextView) findViewById(R.id.myStatusMsg);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            imageId = extras.getInt(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID);
            name = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            statusMsg1 = extras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            myProfileInfo.setImageResource(imageId);
            myName.setText(name);
            myStatusMsg.setText(statusMsg1);
        }


        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               statusEdit();
            }
        });
    }

    private void statusEdit() {
        Intent intent = new Intent(this, StatusEditActivity.class);
        intent.putExtra("myProfile", imageId);
        intent.putExtra("myName", name);
        intent.putExtra("myStatusMsg", String.valueOf(statusMsg1));
        startActivityForResult(intent,REQ_CODE_SAVE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE_SAVE &&resultCode==RESULT_OK){
            String image=data.getStringExtra(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID);
            String name=data.getStringExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            String status=data.getStringExtra(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE);
            Toast.makeText(this,"변경사항 넘김"+name+status,Toast.LENGTH_SHORT).show();
        }
    }
}
