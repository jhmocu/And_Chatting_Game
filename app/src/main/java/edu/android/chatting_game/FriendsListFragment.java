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
public class FriendsListFragment
        extends Fragment {


    public FriendsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_friends_list, container, false);
        FriendsRecyclerViewFragment fragment = new FriendsRecyclerViewFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_recyclerView, fragment);
        transaction.commit();

        return view;
    }

}
