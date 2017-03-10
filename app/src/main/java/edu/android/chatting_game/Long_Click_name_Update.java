package edu.android.chatting_game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Long_Click_name_Update extends AppCompatActivity {

    private EditText textView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long__click_name__update);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.BLUE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView=(EditText) findViewById(R.id.editName);
        btn=(Button)findViewById(R.id.button) ;
        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME2);
            textView.setText(name);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String msg=textView.getText().toString();
                Intent intent=new Intent();
                intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME2,msg);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
