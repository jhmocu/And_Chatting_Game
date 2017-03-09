package edu.android.chatting_game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Long_Click_name_Update extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long__click_name__update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView=(TextView)findViewById(R.id.editName);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String name = extra.getString(FriendsRecyclerViewFragment.KEY_EXTRA_NAME2);
            textView.setText(name);
        }
    }

}
