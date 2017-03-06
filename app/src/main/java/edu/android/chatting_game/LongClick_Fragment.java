package edu.android.chatting_game;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


public class LongClick_Fragment extends DialogFragment {

    private SelectedListener listener;

    public LongClick_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectedListener){
            listener=(SelectedListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public static LongClick_Fragment newInstance(){
        return new LongClick_Fragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("수정");
        final String[]update=getResources().getStringArray(R.array.long_click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null){
                    listener.Selected(which);
                }
            }
        });
        return builder.create();
    }



    public interface SelectedListener{
        void Selected(int which);
    }

}
