package edu.android.chatting_game;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity
        extends AppCompatActivity {

    public static final String TAG = "edu.android.chatting";

    private EditText writeMsg;
    private TextView textMyMsg;
    private ImageButton btnOption, btnSend;
    private String title;

    private ListView listView;
    private ChatMessageLab lab;
    private ArrayList<ChatMessage> chatMessageArrayList;


    class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

        private List<ChatMessage> list;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessage> objects) {
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

            return view;
        }// end getView()
    }// end class ChatMessageAdapter

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

        Log.i(TAG, "onCreateOptionsMenu()");

        final ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, chatMessageArrayList);
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
        chatMessageArrayList = lab.getChatMessageList();

//        ChatMessageAdapter adapter = new ChatMessageAdapter(this, -1, chatMessageArrayList);
//        listView = (ListView) findViewById(R.id.chatMessageListView);
//        listView.setAdapter(adapter);
//        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        writeMsg = (EditText) findViewById(R.id.writeMsg);
        btnOption = (ImageButton) findViewById(R.id.btnOption);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 ' + ' 버튼 이벤트 처리 
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 'send' 버튼 이벤트 처리
                String msg = writeMsg.getText().toString();
                ChatMessage chatMessage = new ChatMessage(msg);
                chatMessageArrayList = ChatMessageLab.getInstance().getChatMessageList();
                chatMessageArrayList.add(chatMessage);

//                writeMsg.setEnabled(true);
                writeMsg.clearFocus();
                writeMsg.setText("");
            }
        });


        // TODO: 2017-03-10 title: 대화상대로 set 하는 public 메소드 만들기
        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        title = "대화방 이름";
        actionBar.setTitle(title);
    }// end onCreate()
} // end class ChatRoomActivity
