package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddFriendsActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_NAME = "key_name";
    public static final String KEY_EXTRA_PHONENUMBER = "key_phone";
    public static final String KEY_EXTRA_MESSAGE = "key_msg";
    public static final String KEY_EXTRA_IMAGEID = "key_image";

    private EditText editPhoneAdd;
    private Button btnAddFriend;

    private ArrayList<Friend> list = new ArrayList<Friend>();
    FriendLab lab = FriendLab.getInstance();

    Friend friend = new Friend();

    private String[] dummyGasangData = {
            "1111", "2222", "3333", "4444"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        editPhoneAdd = (EditText) findViewById(R.id.editPhoneAdd);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list = lab.getFriendList();
                String str = "";
                String phoneNo = "";
                boolean run = true;
                boolean found = false;
                while (run) {
                    for (int i = 0; i < list.size(); i++) {
                        phoneNo = list.get(i).getPhone();

                        if (editPhoneAdd.getText().toString().equals(phoneNo)) {
                            Toast.makeText(AddFriendsActivity.this, "일치\nphoneNo:" + phoneNo + "\n" +
                                    "edit:" + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
                            found = true;
                            break;
                        } // end if()

                    } //end for()
                    break;
                } // end while()
                if (!found) {
                    Toast.makeText(AddFriendsActivity.this, "불일치\nphoneNo:" + phoneNo + "\n" +
                            "edit:" + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
                } // end if()

                // dummyGasngData이용
//                while (run) {
//                    for (int i = 0; i < dummyGasangData.length; i++) {
//                        phoneNo = dummyGasangData[i];
//
//                        if (editPhoneAdd.getText().toString().equals(phoneNo)) {
//                            Toast.makeText(AddFriendsActivity.this, "일치" + "phone: " + phoneNo
//                                    + "edit: " + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
//                            found = true;
//                            // TODO: 저장된 데이터를 불러서 리싸이클러 뷰에 추가해줘서 새로운 친구를 생성
//
//                            friend.setPhoneNumber(dummyGasangData[i]);
//                            list.add(friend);
//
//                            break;
//                        }
//                    }
//                    break;
//                }
//                if (!found) {
//                    Toast.makeText(AddFriendsActivity.this, "불일치\nphoneNo:" + phoneNo + "\n" +
//                            "edit:" + editPhoneAdd.getText(), Toast.LENGTH_SHORT).show();
//                }


            } // onClick()
        }); // end setOnClickListener()
    } // end onCreate()
}
