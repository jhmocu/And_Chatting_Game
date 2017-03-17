package edu.android.chatting_game;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsListFragment
        extends Fragment {
    private static final String TAG = "edu.android.chatting";
    private FloatingActionButton floatingBtn;
    private Button btnSearchFriend;
    private EditText editNameSearch;
    private ArrayList<Friend> list = new ArrayList<>();
    private FriendLab lab;
    private String my_phone;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FriendsListFragment(String my_phone) {
        this.my_phone = my_phone;

    }

    @Override
    public void onResume() {
        super.onResume();
        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
        task.execute(my_phone);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "FriendsListFragment//onCreate()//my_phone:" + my_phone);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        editNameSearch = (EditText) view.findViewById(R.id.editNameSearch);
        floatingBtn = (FloatingActionButton) view.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });
        btnSearchFriend = (Button) view.findViewById(R.id.btnSearchFriend);
        btnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-16 친구 이름으로 검색
//                onClickBtnSearchFriend();
            }
        });
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }


    private class HttpSelectFriendAsyncTask
            extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = selectProfile(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            TypeToken<ArrayList<Friend>> typeToken = new TypeToken<ArrayList<Friend>>() {};
            Type type = typeToken.getType();
            list = gson.fromJson(s, type);
            if (list != null) {
                lab = FriendLab.getInstance();
                lab.setFriendList(list);
            }
            updateFriendsList();
        }

    } // end class HttpSelectFriendAsyncTask

    public String selectProfile(String s) {
        Log.i(TAG, "s: " + s);
        String requestURL = "http://192.168.11.11:8081/Test3/SelectProfile";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("phone", s/** my_phone */, ContentType.create("Multipart/related", "UTF-8"));
        Log.i(TAG, "FriendsListFragment//selectProfile()//my_phone:" + my_phone);
        InputStream inputStream = null;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        try {
            // send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);
            Log.i(TAG, "연결됨");

            // receive
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
            Log.i(TAG, "다 읽음\nresult: " + result);

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
    }// end selectProfile()

    private void updateFriendsList() {
        FriendsRecyclerViewFragment fragment = new FriendsRecyclerViewFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_recyclerView, fragment);
        transaction.commit();
    }// end updateFriendsList()


    public void setMyPhone(String myPhone) {
        my_phone = myPhone;
    }
}// end class FriendsListFragment