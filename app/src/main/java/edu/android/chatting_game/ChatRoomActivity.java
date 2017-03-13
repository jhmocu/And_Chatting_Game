package edu.android.chatting_game;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class ChatRoomActivity
        extends AppCompatActivity implements OptionBtnFragment.optionItemSelectedListener{

    private ListView listView;
    private EditText writeMsg;
    private ImageButton btnOption, btnSend;
    private String title;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_room, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.search: break;
            case R.id.chat_room_option1: break;
            case R.id.chat_room_option2: break;
        } //end switch
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        writeMsg = (EditText) findViewById(R.id.writeMsg);
        btnOption = (ImageButton) findViewById(R.id.btnOption);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 ' + ' 버튼 이벤트 처리
                DialogFragment optionClickFragment=OptionBtnFragment.newInstance();
                optionClickFragment.show(getFragmentManager(),"optionClick_dialog");
            }
        });
        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-13 'send' 버튼 이벤트 처리
                String msg = writeMsg.getText().toString();
                Toast.makeText(ChatRoomActivity.this, "message:\n" + msg, Toast.LENGTH_SHORT).show();
            }
        });

//        MessageListFragment messageListFragment = new MessageListFragment();
//        SendMessageFragment sendMessageFragment = new SendMessageFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.sendMessageFrame, sendMessageFragment);
//        transaction.add(R.id.messageListFrame, messageListFragment);
//        transaction.commit();

        ActionBar actionBar = getSupportActionBar();
        // TODO: 2017-03-10 title: 대화상대로 set 하는 public 메소드 만들기
//        actionBar.hide();
        title = "대화방 이름";
        actionBar.setTitle(title);
    }// end onCreate()

    @Override
    public void optionItemSelected(int which) {
        switch (which){
            case 0:
                sendContact();
                break;
            case 1:

                break;

            case 2:
                ProfileSendFragment fragment = new ProfileSendFragment();
                fragment.show(getSupportFragmentManager(), "show");


//                Bundle extra = getIntent().getExtras();
//                Toast.makeText(this, "extra: " + extra, Toast.LENGTH_SHORT).show();
//                if(extra != null) {
//                    int imageId = extra.getInt(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID);
//                    String name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME);
//                    Toast.makeText(this, "imageId: " + imageId + "\n" + "name: " + name, Toast.LENGTH_SHORT).show();
//                }
                break;
        }
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
        if(resultCode == RESULT_OK)
        {
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
}
