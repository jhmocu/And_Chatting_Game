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
import android.widget.Toast;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSendFragment extends DialogFragment {

    private ArrayList<Friend> list;
    private RecyclerView recyclerView;
    int position;

    public ProfileSendFragment() {
        // Required empty public constructor
    }

        class ProfileSendViewHolder extends RecyclerView.ViewHolder {

            private ImageView image;
            private TextView name;

            public ProfileSendViewHolder(final View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.imageView_list1);
                name = (TextView) itemView.findViewById(R.id.textName_list1);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = getAdapterPosition();
                        profileSend(position);

                    }
                });

            }
        }

        class ProfileSendAdapter extends RecyclerView.Adapter<ProfileSendViewHolder> {
            @Override
            public ProfileSendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View itemView = inflater.inflate(R.layout.profile_item, parent, false);
                ProfileSendViewHolder viewHolder = new ProfileSendViewHolder(itemView);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(ProfileSendViewHolder holder, int position) {
                Friend friend = list.get(position);
                holder.image.setImageResource(friend.getImageId());
                holder.name.setText(friend.getName());
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_send, container, false);
        list = FriendLab.getInstance().getFriendList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_container1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProfileSendAdapter());
        return view;
    }

    private void profileSend(int position) {
        Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_IMAGEID, list.get(position).getImageId());
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME, list.get(position).getName());
        startActivity(intent);
    }


}
