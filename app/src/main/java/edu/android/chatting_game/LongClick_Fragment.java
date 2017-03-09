package edu.android.chatting_game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public class LongClick_Fragment
        extends DialogFragment {

    public static final String TAG = "edu.android.chatting";

    private onItemSelectedListener listener;

    public LongClick_Fragment() {
        // Required empty public constructor
    }

    public void setListener(onItemSelectedListener listener) {
        this.listener = listener;
    }

        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onItemSelectedListener) {
            this.listener = (onItemSelectedListener) context;
            Log.i(TAG, "onAttach()");
        }
    }



    public static LongClick_Fragment newInstance() {
        LongClick_Fragment longClickFragment = new LongClick_Fragment();
        return longClickFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "LongClick_Fragment :: onCreateDialog() ::\n savedInstanceState:" + savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("수정");
        final String[] update = getResources().getStringArray(R.array.long_click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "LongClick_Fragment ::: onClick() :::\nwhich: " + which);
                if (listener != null) {
                    Log.i(TAG, "LongClick_Fragment\nlistener NNNNOOOOOTTTT null\nwhich: " + which);
                    listener.itemSelected(which);
//                } else if (listener == null) {
//                    Log.i(TAG, "LongClick_Fragment\nlistener == null");
                }
            }
        });
        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        Log.i(TAG, "onDetach");
    }


    public interface onItemSelectedListener {
        void itemSelected(int which);
    }

}
