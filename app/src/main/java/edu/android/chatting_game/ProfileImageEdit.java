package edu.android.chatting_game;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileImageEdit extends Fragment {

    private static final int PICK_FROM_ALBUM = 100;

    private File photoFile;
    private ImageView imageView;
    private Button button;

    public ProfileImageEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_image_edit, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        button = (Button) view.findViewById(R.id.btnEdit);

//        Bundle extra = getArguments();
//        String imageString = extra.getString("image");
//        int image = Integer.parseInt(imageString);
//        imageView.setImageResource(image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumSelect();

            }
        });
        return view;
    }
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            Log.i("이미지 경로", uri.toString());
            imageView.setImageURI(uri);
        }
    }
}

