package edu.android.chatting_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2017-03-14.
 */

public class NoticeExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> parentList;
    private HashMap<String, List<String>> childList;

    // 생성자 생성
    public NoticeExpandableListViewAdapter(Context context, ArrayList<String> parentList, HashMap<String, List<String>> childList) {
        this.context = context;
        this.parentList = parentList;
        this.childList = childList;
    }

    // ParentListView에 대한 method
    @Override
    public int getGroupCount() { // ParentListView의 원소 개수를 반환
        return parentList.size();
    }

    @Override
    public Object getGroup(int groupPosition) { // ParentListView의 position을 받아 해당 TextView에 반영될 String을 반환
        return parentList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { // ParentListView의 View
        if(convertView == null){
            LayoutInflater groupInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentListView의 layout연결, root로 grgument 중 parent를 받으면 root로 고정하지는 않음
            convertView = groupInflater.inflate(R.layout.parent_listview, parent, false);
        }

        // ParentListView의 layout연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView) convertView.findViewById(R.id.parentText);
        parentText.setText(getGroup(groupPosition).toString());
        return convertView;
    }

    // ChildListView에 대한 method
    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long형값을 반환
        return childPosition;
    }

    @Override
    public boolean hasStableIds() { // stable ID인지 boolean 값으로 반환
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View
        // ParentList의 View를 얻을 때와 비슷하게 layout연결 후, layout 내 TextView를 연결
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater childInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInflater.inflate(R.layout.child_listview, null);
        }

        TextView childListText = (TextView) convertView.findViewById(R.id.childText);
        childListText.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { // groupPosition과 childPosition을 통해 childList의 원소를 얻어옴
        return this.childList.get(this.parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return this.childList.get(this.parentList.get(groupPosition)).size();
    }
}
