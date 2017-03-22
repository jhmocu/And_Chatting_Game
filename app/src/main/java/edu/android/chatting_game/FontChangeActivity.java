package edu.android.chatting_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class FontChangeActivity extends Activity{

    private SeekBar seekBar;
    private TextView text;
    private Button selectBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_change);

        int size=15;
        seekBar=(SeekBar)findViewById(R.id.textsize);
        text=(TextView)findViewById(R.id.textTest);
        selectBtn=(Button)findViewById(R.id.SelectBtn);


        seekBar.setMax(30);
        seekBar.setProgress(size);
        text.setTextSize((float)15);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            text.setTextSize(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 나중에 ChatRoomActivity 에서 my_Message, your_Message 완성후 작업
                Intent intent=new Intent(FontChangeActivity.this,ChatRoomActivity.class);
                intent.putExtra("fontChange",text.getTextSize());
                finish();
            }
        });

    }
}
