package edu.android.chatting_game;


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
    private FloatingActionButton floatingBtn;
    private static final String TAG = "edu.android.chatting";
    private ArrayList<Friend> list = new ArrayList<>();
    private FriendLab lab;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        StartAppActivity startapp = new StartAppActivity();
        //        String my_phone = startapp.readFromFile(StartAppActivity.MY_PHONE_FILE);

        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
        task.execute("010");

        Log.i(TAG, "FriendsListFragment\tonCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        floatingBtn = (FloatingActionButton) view.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);

                startActivity(intent);
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
            Log.i(TAG, "doInBackground()\tparams[0]: " + params[0]);
            String result = selectProfile(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute()\nString s:\n" + s);
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
        String requestURL = "http://192.168.11.11:8081/Test3/SelectProfile";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("phone", s/** my_phone */, ContentType.create("Multipart/related", "UTF-8"));

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

}// end class FriendsListFragment