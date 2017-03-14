package edu.android.chatting_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class StatusEditActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 100;
    public static final int REQ_CODE_IMAGE_CAPTURE = 1000;

    private int image;
    private String name;
    private String statusMsg;


    private ImageView imageView;
    private EditText editName, editStatusMsg;
    private ImageButton btnEdit, btnCamera;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_edit);

        imageView = (ImageView) findViewById(R.id.imageView);
        editName = (EditText) findViewById(R.id.editName);
        editStatusMsg = (EditText) findViewById(R.id.editStatus);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
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

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });
    }

    private void albumSelect() {   // 앨범 선택
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void capturePhoto() { // 직접 찍은 사진 이미지로 선택
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQ_CODE_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
//            Log.i("이미지 경로", uri.toString());
            imageView.setImageURI(uri);

            if (requestCode == REQ_CODE_IMAGE_CAPTURE &&
                    resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this,
                            "사진 이미지 데이터 없음",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,
                        "NOT OK",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
