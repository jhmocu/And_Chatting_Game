package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    public String dataPassed;


    private EditText writeMsg;
    private TextView textChatMessage;
    private ImageButton btnOption, btnSend;
    private String name, my_phone, all_phone, chatroom_name;

    private float font;

    private ListView listView;
    private MessageLab lab;
    private ArrayList<MessageVO> messageList;

    private ProfileSendFragment profileSendFragment;
    private ChatMessageAdapter chatMessageAdapter;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // ui 업데이트
            Bundle data = msg.getData();
            String jsonStr = data.getString("key_json");

            Gson gson = new Gson();
            TypeToken<ArrayList<MessageVO>> typeToken = new TypeToken<ArrayList<MessageVO>>() {
            };
            Type type = typeToken.getType();

//                    Log.i(TAG, "jsonStr: " + jsonStr);
            ////
            ArrayList<MessageVO> tempessageList = gson.fromJson(jsonStr, type);
            lab.setMessageList(tempessageList);

            tempessageList.addAll(chatMessageAdapter.list); // 임시 리스트에 Adapter에 연결한 list의 내용을 모두 담고

            chatMessageAdapter.list.clear(); // adapter에 연결한 list의 내용을 모두 지웠다가
            chatMessageAdapter.list.addAll(tempessageList); // 임시리스트의 내용을 모두 adapter에 연결한 리스트에 추가하기
            chatMessageAdapter.notifyDataSetChanged(); // 그리고 notifyDataSetChanged를 호출해보세요.

//            messageList = gson.fromJson(jsonStr, type);
//            lab.setMessageList(messageList);
//            chatMessageAdapter.notifyDataSetChanged();
//            MessageLab.getInstance().setMessageList(messageList);

//            chatMessageAdapter.setNotifyOnChange(true);
//            chatMessageAdapter.add(messageList.get(messageList.size() - 1));
//            Log.i(TAG, "handleMessage()// add(parameter:\t" + messageList.get(messageList.size() - 1));

            super.handleMessage(msg);
        }
    };

    class ChatMessageAdapter extends ArrayAdapter<MessageVO> {

        private List<MessageVO> list;
        private Context context;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, List<MessageVO> list) {
            super(context, resource);
            this.list = list;
            this.context = context;
        }

        @Override
        public void add(@Nullable MessageVO object) {
            super.add(object);
            list.add(object);
            list = messageList;
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
            Log.i("app_cycle", "getView()");
            my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
            View view = convertView;
//            if (view == null) {
                MessageVO vo = getItem(position);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (vo.getSender().equals(my_phone)) {
                    view = inflater.inflate(R.layout.right_mine, parent, false);
                } else {
                    view = inflater.inflate(R.layout.left_yours, parent, false);
                }
                textChatMessage = (TextView) view.findViewById(R.id.textChatMessage);
                textChatMessage.setText(messageList.get(position).getMsg());
//            }
            writeMsg.setCursorVisible(true);
            writeMsg.requestFocus();

            return view;
        }// end getView()
    }// end class ChatMessageAdapter


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//        final ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, messageList);
//
//
//        listView = (ListView) findViewById(R.id.chatMessageListView);
//        listView.setAdapter(adapter);
//        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        Log.i(TAG, ProfileInfoActivity.class.getName());
//        Bundle extra = getIntent().getExtras();
//        if (extra != null) {
//            int Color = extra.getInt("color");
//            listView.setBackgroundColor(Color);
//            float font = extra.getFloat("Size");
////            textChatMessage.setTextSize(font);
////            textMyMsg.setTextSize(font);
//        }
///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////


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
        Bundle bundle = getIntent().getExtras();
        my_phone = bundle.getString("key_my_phone");
        chatroom_name = bundle.getString("key_room_name");

        // TODO: 2017-03-25 주석 처리 확인
        all_phone = getAllPhone(chatroom_name);
        lab = MessageLab.getInstance();
        messageList = lab.getMessageList();

        // 채팅방 정보 받아오기
        Bundle chatExtras = getIntent().getExtras();
        if (chatExtras != null) {
            // 값가져오기
            name = chatExtras.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
        }

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

        // todo title: 대화상대로 set
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(all_phone);
//        actionBar.hide();

        chatMessageAdapter = new ChatMessageAdapter(this, -1, messageList);

        listView = (ListView) findViewById(R.id.chatMessageListView);
        listView.setAdapter(chatMessageAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Log.i(TAG, ProfileInfoActivity.class.getName());
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int Color = extra.getInt("color");
            listView.setBackgroundColor(Color);
            font = extra.getFloat("Size");
            ////            textChatMessage.setTextSize(font);
////            textMyMsg.setTextSize(font);
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String jsonStr = receiveChatMsgData("s");

                    Message msg = handler.obtainMessage();
                    Bundle data = new Bundle();
//                    data.putSerializable("key_message_list", messageList);
                    data.putString("key_json", jsonStr);
                    msg.setData(data);
                    handler.sendMessage(msg);



//                    Gson gson = new Gson();
//                    TypeToken<ArrayList<MessageVO>> typeToken = new TypeToken<ArrayList<MessageVO>>() {
//                    };
//                    Type type = typeToken.getType();

//                    Log.i(TAG, "jsonStr: " + jsonStr);
                    ////
//                    messageList = gson.fromJson(jsonStr, type);
//                    MessageLab.getInstance().setMessageList(messageList);
                    /////

//            ChatMessageReceiveLab.getInstance().setChatMessageList(chatMessageList);
//            if (!chatMessageList.isEmpty()) {
//                for (ChatMessageReceiveVO vo : chatMessageList) {
////                    String chatroome_name = vo.getChatroom_name();
//                    String msg = vo.getMsg();
//                  Log.i(TASK_CYCLE, "vo:" + vo.toString());
//                }
//            }//end if

                    ////
//                    Message msg = handler.obtainMessage();
//                    Bundle data = new Bundle();
//                    data.putSerializable("key_message_list", messageList);
////                    data.putAll("key_message_list", messageList);
//                    msg.setData(data);
//                    handler.sendMessage(msg);
                    /////

                    Log.i(TAG, "=== Thread ===");
//                    Bundle receiveBundle = getIntent().getExtras();
//                    String msg = receiveBundle.getString("msg");
//                    Log.i(TAG, "receiveBundle// getString// msg: " + msg);

                    synchronized (this) {
                        try {
                            wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }// end while
            }
        };
/** /////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
//        Thread thread = new Thread(runnable);
//        thread.start();
/** /////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

        ////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 메시지가 추가됐을 때, 마지막 메시지로 스크롤 --> 보류
//        chatMessageAdapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                listView.setSelection(chatMessageAdapter.getCount() - 1);
//            }
//        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            HttpReceiveChatMessageAsyncTask receiveTask = new HttpReceiveChatMessageAsyncTask();
            receiveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

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
        String name = null;
        String number = null;
        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);
            number = cursor.getString(1);
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
        profileSendFragment.dismiss();
    }

    private boolean onClickBtnSend() {
        String msg = writeMsg.getText().toString();
        // 1) 서버에 보내고
        if (msg != null && !msg.isEmpty()) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                HttpSendChatMessageAsyncTask task = new HttpSendChatMessageAsyncTask();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msg);
//                task.execute(msg);
            }

            // 2) ArrayList에도 추가함
            // --> receiveTask가 동시 처리된다면 필요 없는 부분.
//            chatMessageAdapter.add(new MessageVO(my_phone, msg, " String date_format "));

            chatMessageAdapter.setNotifyOnChange(true);
//            writeMsg.clearFocus();
            writeMsg.setText("");
        } // end if(msg)
        return true;
    }

    /**
     * --> onClickBtnSend
     */
    private class HttpSendChatMessageAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i(TASK_CYCLE, "SendTask// doInBackground()");
            String result = sendChatMsgData(params[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TASK_CYCLE, "ChatRoom// SendTask// onPostExecute()// Send 완료");
        }
    } // end class HttpSendChatMessageAsyncTask

    /**
     * send
     */
    public String sendChatMsgData(String msg) {
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateChatInfo";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE); // 내 번호
        Log.i(TAG, "sendChatMsgData() all_phone" + all_phone);
        Log.i("allphone_file", "sendChatMsgData() all_phone" + all_phone);

        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("all_phone", all_phone, ContentType.create("Multipart/related", "UTF-8")); //all_phone: ["01090429548"] = receiver
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

    private class HttpReceiveChatMessageAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground()");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        String result = receiveChatMsgData("s");
                        Log.i(TASK_CYCLE, "thread// run()// result: " + result);

//                        Gson gson = new Gson();
//                    TypeToken<ArrayList<MessageVO>> typeToken = new TypeToken<ArrayList<MessageVO>>() {
//                    };
//                    Type type = typeToken.getType();
//
//                    Log.i(TAG, "jsonStr: " + result);
//                    messageList = gson.fromJson(result, type);
//                    MessageLab.getInstance().setMessageList(messageList);



                        synchronized (this) {
                            try {
                                wait(2000);
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


//        @Override
//        protected String doInBackground(String... params) {
//            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground()");
//            String result = receiveChatMsgData(params[0]);
//
//
//
//
//
//            Log.i(TASK_CYCLE, "ReceiveTask// doInBackground()// result:" + result);
//            return result;
//        }

//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.i(TASK_CYCLE, "ReceiveTask// onPostExecute()");
//
//            // TODO: 2017-03-23 receive 이후
//            Gson gson = new Gson();
//            TypeToken<ArrayList<ChatMessageReceiveVO>> typeToken = new TypeToken<ArrayList<ChatMessageReceiveVO>>() {
//            };
//            Type type = typeToken.getType();
//
////            chatMessageList = gson.fromJson(s, type);
////            ChatMessageReceiveLab.getInstance().setChatMessageList(chatMessageList);
////            if (!chatMessageList.isEmpty()) {
////                for (ChatMessageReceiveVO vo : chatMessageList) {
//////                    String chatroome_name = vo.getChatroom_name();
////                    String msg = vo.getMsg();
////                  Log.i(TASK_CYCLE, "vo:" + vo.toString());
////                }
////            }//end if
//        }// end onPostExecute()
    }// end class HttpReceiveChatMessageAsyncTask


    /**
     * receive (2)
     */
    public String receiveChatMsgData(String s) {
        String result = "";
        String checked = "false";
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatTableData";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        /** 변경 */
        builder.addTextBody("table_name", chatroom_name, ContentType.create("Multipart/related", "UTF-8"));

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

        Log.i(TASK_CYCLE, "ReceiveTask// receiveChatMsgData()// result:" + result);
        return result;
    }

    /**
     * 이름이 chatroom_name 인 파일에서 all_phone 읽어오기
     */
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

        return buffer.toString();
    }

//    // Myservice에서 값 받아오기
//    public class ReceiveMyService extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            dataPassed = intent.getStringExtra("DATAPASSED");
//            Log.d("chatroom", dataPassed);
//        }
//    }

} // end class ChatRoomActivity
