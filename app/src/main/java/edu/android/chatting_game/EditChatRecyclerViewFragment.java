package edu.android.chatting_game;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditChatRecyclerViewFragment extends Fragment {


    private onSelectedListener listener;
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private ArrayList<ChatMessageVO> list = new ArrayList<>();
    private int count;
    private static final String KEY_CHAT_NAME = "key_chat_name";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSelectedListener){
            listener = (onSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private static final String TAG = "edu.android.chatting";
    private RecyclerView recyclerView;

    class EditChattingViewHolder extends RecyclerView.ViewHolder {

        private ImageView editChatImageRoom;
        private TextView editChatTxtRoom, editChatTxtLastMsg, editChatTxtFriendCount;
        private CheckBox editChatcheckBox;
        private int position;


        public EditChattingViewHolder(final View itemView) {
            super(itemView);

            editChatImageRoom = (ImageView) itemView.findViewById(R.id.editChatImageRoom);
            editChatTxtRoom = (TextView) itemView.findViewById(R.id.editChatTxtRoom);
            editChatTxtLastMsg = (TextView) itemView.findViewById(R.id.editChatTxtLastMsg);
            editChatTxtFriendCount = (TextView) itemView.findViewById(R.id.editChatTxtFriendCount);
            editChatcheckBox = (CheckBox) itemView.findViewById(R.id.editChatcheckBox);

            // TODO: 클릭시 checkBox가 선택되도록 설정하기
            editChatcheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editChatcheckBox.isChecked()) {
                        selectedList.set(position, true);
                        count++;
                        Log.i(TAG, "count = " + count + " selectList: " + selectedList);
                        listener.itemSelected(count, selectedList);
                    } else {
                        selectedList.set(position, false);
                        count--;
                        Log.i(TAG, "count = " + count + " selectList: " + selectedList);
                        listener.itemSelected(count, selectedList);
                    }
                }
            });
        } // end class EditChattingViewHolder
    }

    class EditChattingAdapter extends RecyclerView.Adapter<EditChattingViewHolder>{
//        public EditChattingAdapter() {
//            for (int i = 0; i < getItemCount(); i++) {
//                selectedList.add(false);
//            }
//        }
        @Override
        public EditChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.fragment_edit_chat_recycler_item, null, false);
            EditChattingViewHolder editChattingViewHolder = new EditChattingViewHolder(view);
            return editChattingViewHolder;
        }

        @Override
        public void onBindViewHolder(EditChattingViewHolder holder, int position) {
            holder.position = position;
            ChatMessageVO vo = list.get(position);
            holder.editChatTxtRoom.setText(vo.getChatroom_name());
            holder.editChatTxtLastMsg.setText(vo.getLast_msg());
            holder.editChatTxtFriendCount.setText(vo.getMember_count());
            selectedList.add(false);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public EditChatRecyclerViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_chat_recycler_view, container, false);
        list = ChatMessageLab.getInstance().getChatMessageVOList();
        recyclerView = (RecyclerView) view.findViewById(R.id.edit_chat_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EditChattingAdapter());

        return view;
    }

    public interface onSelectedListener {
        void itemSelected(int count, ArrayList<Boolean> selectedList);
    }
}
