package edu.android.chatting_game;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


public class FontChangeActivity extends Activity{

    private SeekBar seekBar;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_change);

//        int size=10;
//        seekBar=(SeekBar)findViewById(R.id.textsize);
//        text=(TextView) findViewById(R.id.textMyMsg);
//
//
//        seekBar.setMax(30);
//        seekBar.setProgress(size);
//        text.setTextSize((float)10);
//
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
//                text.setTextSize(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//

    }
}
