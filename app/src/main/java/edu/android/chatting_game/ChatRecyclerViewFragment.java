package edu.android.chatting_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatRecyclerViewFragment extends Fragment {

    private static final String TAG = "edu.android.chatting";
    private RecyclerView recyclerView;
    private ArrayList<ChatMessageVO> list;
    private int listPosition;

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
                    Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                    startActivity(intent);
                }
            });

            // 롱클릭시 삭제 Dialog
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listPosition = getAdapterPosition();
                    DialogFragment chatLongClickFragment = ChatLongClickDialogFragment.newInstance(listPosition);
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
    public ChatRecyclerViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_recycler_view, container, false);
        list = ChatMessageLab.getInstance().getChatMessageVOList();
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

}// end class ChatRecyclerViewFragment






















