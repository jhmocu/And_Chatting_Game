package edu.android.chatting_game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment
        extends Fragment {
    public static final String TAG = "edu.android.chatting";

    ListView listView;
    ArrayList<ChatMessage> chatMessageArrayList;

    class ChatMessageAdapter
            extends ArrayAdapter<ChatMessage> {

        List<ChatMessage> messages;

        public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessage> objects) {
            super(context, resource, objects);
            this.messages = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.content_my_message, parent, false);

            TextView textView = (TextView) view.findViewById(R.id.textMyMsg);
            textView.setText(messages.get(position).getMessage());

            return view;
        }
    }// end class ChatMessageAdaper

    public MessageListFragment() {
        // Required empty public constructor
    }

    public static MessageListFragment newInstance(String param1, String param2) {
        MessageListFragment fragment = new MessageListFragment();
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
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        chatMessageArrayList = new ArrayList<>();
//        chatMessageArrayList = ChatMessage.ChatMessageLab.ge

        listView = (ListView) view.findViewById(R.id.messageListView);
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(getContext(), -1, chatMessageArrayList);
        listView.setAdapter(chatMessageAdapter);

        Log.i(TAG, "chatMessageList" + chatMessageArrayList.get(0).toString());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
