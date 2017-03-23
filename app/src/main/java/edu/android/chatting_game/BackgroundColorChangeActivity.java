package edu.android.chatting_game;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

public class BackgroundColorChangeActivity extends Activity {

    private Button btn1,btn2,btn3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color_change);

        btn1=(Button)findViewById(R.id.btngreen);
        btn2=(Button)findViewById(R.id.btnredlight);
        btn3=(Button)findViewById(R.id.btnorange);


        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int color=getColor(R.color.green);
                BackgroundChangeVO.getInstance().setColor(color);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int color=getColor(R.color.redlight);
                BackgroundChangeVO.getInstance().setColor(color);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int color=getColor(R.color.orange);
                BackgroundChangeVO.getInstance().setColor(color);
                finish();
            }
        });

    }

}
