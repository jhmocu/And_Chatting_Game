package edu.android.chatting_game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends Activity {

    private ExpandableListView expandableListView;
    private NoticeExpandableListViewAdapter noticeExpandableListViewAdapter;

    public List<String> parentList;
    public HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        expandableListView = (ExpandableListView) findViewById(R.id.noticeExpandableListView);

        prepareListData();

        noticeExpandableListViewAdapter = new NoticeExpandableListViewAdapter(this, parentList, listDataChild);

        expandableListView.setAdapter(noticeExpandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void prepareListData(){
        parentList = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // add parent 데이터
        parentList.add("프로젝트 진행사항");
        parentList.add("첫 번째 공지사항");

        // add child 데이터
        List<String> noticeChild_02 = new ArrayList<String>();
        noticeChild_02.add("2017.03.15 프로젝트 진행사항" + "\n" + "DB연결하기");

        List<String> noticeChild_01 = new ArrayList<String>();
        noticeChild_01.add("2017.03.06 프로젝트 시작" + "\n"
                + "이름없는데괜찮조" + "\n" + "개발자 : 목진혁, 김하진, 이도희, 이애심, 이혜진, 최상현");

        // 리스트에 데이터들을 추가
        listDataChild.put(parentList.get(0), noticeChild_02);
        listDataChild.put(parentList.get(1), noticeChild_01);
    }


}
