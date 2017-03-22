package edu.android.chatting_game;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteChatRoomDialogFragment extends DialogFragment {
    private int position;

    public DeleteChatRoomDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DeleteChatRoomDialogFragment(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("채팅방에서 나가시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("chat_list", "DeleteChatRoomDialogFragment// onClick()// position= " + position);
                deleteChatRoomConnect(position);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        return builder.create();
    }// end onCreateDialog()

    public void deleteChatRoomConnect(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DeleteChatRoomActivity.class);
        intent.putExtra("key_delete_position", position);
        startActivity(intent);
    }// end deleteChatRoomConnect()

}// end class DeleteChatRoomDialogFragment
