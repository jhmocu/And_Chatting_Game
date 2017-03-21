package edu.android.chatting_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatRecyclerViewFragment extends Fragment implements ChatLongClickFragment.onItemSelectedListener {

    private static final String TAG = "edu.android.chatting";
    private RecyclerView recyclerView;
    private ArrayList<ChatMessageVO> list;

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

            // 아이템 클릭 -> ChatRoomActivity 실행
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick()\nStart------ChatRoomActivity");
                    Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                    startActivity(intent);
                }
            });

            // 롱클릭시 삭제 Dialog
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    DialogFragment chatLongClickFragment = ChatLongClickFragment.newInstance(position);
                    chatLongClickFragment.show(getChildFragmentManager(), "chatLongClickFragment");
                    return true;
                }
            });
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
        public void onBindViewHolder(ChattingViewHolder holder, int position) {
            ChatMessageVO vo = list.get(position);
            holder.txtRoom.setText(vo.getChatroom_name());
            holder.txtLastMsg.setText(vo.getLast_msg());
            holder.txtTime.setText(vo.getChat_date());
            holder.txtMsgCount.setText(String.valueOf(vo.getChecked_read()));
            holder.txtFriendCount.setText(String.valueOf(vo.getMember_count()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public ChatRecyclerViewFragment() {}

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
        list = ChatMessageLab.getInstance().getChatMessageVOList();
        Log.i("chat_list", "ChatRecyclerViewFragment// onCreateView()// list=" + list.toString());
        recyclerView = (RecyclerView) view.findViewById(R.id.chatlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ChattingAdapter());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 롱클릭시 채팅방 삭제
    @Override
    public void itemSelected(int which) {
        switch (which) {
            case 0:
                deleteChatRoom();
                break;
        }
    }

    private void deleteChatRoom() {
        // TODO: 채팅방 Delete 기능 추가

    }


}
