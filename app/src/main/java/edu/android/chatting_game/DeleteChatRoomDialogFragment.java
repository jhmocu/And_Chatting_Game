package edu.android.chatting_game;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteChatRoomDialogFragment extends DialogFragment {
    private int position;

    public DeleteChatRoomDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DeleteChatRoomDialogFragment(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("채팅방에서 나가시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("chat_list", "DeleteChatRoomDialogFragment// onClick()// position= " + position);
                deleteChatRoomConnect(position);
                Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}});

        return builder.create();
    }// end onCreateDialog()

    public void deleteChatRoomConnect(int position) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            ChatMessageVO vo = ChatMessageLab.getInstance().getChatMessageVOList().get(position);
            HttpDeleteChatRoomAsyncTask task = new HttpDeleteChatRoomAsyncTask();
            task.execute(vo);
        }
    }// end deleteChatRoomConnect()

    private class HttpDeleteChatRoomAsyncTask extends AsyncTask<ChatMessageVO, Void, String> {

        @Override
        protected String doInBackground(ChatMessageVO... params) {
            String result = deleteChatRoom(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1")) {
                Log.i("chat_list", "채팅방 삭제 성공");
            }
        }
    }// end class HttpDeleteChatRoomAsyncTask

    public String deleteChatRoom(ChatMessageVO vo) {
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

}// end class DeleteChatRoomDialogFragment
