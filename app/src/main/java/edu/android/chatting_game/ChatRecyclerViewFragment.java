package edu.android.chatting_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatRecyclerViewFragment
        extends Fragment {

    private RecyclerView recyclerView;

    class ChattingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtRoom, txtLastMsg, txtFriendCount, txtTime, txtMsgCount;

        public ChattingViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageRoom);
            txtRoom = (TextView) itemView.findViewById(R.id.txtRoom);
            txtLastMsg = (TextView) itemView.findViewById(R.id.txtLastMsg);
            txtFriendCount = (TextView) itemView.findViewById(R.id.txtFriendCount);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMsgCount = (TextView) itemView.findViewById(R.id.txtMsgCont);

        }
    } // end class ChattingViewHolder

    class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder> {

        @Override
        public ChattingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // TODO: 2017-03-08 null 확인  
            View view = inflater.inflate(R.layout.fragment_chat_recycler_item, null, false);
            ChattingViewHolder chattingViewHolder = new ChattingViewHolder(view);
            return chattingViewHolder;
        }

        @Override
        public void onBindViewHolder(ChattingViewHolder chattingViewHolder, int i) {
            // TODO: 2017-03-08
        }

        @Override
        public int getItemCount() {
            return 10; /* 임의 --> list.size()*/
        }
    }


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
        recyclerView = (RecyclerView) view.findViewById(R.id.chatlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ChattingAdapter());

        return view;
    }



}
