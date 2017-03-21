package edu.android.chatting_game;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsListFragment extends Fragment {
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
        Log.i("uri", "FriendsListFragment// onResume()");
        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
        task.execute(my_phone);
//        task.execute("010");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("my_phone", "FriendsListFragment// onCreate()// my_phone:" + my_phone);
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

                // TODO: 2017-03-20 팝업 기능 테스트 -> 백그라운드 서비스에서 구현 예정 -> RestartService// onReceive()
                // 알림을 띄우기 위해 서비스 불러옴
                NotificationManager notificationManager =
                        (NotificationManager)getContext().getSystemService(Activity.NOTIFICATION_SERVICE);

                PendingIntent contentIntent = PendingIntent.getActivity/** OR getService() OR getBroadcastReceiver() */
                        (getContext(), 0, new Intent(getContext(),
                                ChatRoomActivity.class/**알림 터치했을 때 호출할 액티비티*/
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification
                = new Notification.Builder(getContext())
                        .setContentTitle("알림 제목")
                        .setContentText("내용")
                        .setTicker("알림 발생시 잠깐 나오는 텍스트 - 실제 디바이스에서만 나옴")
                        .setSmallIcon(R.drawable.p2)
                        .setContentIntent(contentIntent)/**알림 터치시 실행할 작업 인텐트*/
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) /**알람 발생시 진동, 사운드등을 설정*/
                        .setAutoCancel(true)/**알림 터치시 자동 삭제*/
                        .setPriority(Notification.PRIORITY_MAX)/**헤드업 알림*/
                        .build();

                // Send the notification.
                notificationManager.notify(0, notification);
            }
        });
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    private class HttpSelectFriendAsyncTask extends AsyncTask<String, String, String> {

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
            Log.i(TAG, "FriendsListFragment// onPostExecute()// String s" + s);
            list = gson.fromJson(s, type);
            Log.i(TAG, "FriendsListFragment// onPostExecute()// list" + list.toString());
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
        Log.i("my_phone", "FriendsListFragment// HttpSelectFriendAsyncTask// selectProfile()// my_phone:" + my_phone);
        InputStream inputStream = null;
        AndroidHttpClient androidHttpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        try {
            // send
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = androidHttpClient.execute(httpPost);

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

        } catch (Exception e) {
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