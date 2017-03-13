package edu.android.chatting_game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionBtnFragment extends DialogFragment {

    private optionItemSelectedListener listener;

    public OptionBtnFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof optionItemSelectedListener){
            listener = (optionItemSelectedListener) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    public static OptionBtnFragment newInstance(){
        return new OptionBtnFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] update = getResources().getStringArray(R.array.option_Click);
        builder.setItems(update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.optionItemSelected(which);
                }
            }
        });
        return builder.create();
    }
    public interface optionItemSelectedListener {
        void optionItemSelected(int which);
    }


}
