package edu.android.chatting_game;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    public static final int REQ_CODE_EDIT_CHAT = 444;
    public static final int REQ_CODE_ADD_CHAT = 555;
    private EditText editText;
    private FloatingActionButton floatingEditChatList, floatingBtnChatAdd, floatingBtnBase;
    private boolean isFABOpen;
    private ArrayList<ChatRoomVO> list;
    private ChatRoomLab lab;
    private String my_phone;

    //private colorChangeListener listener;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ChatListFragment(String my_phone) {
        this.my_phone = my_phone;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("cycle", "ChatListFragment// onResume()");
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            HttpSelectChatListAsyncTask task = new HttpSelectChatListAsyncTask();
            task.execute(my_phone);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        editText = (EditText) view.findViewById(R.id.editNameSearch);
        floatingEditChatList = (FloatingActionButton) view.findViewById(R.id.floatingEditChatList);
        floatingBtnChatAdd = (FloatingActionButton) view.findViewById(R.id.floatingBtnChatAdd);
        floatingBtnBase = (FloatingActionButton) view.findViewById(R.id.floatingBtnBase);
        floatingBtnBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        floatingBtnChatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MultiAddChatActivity.class);
                startActivity(intent);
            }
        });

        floatingEditChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditChatListActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_CHAT);
            }
        });

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    private void showFABMenu(){
        isFABOpen = true;
        floatingBtnChatAdd.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        floatingEditChatList.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu(){
        isFABOpen = false;
        floatingBtnChatAdd.animate().translationY(0);
        floatingEditChatList.animate().translationY(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // EditCahtListActivity가 보내준 ArrayList를 가지고
        // true로 된 위치의 아이템들을 삭제
    }

    private class HttpSelectChatListAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = selectChatList(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateChatList(s);
        }
    }// end class HttpSelectChatListAsyncTask

    public String selectChatList(String phone) {
        String result = "";
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatList";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("phone", phone, ContentType.create("Multipart/related", "UTF-8"));
        InputStream inputStream = null;
        AndroidHttpClient androidHttpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        try {
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());
            httpResponse = androidHttpClient.execute(httpPost);
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
            Log.i("result", "SelectChatList()\n result= " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                androidHttpClient.close();
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }// end selectChatList()

    public void updateChatList(String s) {
        Log.i("result", "updateChatList()\n String s=" + s);
        Gson gson = new Gson();
        TypeToken<ArrayList<ChatRoomVO>> typeToken = new TypeToken<ArrayList<ChatRoomVO>>() {};
        Type type = typeToken.getType();
//        Type type = new TypeToken<ArrayList<ChatRoomVO>>(){}.getType();
        list = gson.fromJson(s, type);
        Log.i("result", "updateChatList()\n list=" + list.toString());
        if (list != null) {
            lab = ChatRoomLab.getInstance();
            lab.setChatRoomVOList(list);
        }
        attachChatRecyclerView();
    }// end updateChatList()

    public void attachChatRecyclerView() {
        ChatRecyclerViewFragment fragment = new ChatRecyclerViewFragment(my_phone);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_chat_recyclerView, fragment);
        transaction.commit();
    }

}// end class ChatListFragment
