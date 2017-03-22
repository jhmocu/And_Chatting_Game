package edu.android.chatting_game;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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

public class EditChatListActivity extends AppCompatActivity
        implements EditChatRecyclerViewFragment.onSelectedListener {
    private static final String TAG = "edu.android.chatting";
    private Button btnBack, btnEditChatFinish;
    private TextView textEditChatCount;
    private CheckBox editChatcheckBox;
    private int count;
    private ArrayList<Boolean> selectedList;
    private ArrayList<ChatMessageVO> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chat_list);
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
                    ChatMessageVO vo = ChatMessageLab.getInstance().getChatMessageVOList().get(count);
                    HttpDeleteChatRoomAsyncTask task = new HttpDeleteChatRoomAsyncTask();
                    task.execute(vo);

                }

//                ChatMessageVO vo  = list.get(count).getPhone();
//                for(int i = 0; i < selectedList.size(); i++) {
//                    ChatMessageVO vo = list.get(i);
//                    sendData(vo);
//                }

                    setResult(RESULT_OK, getIntent());
                    finish();

                    // FAB에서는 startActivityForResult() 호출
                    // setResult()...
                    // finish()

            }
        });
    }

    private class HttpDeleteChatRoomAsyncTask extends AsyncTask<ChatMessageVO, Void, String> {

        @Override
        protected String doInBackground(ChatMessageVO... params) {
            String result = deleteChatRoom(params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    private void setResult() {
    }

    public String deleteChatRoom(ChatMessageVO vo) {
        String requestURL = "http://192.168.11.11:8081/Test3/DeleteChatList";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("phone", vo.getPhone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("chartroom_name", vo.getChatroom_name(), ContentType.create("Multipart/related", "UTF-8"));

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

    @Override
    public void itemSelected(int count, ArrayList<Boolean> selectedList) {
        this.count = count;
        textEditChatCount.setText(String.valueOf(count));
        this.selectedList = selectedList;
    }
}
