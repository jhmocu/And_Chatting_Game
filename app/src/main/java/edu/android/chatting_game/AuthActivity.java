package edu.android.chatting_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {
    private EditText inputPhone, inputAuth;
    private Button btnSendPhone, btnCheckedAuth;
    public int authNum;
    public String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        inputPhone = (EditText) findViewById(R.id.inputPhoneNum);
        inputAuth = (EditText) findViewById(R.id.inputAuthNum);
    }

    public void onSendAuthNum (View view){
        double r = Math.random();
        int randomInt = (int) (1000*r) + 1;
        if(randomInt > 100){
            authNum = authPhone(randomInt);
        }
    }

    public void onCheckedAuth (View view){
        if(Integer.parseInt(inputAuth.getText().toString()) == authNum){
            Toast.makeText(this, "인증 성공" , Toast.LENGTH_SHORT).show();
            startSecondActivity();
        } else {
            Toast.makeText(this, "인증 실패", Toast.LENGTH_SHORT).show();
        }

    }

    private int authPhone(int randomInt) {
        phone = inputPhone.getText().toString();
        String msg = String.valueOf(randomInt);

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, msg, null, null);

        return randomInt;
    }

    private void startSecondActivity() {
        Intent i = new Intent(this, InsertProfileActivity.class);

        i.putExtra("my_phone", phone);
        startActivity(i);

        setResult(RESULT_OK, i);
    }
}
