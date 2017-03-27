package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 친구 프로필에서 실행하는 채팅방 액티비티
 */

public class DirectChatRoomActivity extends AppCompatActivity implements OptionBtnFragment.optionItemSelectedListener, ProfileSendFragment.ProfileSendCallback {

    public static final String TAG = "edu.android.chatting";
    public static final String TASK_CYCLE = "task_cycle";

    private EditText writeMsg;
    private TextView textChatMessage;
    private ImageButton btnOption, btnSend;
    private String name, my_phone, chatroom_name, all_phone;
    private String[] member_phone = new String[1];
    private String[] member_phones = {};

    private ListView listView;
    private MessageLab lab;
    private ArrayList<MessageVO> messageList;

    private Uri uri;
    private ProfileSendFragment profileSendFragment;


    class ChatMessageAdapter extends ArrayAdapter<MessageVO> {

        private List<MessageVO> list;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MessageVO> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @Override
        public void add(@Nullable MessageVO object) {
            super.add(object);
            list.add(object);
            Log.i("app_cycle", "add()");
        }

        @Override
        public int getCount() {
            Log.i("app_cycle", "getCout()");
            return this.list.size();
        }

        @Nullable
        @Override
        public MessageVO getItem(int position) {
            Log.i("app_cycle", "getItem()");
            return this.list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("cycle", "getView()");
            my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);

            View view = convertView;
            if (view == null) {
                MessageVO vo = getItem(position);
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (vo.getSender().equals(my_phone)) {
                    view = inflater.inflate(R.layout.right_mine, parent, false);

                } else {
                    view = inflater.inflate(R.layout.left_yours, parent, false);
                }
                textChatMessage = (TextView) view.findViewById(R.id.textChatMessage);
                textChatMessage.setText(messageList.get(position).getMsg());
            }
            writeMsg.setCursorVisible(true);
            writeMsg.requestFocus();

            return view;
        }// end getView()
    }// end class ChatMessageAdapter

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

        final ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, messageList);
        //TODO:채팅방 글자크기 배경색변경
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int Color = extra.getInt("Background");
            ListView chat = (ListView) findViewById(R.id.chatMessageListView);
            chat.setBackgroundColor(Color);
//            Float Size=extra.getFloat("fontChange");
//            textYourMsg.setTextSize(Size);
//            textMyMsg.setTextSize(Size);
        }


        listView = (ListView) findViewById(R.id.chatMessageListView);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Log.i(TAG, ProfileInfoActivity.class.getName());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.search:
                break;
            case R.id.chat_room_option1:
                break;
            case R.id.chat_room_option2:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Log.i("cycle", "onCreate()");
        lab = MessageLab.getInstance();
        messageList = lab.getMessageList();

        // 채팅방 정보 받아오기기
        Bundle chatExtras = getIntent().getExtras();
        if (chatExtras != null) {
            // 값가져오기
            name = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            member_phone[0] = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER); // 한명 채팅할 때 번호 값

            Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0]);
//          member_phones = chatExtras.getStringArrayList(); // 여러명 채팅할 때 번호값 //key값:"otherPhones"
        }

        Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0]);

        // 파일이 있는지 검사
        if (member_phone != null) {
            chatroom_name = createFileName(member_phone);
        } else if (member_phones != null) {
            chatroom_name = createFileName(member_phone);
        }
        Log.i(TAG, "chatroomactivity : chatroom_name:" + chatroom_name);

        if (!this.getFileStreamPath(chatroom_name).exists()) {
            all_phone = createAllPhone(member_phone, member_phones);
        } else {
            all_phone = getAllPhone(chatroom_name);

        }
        HttpConnectAsyncTask task = new HttpConnectAsyncTask();
        task.execute(all_phone);

        writeMsg = (EditText) findViewById(R.id.writeMsg);
        btnOption = (ImageButton) findViewById(R.id.btnOption);
        btnSend = (ImageButton) findViewById(R.id.btnSend);


        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        writeMsg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return onClickBtnSend();
                }
                return false;
            }
        });

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ' + ' 버튼 이벤트 처리
                DialogFragment optionClickFragment = OptionBtnFragment.newInstance();
                optionClickFragment.show(getFragmentManager(), "optionClick_dialog");
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnSend();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String map = extras.getString(MapsActivity.EXTRA_MAP);
            writeMsg.setText(map);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(all_phone);

//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo info = connMgr.getActiveNetworkInfo();
//        if (info != null && info.isAvailable()) {
//            HttpReceiveChatMessageAsyncTask receiveTask = new HttpReceiveChatMessageAsyncTask();
//            receiveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }

    }// end onCreate()

    private String getAllPhone(String chatroom_name) {
        String all_phone = readFromFile(chatroom_name).toString();

        return all_phone;
    }

    @Override
    public void optionItemSelected(int which) {
        switch (which) {
            case 0:
                sendContact();
                break;
            case 1:
                mapOpen();
                break;
            case 2:
                profileSendFragment = new ProfileSendFragment();
                profileSendFragment.show(getSupportFragmentManager(), "profile_send_dialog");
                break;
        }
    }


    public void mapOpen() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 2013-03-13 플러스 버튼 연락처 보내기.
        String name = null;
        String number = null;
        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            number = cursor.getString(1);      //1은 번호를 받아옵니다.
            cursor.close();
            writeMsg.setText("이름: " + name + "\n" + " 번호: " + number);
        }
    }

    @Override
    public void profilesend(int position) {
        Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();
        String name = FriendLab.getInstance().getFriendList().get(position).getfName();
        String phone = FriendLab.getInstance().getFriendList().get(position).getPhone();
        writeMsg.setText("이름: " + name + "\n" + "핸드폰 번호: " + phone);
        profileSendFragment.dismiss();  // 아이템뷰 클릭시 다이얼로그 창 닫기 위함~
    }

    private boolean onClickBtnSend() {
        String msg = writeMsg.getText().toString();
        if (msg != null && !msg.isEmpty()) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                HttpSendChatMessageAsyncTask task = new HttpSendChatMessageAsyncTask();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msg);
            }
            writeMsg.setText("");
        }
        return true;
    }

    /**
     * --> onClickBtnSend
     */
    private class HttpSendChatMessageAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = sendChatMsgData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    } // end class HttpSendChatMessageAsyncTask


    public String sendChatMsgData(String msg) {
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);

        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("last_msg", msg, ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;
        try {
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

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
    }// end sendChatMsgData()

    private class HttpConnectAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String result1 = sendChatListData(params[0]);
            return null;
        }
    }// end class HttpConnectAsyncTask

    // 채팅 참여자 정보 넣기
    public String createAllPhone(String[] member_phone, String[] member_phones) {
        Gson gson = new Gson();
        String json = "";
        String fileName = "";
        if (member_phone != null) {
            json = gson.toJson(member_phone);

            fileName = createFileName(member_phone);
            writeToFile(json, fileName);
        } else if (member_phones != null) {
            json = gson.toJson(member_phones);

            fileName = createFileName(member_phone);
            writeToFile(json, fileName);
        }
        return json;
    }

    /**
     * 파일 이름 생성
     */
    private String createFileName(String[] phones) {
        String fileName = "";
        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
        StringBuffer buffer = new StringBuffer();
        buffer.append("a").append(my_phone).append(phones[0]);
        fileName = buffer.toString();

        return fileName;
    }

    /**
     * 파일에 쓰기
     */
    private void writeToFile(String phones, String fileName) {
        OutputStream out = null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            out = openFileOutput(fileName, MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            bw = new BufferedWriter(writer);
            bw.write(String.valueOf(phones));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }// end writeToFile()

    // 채팅방 생성 정보 넘기기
    public String sendChatListData(String all_phone) {
        String requestURL = "http://192.168.11.11:8081/Test3/InsertChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);

        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8"));

        Log.i(TAG, "chatroomactivity : all_phone :" + all_phone);
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
    }// end sendChatListData()


    private class HttpReceiveChatMessageAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground()");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String result = receiveChatMsgData();
                        Log.i(TASK_CYCLE, "thread// run()// result: " + result);

                        Gson gson = new Gson();
                        TypeToken<ArrayList<MessageVO>> typeToken = new TypeToken<ArrayList<MessageVO>>() {
                        };
                        Type type = typeToken.getType();

                        messageList = gson.fromJson(result, type);
                        MessageLab.getInstance().setMessageList(messageList);

//                        chatMessageAdapter.notifyDataSetChanged();
                        synchronized (this) {
                            try {
                                wait(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
            return null;
        }
    }// end class HttpReceiveChatMessageAsyncTask

    public String receiveChatMsgData() {
        String result = "";
        String checked = "false";
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatTableData";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("table_name", chatroom_name, ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;

        try {
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

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
            while (line != null) {
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

        return buffer.toString();
    }


} // end class ChatRoomActivity
