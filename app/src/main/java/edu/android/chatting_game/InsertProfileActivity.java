package edu.android.chatting_game;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class InsertProfileActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 100;
    public static final int REQ_CODE_IMAGE_CAPTURE = 1000;

    private int image;
    private String name;
    private String statusMsg;

    private ImageView imageView;
    private EditText editName, editStatusMsg;
    private ImageButton btnEdit, btnCamera;
    private Button btnSave;
    private String my_phone;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_profile);

        imageView = (ImageView) findViewById(R.id.imageView);
        editName = (EditText) findViewById(R.id.editName);
        editStatusMsg = (EditText) findViewById(R.id.editStatus);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnSave = (Button) findViewById(R.id.btnSave);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            my_phone = extras.getString("my_phone");
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
                // 연결 가능한 네트워크 자원이 있는 지 체크
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connMgr.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    Log.i("gg", info.getTypeName() + "사용 가능");

                    String pic_path = getPathFromUri(uri);
                    Log.i("image_res", pic_path);

                    String name = editName.getText().toString();
                    String status_msg = editStatusMsg.getText().toString();
                    // 데이터 넣는곳
                    ProfileVO vo = new ProfileVO(my_phone, name, pic_path, status_msg);
                    HttpSendAsyncTask task = new HttpSendAsyncTask();
                    task.execute(vo);
                    writeToFile(vo.getPhone(), StartAppActivity.MY_PHONE_FILE);
                }

                Intent intent = new Intent(InsertProfileActivity.this, Main2Activity.class);

                startActivity(intent);

                setResult(RESULT_OK, intent);

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });
    }

    private class HttpSendAsyncTask extends AsyncTask<ProfileVO, String, String> {

        @Override
        protected String doInBackground(ProfileVO... params) {
            Log.i("http", "연결되었다");
            String result = sendData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String sendData(ProfileVO vo) {

        String requestURL = "http://192.168.11.11:8081/Test3/InsertProfile";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 데이터 넣는 부분
        builder.addTextBody("phone", vo.getPhone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("name", vo.getName(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("pic_res", vo.getPic_res(), ContentType.create("Multipart/related", "UTF-8"));
//        builder.addPart("image", new FileBody(new File(vo.getPic_res())));
        builder.addTextBody("status_msg", vo.getStates_msg(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("friend_count", "0", ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;

        try {
            // http 통신 send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost); // 연결 실행

            // http 통신 receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            Log.i("gg", "good");
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferdReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
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
            uri = data.getData();
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
                        "사진 선택 완료",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void writeToFile(String my_phone, String fileName) {
        OutputStream out = null; // file output stream
        OutputStreamWriter writer = null; // 인코딩된 문자열을 쓰기 위해서
        BufferedWriter bw = null;
        try {
            out = openFileOutput(fileName, MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            bw = new BufferedWriter(writer);
            bw.write(my_phone);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } // end writeToFile()

    public String getPathFromUri(Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex( "_data" ));
        cursor.close();

        return path;
    }
}
