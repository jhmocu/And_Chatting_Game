package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ChatListFragment
        extends Fragment {

    private EditText editText;
    private FloatingActionButton floatingEditChatList;

    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_list, container, false);

        editText = (EditText) view.findViewById(R.id.editNameSearch);
        floatingEditChatList = (FloatingActionButton) view.findViewById(R.id.floatingEditChatList);

        ChatRecyclerViewFragment fragment = new ChatRecyclerViewFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_chat_recyclerView, fragment);
        transaction.commit();

        floatingEditChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditChatListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
