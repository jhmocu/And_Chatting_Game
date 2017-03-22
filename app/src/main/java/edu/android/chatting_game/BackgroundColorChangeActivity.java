package edu.android.chatting_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BackgroundColorChangeActivity extends Activity {

    private Button btn1,btn2,btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);

        btn1=(Button)findViewById(R.id.btngreen);
        btn2=(Button)findViewById(R.id.btnredlight);
        btn3=(Button)findViewById(R.id.btnorange);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BackgroundColorChangeActivity.this,ChatRoomActivity.class);
                intent.putExtra("Background",getColor(R.id.btngreen));
                startService(intent);
                finish();
            }
        });

    }

}
