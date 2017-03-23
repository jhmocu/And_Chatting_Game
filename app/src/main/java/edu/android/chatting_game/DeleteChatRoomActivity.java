package edu.android.chatting_game;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

/**
 * DeleteChatRoomDialogFragment 에서
 * dialog가 destroy 되어도
 * ChatListFragment, ChatRecyclerViewFragment가 resume되지 않는 문제 해결을 위해
 * dialog - fragment 사이에서 다리 역할
 */

public class DeleteChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app );
        // TODO: 2017-03-22 삭제 작업중 잠깐 뜨는 화면 이미지 바꾸기 - 현재: activity_start_app과 동일하게 설정
        getSupportActionBar().hide();
    }// end onCreate()

    @Override
    protected void onResume() {
        super.onResume();
        int position = getIntent().getExtras().getInt("key_delete_position");
        Log.i("chat_list", "DeleteChatRoomActivity// position=" + position);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            ChatRoomVO vo = ChatRoomLab.getInstance().getChatRoomVOList().get(position);
            HttpDeleteChatRoomAsyncTask task = new HttpDeleteChatRoomAsyncTask();
            task.execute(vo);
        }
    }// end onResume()

    private class HttpDeleteChatRoomAsyncTask extends AsyncTask<ChatRoomVO, Void, String> {

        @Override
        protected String doInBackground(ChatRoomVO... params) {
            String result = deleteChatRoom(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1")) {
                Log.i("chat_list", "채팅방 삭제 성공");
            } else {
                Log.i("chat_list", "채팅방 삭제 실패");
            }
            finish();
        }
    }// end class HttpDeleteChatRoomAsyncTask

    public String deleteChatRoom(ChatRoomVO vo) {
        String result = "";
        String requestURL = "http://192.168.11.11:8081/Test3/DeleteChatList";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("phone", vo.getPhone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("chatroom_name", vo.getChatroom_name(), ContentType.create("Multipart/related", "UTF-8"));
        InputStream inputStream = null;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        try {
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            inputStream = httpEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuffer.append(line);
                line = bufferedReader.readLine();
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
    }// end deleteChatRoom()

}// end class DeleteChatRoomActivity