package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFriendsFragment
        extends Fragment {
//        implements LongClick_Fragment.onItemSelectedListener {

//    public static final String KEY_EXTRA_INDEX = "key_extra";

    private RecyclerView recyclerView;
    private ArrayList<Friend> list;

    class FriendViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name;

        public FriendViewHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.imageView_recycler);
            name = (TextView) itemView.findViewById(R.id.textView_recycler);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017-03-07 position 정보 넘기기
//                    int position = getAdapterPosition();
                    Intent intent = new Intent(getContext(), ProfileInfoActivity.class);
//                    intent.putExtra(KEY_EXTRA_INDEX, position);
                    startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO: 2017-03-07 position 정보 넘기기
//                    int position = getAdapterPosition();
                    DialogFragment longClickFragment = LongClick_Fragment.newInstance();
                    longClickFragment.show(getChildFragmentManager(), "longClick_dialog");
                    return true;
                }
            });
        }
    } // end class FriendViewHolder

    class FriendAdapter
            extends RecyclerView.Adapter<FriendViewHolder> {

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View itemView = inflater.inflate(R.layout.friend_item, parent, false);
            FriendViewHolder viewHolder = new FriendViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            Friend friend = list.get(position);
            holder.photo.setImageResource(friend.getImageId());
            holder.name.setText(friend.getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view_friends, container, false);
        list = FriendLab.getInstance().getFriendList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FriendAdapter());
        return view;
    }


}