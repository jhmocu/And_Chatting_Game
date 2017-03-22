package edu.android.chatting_game;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ChatLongClickDialogFragment extends DialogFragment {
    public static final String ARG_KEY = "key_args";

    public ChatLongClickDialogFragment() {}

    public static ChatLongClickDialogFragment newInstance(int position) {
        ChatLongClickDialogFragment instance = new ChatLongClickDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_KEY, position);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] update = getResources().getStringArray(R.array.chat_long_click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = getArguments().getInt(ARG_KEY);
                DeleteChatRoomDialogFragment dlg = new DeleteChatRoomDialogFragment(position);
                dlg.show(getFragmentManager(), "delete_chatroom_dialog");
            }
        });
        return builder.create();
    }

}
