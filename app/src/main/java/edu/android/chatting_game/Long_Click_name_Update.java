package edu.android.chatting_game;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Long_Click_name_Update extends AppCompatActivity {
    private static final String TAG = "edu.android.chatting";

    private EditText textView;
    private ImageButton btn;
    private String friend_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long__click_name__update);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (EditText) findViewById(R.id.editName);
        btn = (ImageButton) findViewById(R.id.button);
        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            friend_name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME2);
            textView.setText(friend_name);
            Log.i(TAG, "원래이름:\t" + friend_name);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connMgr.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    StartAppActivity startAppActivity = new StartAppActivity();
//                    String my_phone = startAppActivity.readFromFile(StartAppActivity.MY_PHONE_FILE);
                    String my_phone = "010";
                    Log.i("gg", info.getTypeName() + "사용 가능");
                    String friend_phone="0102";

                    friend_name = textView.getText().toString();
                    Log.i(TAG, "변경된이름:\t" + friend_name);

                    FriendVO vo = new FriendVO(my_phone,friend_phone,friend_name);
                    HttpNameAsyncTask task = new HttpNameAsyncTask();
                    task.execute(vo);
                }
            }
        });
    }
        private class HttpNameAsyncTask extends AsyncTask<FriendVO, String, String>{


            @Override
            protected String doInBackground(FriendVO... params) {
                String result=sendData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                finish();
            }
        }

        public String sendData(FriendVO vo) {

            String requestURL = "http://192.168.11.11:8081/Test3/UpdateFriend";
            String result = "";
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addTextBody("my_phone", vo.getMy_phone(), ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("friend_phone", vo.getFriend_phone(), ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("friend_name", vo.getFriend_name(), ContentType.create("Multipart/related", "UTF-8"));

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
    }

