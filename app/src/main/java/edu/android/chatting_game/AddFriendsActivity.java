package edu.android.chatting_game;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;

public class AddFriendsActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_NAME = "key_name";
    public static final String KEY_EXTRA_PHONENUMBER = "key_phone";
    public static final String KEY_EXTRA_MESSAGE = "key_msg";
    public static final String KEY_EXTRA_IMAGEID = "key_image";

    private EditText editPhoneAdd;
    private Button btnAddFriend;

    private ArrayList<Friend> list = new ArrayList<Friend>();
    FriendLab lab = FriendLab.getInstance();

    Friend friend = new Friend();

    private String[] dummyGasangData = {
            "1111", "2222", "3333", "4444"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        editPhoneAdd = (EditText) findViewById(R.id.editPhoneAdd);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list = lab.getFriendList();
                String str = "";
                String phoneNo = "";
                boolean run = true;
                boolean found = false;
                while (run) {
                    for (int i = 0; i < list.size(); i++) {
                        phoneNo = list.get(i).getPhone();

                        if (editPhoneAdd.getText().toString().equals(phoneNo)) {
                            addFriendConnect();
                            Toast.makeText(AddFriendsActivity.this, "일치\nphoneNo:" + phoneNo + "\n" +
                                    "edit:" + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
                            found = true;
                            break;
                        } // end if()

                    } //end for()
                    break;
                } // end while()
                if (!found) {
                    Toast.makeText(AddFriendsActivity.this, "불일치\nphoneNo:" + phoneNo + "\n" +
                            "edit:" + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
                } // end if(!found)

            } // onClick()
        }); // end setOnClickListener()

    } // end onCreate()

    private void addFriendConnect() {
        ConnectivityManager connMng = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connMng.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {

            String my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
            String friend_phone = editPhoneAdd.getText().toString();

            FriendVO vo = new FriendVO(my_phone, friend_phone, null);

            HttpAddFriendAsyncTask task = new HttpAddFriendAsyncTask();
            task.execute(vo);

        }
    }

    class HttpAddFriendAsyncTask extends AsyncTask<FriendVO, String, String> {

            @Override
            protected String doInBackground(FriendVO... params) {
                String result = sendData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

    public String sendData(FriendVO vo) {
                String requestURL = "http://192.168.11.11:8081/Test3/AddFriend";
                String result = "";
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addTextBody("my_phone", vo.getMy_phone(), ContentType.create("Multipart/related", "UTF-8"));
                builder.addTextBody("friend_phone", vo.getFriend_phone(), ContentType.create("Multipart/related", "UTF-8"));

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


