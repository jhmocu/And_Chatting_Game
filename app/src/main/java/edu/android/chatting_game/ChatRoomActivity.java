package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity
        extends AppCompatActivity implements OptionBtnFragment.optionItemSelectedListener, ProfileSendFragment.ProfileSendCallback {

    public static final String TAG = "edu.android.chatting";

    private EditText writeMsg;
    private TextView textMyMsg;
    private ImageButton btnOption, btnSend;
    private String title;

    private ListView listView;
    private ChatMessageLab lab;
    private ArrayList<ChatMessageVO> chatMessageVOArrayList;

    private Uri uri;
    private ProfileSendFragment profileSendFragment;


    class ChatMessageAdapter extends ArrayAdapter<ChatMessageVO> {

        private List<ChatMessageVO> list;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessageVO> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i(TAG, "getView()");
            View view = convertView;
//            if(내 메세지){
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.content_my_message, parent, false);
            }
            textMyMsg = (TextView) view.findViewById(R.id.textMyMsg);
            textMyMsg.setText(list.get(position).getMessage());
//            } else if (상대 메세지) {
//                    view = LayoutInflater.from(getContext()).inflate(R.layout.content_your_message, parent, false);
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

        Log.i(TAG, "onCreateOptionsMenu()");

        final ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, chatMessageVOArrayList);
        listView = (ListView) findViewById(R.id.chatMessageListView);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


//         TODO: 2017-03-13 메시지가 추가됐을 때, 마지막 메시지로 스크롤 --> 보류
//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                listView.setSelection(adapter.getCount()-1);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Log.i(TAG, "onCreate()");

        lab = ChatMessageLab.getInstance();
        chatMessageVOArrayList = lab.getChatMessageVOList();

//        ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, chatMessageVOArrayList);
//        listView = (ListView) findViewById(R.id.chatMessageListView);
//        listView.setAdapter(adapter);
//        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        writeMsg = (EditText) findViewById(R.id.writeMsg);
        btnOption = (ImageButton) findViewById(R.id.btnOption);
        btnSend = (ImageButton) findViewById(R.id.btnSend);

        //writeMsg(editText) 클릭하기 전에 키보드 숨기기
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 ' + ' 버튼 이벤트 처리
                DialogFragment optionClickFragment = OptionBtnFragment.newInstance();
                optionClickFragment.show(getFragmentManager(), "optionClick_dialog");
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 'send' 버튼 이벤트 처리
                /**
                 // 연결 가능한 네트워크 자원이 있는 지 체크
                 ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                 NetworkInfo info = connMgr.getActiveNetworkInfo();
                 if (info != null && info.isAvailable()) {
                 Log.i(TAG, info.getTypeName() + "사용 가능");

                 String message = writeMsg.getText().toString();
                 Log.i(TAG, "message: " + message);

                 ChatMessageVO chatMessageVO = new ChatMessageVO(message);
                 HttpSendMessageAsyncTask task = new HttpSendMessageAsyncTask();
                 task.execute(chatMessageVO);
                 }*/
                String msg = writeMsg.getText().toString();
                ChatMessageVO chatMessage = new ChatMessageVO(msg);
                chatMessageVOArrayList = ChatMessageLab.getInstance().getChatMessageVOList();
                chatMessageVOArrayList.add(chatMessage);
                writeMsg.clearFocus();
                writeMsg.setText("");
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String map = extras.getString(MapsActivity.EXTRA_MAP);
            writeMsg.setText(map);
        }

        // TODO: 2017-03-10 title: 대화상대로 set 하는 public 메소드 만들기
        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        title = "대화방 이름";
        actionBar.setTitle(title);
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
        // TODO: 2013-03-13 플러스 버튼 연락처 보내기.
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

/***********************************************************************************************************************************************************************************/
    /** 서버 연결 테스트
     private class HttpSendMessageAsyncTask
     extends AsyncTask<ChatMessageVO, String, String> {
    @Override protected String doInBackground(ChatMessageVO... params) {
    Log.i(TAG, "연결되었다");
    String result = sendData(params[0]);
    return result;
    }

    @Override protected void onPostExecute(String s) {
    super.onPostExecute(s);
    textMyMsg.setText(s);
    }
    }// end class HttpSendMessageAsyncTask

     private String sendData(ChatMessageVO vo) {
     String requestURL = "http://192.168.11.11:8081/Test3/InsertProfile";
     String result = "";
     MultipartEntityBuilder builder = MultipartEntityBuilder.create();
     builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
     builder.addTextBody("phone", vo.getStatusMessage(), ContentType.create("Multipart/related", "UTF-8")); //"phone"
     builder.addTextBody("name", "이름", ContentType.create("Multipart/related", "UTF-8"));
     builder.addTextBody("pic_res", "아이디", ContentType.create("Multipart/related", "UTF-8"));
     builder.addTextBody("status_msg", "상태메세지", ContentType.create("Multipart/related", "UTF-8"));
     builder.addTextBody("friend_count", "0", ContentType.create("Multipart/related", "UTF-8"));

     InputStream inputStream = null;
     HttpClient httpClient = null; //
     HttpPost httpPost = null; //new HttpPost(requestURL);
     HttpResponse httpResponse = null;

     // http 통신 send
     try {
     httpClient = AndroidHttpClient.newInstance("Android");
     httpPost = new HttpPost(requestURL);
     httpPost.setEntity(builder.build());
     httpResponse = httpClient.execute(httpPost); // 연결 실행

     // http 통신 receive
     HttpEntity httpEntity = httpResponse.getEntity();
     inputStream = httpEntity.getContent();

     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
     StringBuffer stringBuffer = new StringBuffer();
     String line = bufferedReader.readLine();
     while (line != null) {
     stringBuffer.append(line + "\n");
     line = bufferedReader.readLine();
     }
     result = stringBuffer.toString();
     } catch (IOException e) {
     e.printStackTrace();
     } finally {
     try {
     inputStream.close();
     httpPost.abort();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }
     return result;
     }

     //    public String getPathFromUri(Uri uri) {
     //        String[] filePathColumn = {MediaStore.Images.Media.DATA};
     //        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
     //        cursor.moveToNext();
     //        String path = cursor.getString(cursor.getColumnIndex("_data"));
     //        cursor.close();
     //        return path;
     //    }
     */

} // end class ChatRoomActivity
