package edu.android.chatting_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class StatusEditActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 100;


    private int image;
    private String name;
    private String statusMsg;


    private ImageView imageView;
    private EditText editName, editStatusMsg;
    private ImageButton btnEdit;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_edit);

        imageView = (ImageView) findViewById(R.id.imageView);
        editName = (EditText) findViewById(R.id.editName);
        editStatusMsg = (EditText) findViewById(R.id.editStatus);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnSave = (Button) findViewById(R.id.btnSave);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            image = extras.getInt("myProfile");
            name = extras.getString("myName");
            statusMsg = extras.getString("myStatusMsg");

            imageView.setImageResource(image);
            editName.setText(name);
            editStatusMsg.setText(statusMsg);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumSelect();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String name=editName.getText().toString();
                String status=editStatusMsg.getText().toString();
                int image=imageView.getImageAlpha();

                intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID,image);
                intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME, name);
                intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_MESSAGE, status);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            Log.i("이미지 경로", uri.toString());
            imageView.setImageURI(uri);
        }
    }
}
