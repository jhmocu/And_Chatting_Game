package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private Button btnNotice, btnFontChange, btnBackgroundColorChange, btnVersion;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btnNotice = (Button) view.findViewById(R.id.btnNotice);
        btnFontChange = (Button) view.findViewById(R.id.btnFontChange);
        btnBackgroundColorChange = (Button) view.findViewById(R.id.btnBackgroundColorChange);
        btnVersion = (Button) view.findViewById(R.id.btnVersion);

        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        }); // end btnNotice.setOnClickListener()

        btnFontChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FontChangeActivity.class);
                startActivity(intent);
            }
        }); // end btnFontChange.setOnClickListener()

        btnBackgroundColorChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackgroundColorChangeActivity.class);
                startActivity(intent);
            }
        }); // end btnBackgroundColorChange.setOnClickListener()

        btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VersionActivity.class);
                startActivity(intent);
            }
        }); // end btnVersion.setOnClickListener()
        return view;
    } // end onCreateView()

}
