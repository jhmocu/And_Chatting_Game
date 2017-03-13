package edu.android.chatting_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditChatListFragment extends Fragment {



    public EditChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_chat_list, container, false);
        // TODO: 새로운 Fragment를 만들어서 EditChatListFragment에 넣어주기
        EditChatRecyclerViewFragment fragment = new EditChatRecyclerViewFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_Edit_Chat_list_Fragment, fragment);
        transaction.commit();
        return view;
    }

}
