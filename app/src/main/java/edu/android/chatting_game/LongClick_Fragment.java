package edu.android.chatting_game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class LongClick_Fragment
        extends DialogFragment {

    private onItemSelectedListener listener;
    private String name;

    public LongClick_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof onItemSelectedListener){
            listener = (onItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static LongClick_Fragment newInstance(String name){
        LongClick_Fragment frag = new LongClick_Fragment();
        frag.name = name;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] update = getResources().getStringArray(R.array.long_click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.itemSelected(which, name);

                }
            }
        });
        return builder.create();
    }


    public interface onItemSelectedListener {
        void itemSelected(int which, String name);
    }

}
