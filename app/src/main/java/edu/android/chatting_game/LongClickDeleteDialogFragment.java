package edu.android.chatting_game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LongClickDeleteDialogFragment extends DialogFragment {

    private RecyclerView recyclerView;

    private FriendDeleteCallback callback;

    public LongClickDeleteDialogFragment() {
        // Required empty public constructor
    }

    class LongClickDeleteViewHolder extends RecyclerView.ViewHolder{

        public LongClickDeleteViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    callback.deleteFriend(position);

                }
            });
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FriendDeleteCallback) {
            callback = (FriendDeleteCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("확인");
        builder.setMessage("친구를 삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }


    public interface FriendDeleteCallback {
        void deleteFriend(int position);
    }
}
