package edu.android.chatting_game;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatLongClickFragment extends DialogFragment {
    public static final String ARG_KEY = "key_args";
    private onItemSelectedListener listener;

    public ChatLongClickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ChatLongClickFragment.onItemSelectedListener){
            listener = (ChatLongClickFragment.onItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static ChatLongClickFragment newInstance(int position){
        ChatLongClickFragment instance = new ChatLongClickFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_KEY, position);
        instance.setArguments(args);

        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] update = getResources().getStringArray(R.array.chat_long_click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.itemSelected(which);
                }
            }
        });
        return builder.create();
    }

    public interface onItemSelectedListener {
        void itemSelected(int which);

    }
}
