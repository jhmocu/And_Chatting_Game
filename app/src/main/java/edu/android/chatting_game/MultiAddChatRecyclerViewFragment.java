package edu.android.chatting_game;


import android.content.Context;
import android.net.Uri;
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

import it.sephiroth.android.library.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MultiAddChatRecyclerViewFragment extends Fragment {

    private MultiAddChatSendProfile callback;
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private ArrayList<Friend> list = new ArrayList<>();
    private int count;
    private String name;
    private String phone;
    private ArrayList<Integer> positions = new ArrayList<>();
    private static final String KEY_MULTI_ADD_CHAT = "key_";
    private static final String TAG = "edu.android.chatting";

    private RecyclerView recyclerView;

    public MultiAddChatRecyclerViewFragment() {
        // Required empty public constructor
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
            Friend vo = list.get(position);
            Picasso.with(holder.itemView.getContext()).load(Uri.parse(vo.getPic_url())).resize(100, 100).centerCrop().into(holder.imageView);
            holder.textName.setText(vo.getfName());
            holder.textPhone.setText(vo.getPhone());
            selectedList.add(false);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    class MultiAddViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textName, textPhone, multiAddChatCount;
        private CheckBox multiChatBox;
        public int position;

        public MultiAddViewHolder(final View itemView){
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.MultiAddChatProfileImageView);
            textName = (TextView) itemView.findViewById(R.id.multiAddNameText);
            textPhone = (TextView) itemView.findViewById(R.id.multiAddPhoneText);
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
                        position=getAdapterPosition();
                        positions.add(position);
                        Log.i(TAG,"체크하면 position : " + position);
                        callback.multichatsendprofile(name, phone, count, selectedList, positions);
                    }else {
                        selectedList.set(position, false);
                        count--;
                        callback.multichatsendprofile(name, phone, count, selectedList, positions);
                    }
                }
            });
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
        void multichatsendprofile(String name, String phone, int count, ArrayList<Boolean> selectedList, ArrayList<Integer> positions);
    }

}
