package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 친구 프로필에서 실행하는 채팅방 액티비티
 * */

public class DirectChatRoomActivity extends AppCompatActivity implements OptionBtnFragment.optionItemSelectedListener, ProfileSendFragment.ProfileSendCallback {

    public static final String TAG = "edu.android.chatting";
    public static final String TASK_CYCLE = "task_cycle";

    private EditText writeMsg;
    private TextView textMyMsg, textYourMsg;
    private ImageButton btnOption, btnSend;
    private String title;
    private String name, my_phone;
    private String[] member_phone = new String[1];
    private String[] member_phones = {};

    private ListView listView;
    private ChatMessageReceiveLab lab;
    private ArrayList<ChatMessageReceiveVO> chatMessageList;

    private Uri uri;
    private ProfileSendFragment profileSendFragment;


    class ChatMessageAdapter extends ArrayAdapter<ChatMessageReceiveVO> {

        private List<ChatMessageReceiveVO> list;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessageReceiveVO> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("cycle", "getView()");
            my_phone = getIntent().getExtras().getString("key_my_phone");

            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());

                for (ChatMessageReceiveVO vo : chatMessageList) {

                    if (vo.getMy_phone().equals(my_phone)) { /** 내 메세지 */ /** list.get(position).getMy_phone()*/
                        view = inflater.inflate(R.layout.content_my_message, parent, false);
                        textMyMsg = (TextView) view.findViewById(R.id.textMyMsg);
                        textMyMsg.setText(list.get(position).getMsg());

                    } else { /** 상대 메세지 */
                        view = LayoutInflater.from(getContext()).inflate(R.layout.content_your_message, parent, false);
                        textYourMsg = (TextView) view.findViewById(R.id.textYourMsg);
                        textYourMsg.setText(list.get(position).getMsg()); /** 임의!! 상대 메세지 select 찾아야함 */
                    }

                }// end for
            }// end if(view)
            writeMsg.setCursorVisible(true);
            writeMsg.requestFocus();

            return view;
        }// end getView()
    }// end class ChatMessageAdapter

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

        final ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, chatMessageList);
        //TODO:채팅방 글자크기 배경색변경
        Bundle extra=getIntent().getExtras();
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

        // 메시지가 추가됐을 때, 마지막 메시지로 스크롤 --> 보류
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });
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
    protected void onStart() {
        super.onStart();
        Log.i("cycle", "onStart()");
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("cycle", "onPostResume()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Log.i("cycle", "onCreate()");
        lab = ChatMessageReceiveLab.getInstance();
        chatMessageList = lab.getChatMessageList();

        // 채팅방 정보 받아오기기
        Bundle chatExtras = getIntent().getExtras();
        if(chatExtras != null){
            // 값가져오기
            name = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
            member_phone[0] = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER); // 한명 채팅할 때 번호 값

            Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0] );
//          member_phones = chatExtras.getStringArrayList(); // 여러명 채팅할 때 번호값 //key값:"otherPhones"
        }

        Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0] );

        /** ///////////////////////////////////////////////////////////////////////////////////////////*/
        /***/
        // 처음 시작할 때 채팅방 정보들을 DB에 넘겨준다.
        String all_phone = createAllPhone(member_phone, member_phones);
        HttpConnectAsyncTask task = new HttpConnectAsyncTask();
        task.execute(all_phone);

        writeMsg = (EditText) findViewById(R.id.writeMsg);
        btnOption = (ImageButton) findViewById(R.id.btnOption);
        btnSend = (ImageButton) findViewById(R.id.btnSend);

        //writeMsg(editText) 클릭하기 전에 키보드 숨기기
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        // title: 대화상대로 set
        ActionBar actionBar = getSupportActionBar();
        // title = chatMessageList.get(0).getChatroom_name(); <- 이렇게 하는 게 어때요.
//        title = my_phone;
        actionBar.setTitle("대화방");
    }// end onCreate()

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

    private void onClickBtnSend() {
        String msg = writeMsg.getText().toString();
        HttpSendChatMessageAsyncTask task = new HttpSendChatMessageAsyncTask();
        task.execute(msg);
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

    // DB 연동
    private class HttpConnectAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String result1 = sendChatListData(params[0]);
            return null;
        }
    }// end class HttpConnectAsyncTask

    // 채팅 참여자 정보 넣기
    public String createAllPhone(String[] member_phone, String[] member_phones){
        Gson gson = new Gson();
        String json = "";
        String fileName = "";
        if(member_phone != null) {

            json = gson.toJson(member_phone);

            // 파일에 쓰기
            fileName = createFileName(member_phone);
            writeToFile(json, fileName);
        } else if(member_phones != null){

            json = gson.toJson(member_phones);

            // 파일에 쓰기
            fileName = createFileName(member_phone);
            writeToFile(json, fileName);
        }

        Log.i("allphone_file", "Direct// createAllPhone()// json:" + json + "|fileName:" + fileName);

        Log.i(TAG, "chatroomActivity : createAllPhone: " + json);

        return json;
    }

    /** 파일 이름 생성 */
    private String createFileName(String[] phones) {
        String fileName = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < phones.length; i++) {
            buffer.append(phones[i]).append(", ");
        }
        fileName = buffer.toString();

        Log.i("allphone_file", "Direct// createFileName() fileName:" + fileName);
        return fileName;
    }

    /** 파일에 쓰기 */
    private void writeToFile(String phones, String fileName) {
        Log.i("allphone_file", "Direct// writeToFile() fileName:" + fileName);
        Log.i("allphone_file", "Direct// writeToFile() phones:" + phones);
        OutputStream out = null; // file output stream
        OutputStreamWriter writer = null; // 인코딩된 문자열을 쓰기
        BufferedWriter bw = null;
        try {
            out = openFileOutput(fileName, MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            bw = new BufferedWriter(writer);
            bw.write(String.valueOf(phones));
            Toast.makeText(this, "all_phones 파일 생성 성공", Toast.LENGTH_SHORT).show();
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

        // 데이터 넣는 부분
        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8"));

        Log.i(TAG, "chatroomactivity : all_phone :" + all_phone );
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

    /***/
    /***/
    /***/ // --> onClickBtnSend
    private class HttpSendChatMessageAsyncTask extends AsyncTask<String, String, String> {
        // TODO: 2017-03-23 :: 2nd parameter :: Void

        @Override
        protected String doInBackground(String... params) {
            String result = sendChatMsgData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // TODO: 2017-03-23 서버에 메세지 보내기 완료 이후

            // receive 실행 task
//            HttpReceiveChatMessageAsyncTask task = new HttpReceiveChatMessageAsyncTask();
//            task.execute();
        }
    } // end class HttpSendChatMessageAsyncTask


    // TODO: 2017-03-24 코드 수정 예정
    // 채팅 메시지 보내기
    public String sendChatMsgData(String msg) {
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE); // 내 번호
        String all_phone = createAllPhone(member_phone, member_phones); // 참여자 번호
        Log.i(TAG, "sendChatMsgData() all_phone" + all_phone);

        // 데이터 넣는 부분
        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("last_msg", msg, ContentType.create("Multipart/related", "UTF-8"));

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
    }// end sendChatMsgData()

    private class HttpReceiveChatMessageAsyncTask extends AsyncTask<String, String, String> {
        // TODO: 2017-03-23 :: 2nd parameter :: Void

        @Override
        protected String doInBackground(String... params) {
            Log.i(TASK_CYCLE, "HttpReceiveChatMessageAsyncTask// doInBackground()");
            String result = receiveChatMsgData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // TODO: 2017-03-23 서버에서 receive 이후
        }
    }


    // TODO: 2017-03-23 메세지 받기
    // 채팅 메시지 받기
    public String receiveChatMsgData(String all_phone) {
        String requestURL = "http://192.168.11.11:8081/Test3/ /**InsertChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
        all_phone = createAllPhone(member_phone, member_phones);
        Log.i(TAG, "receiveChatMsgData()// all_phone: " + all_phone);

        // 데이터 넣는 부분
        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8"));

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

        Log.i("uri", "readFromFile()// uri=" + uri);
        return buffer.toString();
    }


} // end class ChatRoomActivity
