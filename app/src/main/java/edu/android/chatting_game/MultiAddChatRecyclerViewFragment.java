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
public class MultiAddChatRecyclerViewFragment extends Fragment {

    private MultiAddChatSendProfile callback;
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private ArrayList<Friend> list = new ArrayList<>();
    private int count;

    private static final String KEY_MULTI_ADD_CHAT = "key_";
    private static final String TAG = "edu.android.chatting";

    private RecyclerView recyclerView;

    public MultiAddChatRecyclerViewFragment() {
        // Required empty public constructor
    }

    class MultiAddViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView name, phone, multiAddChatCount;
        private CheckBox multiChatBox;
        private int position;

        public MultiAddViewHolder(final View itemView){
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.MultiAddChatProfileImageView);
            name = (TextView) itemView.findViewById(R.id.multiAddNameText);
            phone = (TextView) itemView.findViewById(R.id.multiAddPhoneText);
            multiAddChatCount = (TextView) itemView.findViewById(R.id.textCount);
            multiChatBox = (CheckBox) itemView.findViewById(R.id.checkBoxMultiAddChat);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    callback.multichatsendprofile(position, selectedList);
//                }
//            });

            multiChatBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(multiChatBox.isChecked()){
                        selectedList.set(position, true);
                        count++;
                        Log.i(TAG, "count = " + count);
                        callback.multichatsendprofile(position, count, selectedList);
                    }else {
                        selectedList.set(position, false);
                        count--;
                        callback.multichatsendprofile(position, count, selectedList);
                    }
                }
            });
        }
    }


    class MultiAddChatAdapter extends RecyclerView.Adapter<MultiAddViewHolder>{

        @Override
        public MultiAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.multi_add_friends_item, null, false);
            MultiAddViewHolder multiAddViewHolder = new MultiAddViewHolder(view);
            return multiAddViewHolder;
        }

        @Override
        public void onBindViewHolder(MultiAddViewHolder holder, int position) {
            Friend friend = list.get(position);
            holder.name.setText(friend.getfName());
            holder.phone.setText(friend.getPhone());
            selectedList.add(false);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MultiAddChatSendProfile){
            callback = (MultiAddChatSendProfile) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multi_add_chat_recycler_view, container, false);
        list = FriendLab.getInstance().getFriendList();
        recyclerView = (RecyclerView) view.findViewById(R.id.multi_Add_Chat_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MultiAddChatAdapter());

        return view;
    }

    public interface MultiAddChatSendProfile{
        void multichatsendprofile(int position, int count, ArrayList<Boolean> selectedList);
    }

}
