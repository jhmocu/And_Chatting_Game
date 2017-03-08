package edu.android.chatting_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        }
    } // end class ChattingViewHolder

    class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder> {

        @Override
        public ChattingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // TODO: 2017-03-08  
            
            return null;
        }

        @Override
        public void onBindViewHolder(ChattingViewHolder chattingViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
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
        return view;
    }



}
