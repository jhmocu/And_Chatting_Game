package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class EditChatListActivity extends AppCompatActivity
        implements EditChatRecyclerViewFragment.onSelectedListener {
    private static final String TAG = "edu.android.chatting";
    private Button btnBack, btnEditChatFinish;
    private TextView textEditChatCount;
    private CheckBox editChatcheckBox;
    private int count;
    private ArrayList<Boolean> selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chat_list);
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.frameEditChatList);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnEditChatFinish = (Button) findViewById(R.id.btnEditChatFinish);
        textEditChatCount = (TextView) findViewById(R.id.textEditChatCount);
        editChatcheckBox = (CheckBox) findViewById(R.id.editChatcheckBox);
        textEditChatCount.setText(String.valueOf(count));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }); // 편집이라는 부분을 누르면 다시 이전 액티비티로 돌아가는 기능

        if (frag == null) {
            frag = new EditChatListFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.frameEditChatList, frag);
            transaction.commit();
        } // RecyclerView List를 띄어줌

        btnEditChatFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "selected: " + selectedList.toString());
                String position = selectedList.toString();
                    setResult(RESULT_OK, getIntent());
                    finish();
                    // FAB에서는 startActivityForResult() 호출
                    // setResult()...
                    // finish()

            }
        });


    }

    private void setResult() {

    }

    @Override
    public void itemSelected(int count, ArrayList<Boolean> selectedList) {
        textEditChatCount.setText(String.valueOf(count));
        this.selectedList = selectedList;
    }
}
