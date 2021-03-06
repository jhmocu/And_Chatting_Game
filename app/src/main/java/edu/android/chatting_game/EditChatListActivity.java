package edu.android.chatting_game;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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

public class EditChatListActivity extends AppCompatActivity
        implements EditChatRecyclerViewFragment.onSelectedListener {
    private static final String TAG = "edu.android.chatting";
    private Button btnBack, btnEditChatFinish;
    private TextView textEditChatCount;
    private CheckBox editChatcheckBox;
    private int position;
    private int count;
    private ArrayList<Integer> positions = new ArrayList<>();
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private ArrayList<ChatRoomVO> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chat_list);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.frameEditChatList);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnEditChatFinish = (Button) findViewById(R.id.btnEditChatFinish);
        textEditChatCount = (TextView) findViewById(R.id.textEditChatCount);
        editChatcheckBox = (CheckBox) findViewById(R.id.editChatcheckBox);
        textEditChatCount.setText(String.valueOf(count));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); // 편집이라는 부분을 누르면 다시 이전 액티비티로 돌아가는 기능

        if (frag == null) {
            frag = new EditChatListFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.frameEditChatList, frag);
            transaction.commit();
        } // RecyclerView List를 띄어줌

        // TODO: 체크된 채팅방 나가기 기능(진행중)
        btnEditChatFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connMgr.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    for (int p : positions) {
                        Log.i(TAG, "EditChatListActivity// onClickBtnEditFinish() size: " + positions.size());
                        ChatRoomVO vo = ChatRoomLab.getInstance().getChatRoomVOList().get(p);
                        HttpDeleteChatRoomAsyncTask task = new HttpDeleteChatRoomAsyncTask();
                        task.execute(vo);
                        Log.i(TAG, "execute()");
                    }
                }
            }

        });
    }

    private class HttpDeleteChatRoomAsyncTask extends AsyncTask<ChatRoomVO, Void, String> {

        @Override
        protected String doInBackground(ChatRoomVO... params) {
            String result = deleteChatRoom(params[0]);
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            finish();
        }

    }

    private void setResult() {
    }

    public String deleteChatRoom(ChatRoomVO vo) {
        String requestURL = "http://192.168.11.11:8081/Test3/DeleteChatList";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

//        Log.i(TAG, "deleteChatRoom()// vo: "+ vo.getPhone() +"\t" + vo.getChatroom_name());
        builder.addTextBody("phone", vo.getPhone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("chatroom_name", vo.getChatroom_name(), ContentType.create("Multipart/related", "UTF-8"));

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
            Log.i(TAG, "result: " + result);
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

    @Override
    public void itemSelected(int count, int position, ArrayList<Boolean> selectedList, ArrayList<Integer> positions) {
        Log.i(TAG, "EditChatListActivity// itemSelected()// position.size(): " + positions.size());
        this.positions = positions;
        this.position = position;
        this.count = count;
        textEditChatCount.setText(String.valueOf(count));
        this.selectedList = selectedList;
    }
}
