package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsRecyclerViewFragment
        extends Fragment {

    private static final String TAG = "edu.android.chatting";

    public static final String KEY_EXTRA_NAME = "key_name";
    public static final String KEY_EXTRA_PHONENUMBER = "key_phone";
    public static final String KEY_EXTRA_MESSAGE = "key_msg";
    public static final String KEY_EXTRA_IMAGEID = "key_image";
    public static final String KEY_EXTRA_NAME2 = "key_name2";

    private RecyclerView recyclerView;
    private ArrayList<Friend> list;

    class FriendViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name, message;

        public FriendViewHolder(final View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.imageView_list);
            name = (TextView) itemView.findViewById(R.id.textName_list);
            message = (TextView) itemView.findViewById(R.id.textMsg_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    startProfileActivity(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    DialogFragment longClickFragment = LongClick_Fragment.newInstance(name.getText().toString(),list.get(position).getPhone());
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
//            holder.photo.setImageResource(friend.getImageId()); /* 이미지 보류 */
//            holder.photo.setImageBitmap(friend.getPic_bitmap());
            holder.name.setText(friend.getfName());
            holder.message.setText(friend.getStatus_msg());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }// end class FriendAdapter


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "FriendsRecyclerViewFragment\tonCreateView()");

        View view = inflater.inflate(R.layout.fragment_recycler_view_friends, container, false);
        list = FriendLab.getInstance().getFriendList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FriendAdapter());
        return view;
    }

    private void startProfileActivity(int position) {
        if (position == list.size() - 1) {
            Intent intent = new Intent(getContext(), Profile_My_info.class);
            intent.putExtra(KEY_EXTRA_IMAGEID, list.get(
                    position).getPic_res());
            intent.putExtra(KEY_EXTRA_NAME, list.get(position).getfName());
            intent.putExtra(KEY_EXTRA_MESSAGE, list.get(position).getStatus_msg());
            startActivity(intent);
//            intent.putExtra(KEY_EXTRA_PHONENUMBER, list.get(position).getPhoneNumber());
        } else {
            Intent intent = new Intent(getContext(), ProfileInfoActivity.class);
            intent.putExtra(KEY_EXTRA_IMAGEID, list.get(position).getPic_res());
            intent.putExtra(KEY_EXTRA_NAME, list.get(position).getfName());
            intent.putExtra(KEY_EXTRA_PHONENUMBER, list.get(position).getPhone());
            intent.putExtra(KEY_EXTRA_MESSAGE, list.get(position).getStatus_msg());
            startActivity(intent);
        }
    }
}// end class FriendsRecyclerViewFragment