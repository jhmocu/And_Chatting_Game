package edu.android.chatting_game;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * ChatRoom Activity 3번째(제일 밑) 프레임에 끼워질 Fragment
 * 메세지 입력 / 전송 / 옵션 (전화 걸기, 위치 전송, 연락처 불러오기 등)
 */
public class SendMessageFragment
        extends Fragment {
    /////////////
    private ArrayList<ChatMessage> messages;
    private ChatMessage chatMessage;
    /////////////


    private OnFragmentInteractionListener mListener;

    private EditText writeMsg;
    private ImageButton btnOption, btnSend;

    public SendMessageFragment() {
        // Required empty public constructor
    }

    public static SendMessageFragment newInstance(String param1, String param2) {
        SendMessageFragment fragment = new SendMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        chatMessage = new ChatMessage();
        messages = new ArrayList<>();
        //

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        writeMsg = (EditText) view.findViewById(R.id.writeMsg);
        btnOption = (ImageButton) view.findViewById(R.id.btnOption);
        btnSend = (ImageButton) view.findViewById(R.id.btnSend);

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-09 옵션버튼(+) 클릭
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017-03-09 전송버튼 클릭
                String msg = writeMsg.getText().toString();
                chatMessage.setMessage(msg);
                messages.add(chatMessage);
                Toast.makeText(getContext(), "chatMessage:\n" + msg, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
