package edu.android.chatting_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChatRecyclerViewFragment
        extends Fragment {


    public ChatRecyclerViewFragment() {

    }


    public static ChatRecyclerViewFragment newInstance(String param1, String param2) {
        ChatRecyclerViewFragment fragment = new ChatRecyclerViewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_recycler_view, container, false);
        return view;
    }

}
