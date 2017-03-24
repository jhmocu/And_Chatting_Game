package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.List;

/**
 * ChatRecyclerViewFragment 에서 호출하는 Activity
 */


public class ChatRoomActivity extends AppCompatActivity implements OptionBtnFragment.optionItemSelectedListener, ProfileSendFragment.ProfileSendCallback {

    public static final String TAG = "edu.android.chatting";
    public static final String TASK_CYCLE = "task_cycle";

    private EditText writeMsg;
    private TextView textMyMsg, textYourMsg;
    private ImageButton btnOption, btnSend;
    private String chatroom_name;
    private String name, my_phone;
//    private String[] member_phone = new String[1];
//    private String[] member_phones = {};
    private String all_phone;

    private ListView listView;
    private ChatMessageReceiveLab lab;
    private ArrayList<ChatMessageReceiveVO> chatMessageList;

    private Uri uri;
    private ProfileSendFragment profileSendFragment;

    public ChatRoomActivity() {
//        Bundle extras = getIntent().getExtras();
//        my_phone = extras.getString("key_my_phone");
//        chatroom_name = extras.getString("key_room_name");
    }

    class ChatMessageAdapter extends ArrayAdapter<ChatMessageReceiveVO> {

        private List<ChatMessageReceiveVO> list;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessageReceiveVO> objects) {
            super(context, resource, objects);

//            this.list = objects;
            this.list = ChatMessageReceiveLab.getInstance().getChatMessageList();
            if (list != null) {
                Log.i(TAG, "ChatRoomActivity// ChatMessageAdapter 생성자// list(object) != null");
                Log.i(TAG, "ChatRoomActivity// ChatMessageAdapter 생성자// list.size()=" + list.size());
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("app_cycle", "getView()");
//            Bundle extras = getIntent().getExtras();
//            my_phone = extras.getString("key_my_phone");
//            chatroom_name = extras.getString("key_room_name");

            HttpReceiveChatMessageAsyncTask task = new HttpReceiveChatMessageAsyncTask();
            task.execute();

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
        // 채팅방 글자크기 배경색변경
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

        // 메시지가 추가됐을 때, 마지막 메시지로 스크롤 --> 보류
//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                listView.setSelection(adapter.getCount() - 1);
//            }
//        });
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
        Bundle bundle = getIntent().getExtras();
        my_phone = bundle.getString("key_my_phone");
        chatroom_name = bundle.getString("key_room_name");

        Log.i("allphone_file", "ChatRoom// onCreate()// chatroom_name:" + chatroom_name);
        all_phone = getAllPhone(chatroom_name);
        lab = ChatMessageReceiveLab.getInstance();
        chatMessageList = lab.getChatMessageList();

        // 채팅방 정보 받아오기기
        Bundle chatExtras = getIntent().getExtras();
        if (chatExtras != null) {
            // 값가져오기
            name = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
//            member_phone[0] = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER); // 한명 채팅할 때 번호 값

//            Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0]);
//          member_phones = chatExtras.getStringArrayList(); // 여러명 채팅할 때 번호값 //key값:"otherPhones"
        }

//        Log.i(TAG, "chatroomactivity : member_phone :" + member_phone[0]);

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

        // todo title: 대화상대로 set
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(chatroom_name);
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

    private void onClickBtnSend() {
        String msg = writeMsg.getText().toString();
        HttpSendChatMessageAsyncTask task = new HttpSendChatMessageAsyncTask();
        task.execute(msg);
        writeMsg.clearFocus();
        writeMsg.setText("");
    }

    /** --> onClickBtnSend */
    private class HttpSendChatMessageAsyncTask extends AsyncTask<String, String, String> {
        // TODO: 2017-03-23 :: 2nd parameter :: Void

        @Override
        protected String doInBackground(String... params) {
            Log.i(TASK_CYCLE, "SendTask// doInBackground()");
            String result = sendChatMsgData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TASK_CYCLE, "SendTask// onPostExecute()// Send 완료");
            // TODO: 2017-03-23 서버에 메세지 보내기 완료 이후
            // receive 실행 task
//            HttpReceiveChatMessageAsyncTask task = new HttpReceiveChatMessageAsyncTask();
//            task.execute(); ==> 안 됨.
        }
    } // end class HttpSendChatMessageAsyncTask

    /** 채팅 메시지 보내기 */
    public String sendChatMsgData(String msg) {
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE); // 내 번호
//        String all_phone = createAllPhone(member_phone, member_phones); // 참여자 번호
//        String all_phone = "";
        Log.i(TAG, "sendChatMsgData() all_phone" + all_phone);
        Log.i("allphone_file", "sendChatMsgData() all_phone" + all_phone);

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
            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground()");
            String result = receiveChatMsgData(params[0]);

            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground() result:" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TASK_CYCLE, "ReceiveTask// onPostExecute()");

            // TODO: 2017-03-23 receive 이후
            Gson gson = new Gson();
            TypeToken<ArrayList<ChatMessageReceiveVO>> typeToken = new TypeToken<ArrayList<ChatMessageReceiveVO>>() {
            };
            Type type = typeToken.getType();
            chatMessageList = gson.fromJson(s, type);
            ChatMessageReceiveLab.getInstance().setChatMessageList(chatMessageList);
            if (!chatMessageList.isEmpty()) {
                for (ChatMessageReceiveVO vo : chatMessageList) {
                    String chatroome_name = vo.getChatroom_name();
                    String msg = vo.getMsg();
//                    Log.i(TAG, "onPostExecute()//\tChatMessageReceiveVO: " + vo.toString());
                }
            }
        }
    }// end class HttpReceiveChatMessageAsyncTask

    /** receive_info table select 해서 메세지 불러오기 */
    public String receiveChatMsgData(String nonParams/*chatroom_name*/) {
        String result = "";
        String checked = "true"; /** false */
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatReceive";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("checked", checked, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("chatroom_name", chatroom_name, ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        AndroidHttpClient androidHttpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;
        try {
            // http 통신 send
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = androidHttpClient.execute(httpPost); // 연결 실행

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
                androidHttpClient.close();
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /** 이름이 chatroom_name 인 파일에서 all_phone 읽어오기 */
    private String getAllPhone(String fileName) {
        Log.i("allphone_file", "ChatRoom// setAllPhone()// fileName:" + fileName);
        String all_phone = readFromFile(fileName).toString();

        Log.i("allphone_file", "ChatRoom// setAllPhone()// all_phone: " + all_phone);
        return all_phone;
    }

    /**
     * my_phone 얻어오기 :: 손 대지 말 것
     */
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
