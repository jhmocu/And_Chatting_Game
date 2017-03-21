package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

public class ChatListFragment
        extends Fragment {

    private EditText editText;
    private FloatingActionButton floatingEditChatList, floatingBtnChatAdd, floatingBtnBase;
    private boolean isFABOpen;
    public static final int REQ_CODE_EDIT_CHAT = 444;
    public static final int REQ_CODE_ADD_CHAT = 555;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        editText = (EditText) view.findViewById(R.id.editNameSearch);
        floatingEditChatList = (FloatingActionButton) view.findViewById(R.id.floatingEditChatList);
        floatingBtnChatAdd = (FloatingActionButton) view.findViewById(R.id.floatingBtnChatAdd);
        floatingBtnBase = (FloatingActionButton) view.findViewById(R.id.floatingBtnBase);

        ChatRecyclerViewFragment fragment = new ChatRecyclerViewFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_chat_recyclerView, fragment);
        transaction.commit();

        floatingBtnBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        floatingBtnChatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddChatActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_CHAT);
            }
        });

        floatingEditChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditChatListActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_CHAT);

            }
        });


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

//    private void startActivityForResult(int requestCode, int resultcode, Intent intent) {
//        if (requestCode == REQ_CODE_EDIT_CHAT && resultcode == Activity.RESULT_OK) {
//            intent = new Intent(getActivity(), EditChatListActivity.class);
//        }
//    }

    private void showFABMenu(){
        isFABOpen = true;
        floatingBtnChatAdd.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        floatingEditChatList.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu(){
        isFABOpen = false;
        floatingBtnChatAdd.animate().translationY(0);
        floatingEditChatList.animate().translationY(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // EditCahtListActivity가 보내준 ArrayList를 가지고
        // true로 된 위치의 아이템들을 삭제
    }
}
