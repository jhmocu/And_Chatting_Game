package edu.android.chatting_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditChatRecyclerViewFragment extends Fragment {

    private static final String TAG = "edu.android.chatting";
    private RecyclerView recyclerView;

    class EditChattingViewHolder extends RecyclerView.ViewHolder {

        private ImageView editChatImageRoom;
        private TextView editChatTxtRoom, editChatTxtLastMsg, editChatTxtFriendCount;
        private CheckBox editChatcheckBox;

        public EditChattingViewHolder(View itemView) {
            super(itemView);

            editChatImageRoom = (ImageView) itemView.findViewById(R.id.editChatImageRoom);
            editChatTxtRoom = (TextView) itemView.findViewById(R.id.editChatTxtRoom);
            editChatTxtLastMsg = (TextView) itemView.findViewById(R.id.editChatTxtLastMsg);
            editChatTxtFriendCount = (TextView) itemView.findViewById(R.id.editChatTxtFriendCount);
            editChatcheckBox = (CheckBox) itemView.findViewById(R.id.editChatcheckBox);

            // TODO: 클릭시 checkBox가 선택되도록 설정하기
        } // end class EditChattingViewHolder
    }


    class EditChattingAdapter extends RecyclerView.Adapter<EditChattingViewHolder>{

        @Override
        public EditChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.fragment_edit_chat_recycler_item, null, false);
            EditChattingViewHolder editChattingViewHolder = new EditChattingViewHolder(view);
            return editChattingViewHolder;
        }

        @Override
        public void onBindViewHolder(EditChattingViewHolder editChattingViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public EditChatRecyclerViewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_chat_recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.edit_chat_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EditChattingAdapter());
        return view;
    }

}
