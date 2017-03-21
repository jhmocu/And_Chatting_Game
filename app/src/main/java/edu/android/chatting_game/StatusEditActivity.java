package edu.android.chatting_game;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import org.apache.http.entity.mime.content.FileBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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
    private String my_phone;
    private Uri uri;

    private String pic_res;

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


            editName.setText(name);
            editStatusMsg.setText(statusMsg);
            imageView.setImageResource(image);


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
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connMgr.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    Log.i("zz", info.getTypeName() + "사용 가능");

                    String pic_path = null;

                    // TODO: 기본 이미지 설정 (진행중)
                    if(uri == null) {

//                        pic_path = getPathFromUri2(uri);

                    } else if(uri != null){

                        pic_path = getPathFromUri(uri);
                    }


                    Log.i("image_res", pic_path);



                    my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
                    Log.i("test", my_phone);
                    String name = editName.getText().toString();
                    String status_msg = editStatusMsg.getText().toString();
                    // 데이터 넣는 곳
                    ProfileVO vo = new ProfileVO(my_phone, name, pic_path, status_msg);
                    HttpUpdateProfileAsyncTask task = new HttpUpdateProfileAsyncTask();
                    task.execute(vo);
                }

                Intent intent = new Intent();
                String name=editName.getText().toString();
                String status=editStatusMsg.getText().toString();
                int image=imageView.getImageAlpha();

                // TODO: 기본이미지 설정! - 선택안할 시 에러 방지
                intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEURL, pic_res);
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
            uri = data.getData();
//            Log.i("이미지 경로", uri.toString());
//            imageView.setImageURI(uri);

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
                        "사진 저장 완료",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: UpdateProfileAsyncTask DB 업데이트 시작부분
    private class HttpUpdateProfileAsyncTask extends AsyncTask<ProfileVO, String, String> {

        @Override
        protected String doInBackground(ProfileVO... params) {
            String result = sendData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String sendData(ProfileVO vo) {
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateProfile";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        pic_res = vo.getPic_res();

        Log.i("test", vo.getPhone() +", " + vo.getName()+ "," + vo.getPic_res() + "," + vo.getStates_msg());
        builder.addTextBody("phone", vo.getPhone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("pic_res", vo.getPic_res(), ContentType.create("Multipart/related", "UTF-8"));

        builder.addPart("image", new FileBody(new File("/res/drawable/p1.png")));

        builder.addPart("image", new FileBody(new File(vo.getPic_res())));
        builder.addTextBody("name", vo.getName(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("status_msg", vo.getStates_msg(), ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
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

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                httpPost.abort();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

//    public String getPathFromUri2(Uri uri) {
//        String[] filePathColumn = {};
//        Cursor cursor = getContentResolver().query(null, null, null, null, null);
//        cursor.moveToNext();
//        String path = cursor.getString(cursor.getColumnIndex("_data"));
//        cursor.close();
//
//        return path;
//    }

    public String getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
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
