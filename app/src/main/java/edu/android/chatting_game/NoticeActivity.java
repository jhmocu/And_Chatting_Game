package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private NoticeExpandableListViewAdapter noticeExpandableListViewAdapter;

    public ArrayList<String> parentList;
    public HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        expandableListView = (ExpandableListView) findViewById(R.id.noticeExpandableListView);

        prepareListData();

        noticeExpandableListViewAdapter = new NoticeExpandableListViewAdapter(this, parentList, listDataChild);

        expandableListView.setAdapter(noticeExpandableListViewAdapter);
    }

    private void prepareListData(){
        parentList = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // add parent 데이터
        parentList.add("");

    }
}
