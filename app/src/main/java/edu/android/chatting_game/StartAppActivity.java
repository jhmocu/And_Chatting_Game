package edu.android.chatting_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StartAppActivity extends AppCompatActivity {
    public static final String MY_PHONE_FILE = "my_phone.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        Log.i("gg", this.getFileStreamPath(MY_PHONE_FILE).exists()+"");

        // 파일이 있을 경우
        if(!this.getFileStreamPath(MY_PHONE_FILE).exists()) {
            createFile();
        }
        Thread timeThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    // 시작할 때 개인의 번호가 있는지 없는지 확인
                    String my_phone = readFromFile(MY_PHONE_FILE);
                    if(!my_phone.equals("")){

                        Intent intent = new Intent(StartAppActivity.this, Main2Activity.class);
                        startActivity(intent);

                        setResult(RESULT_OK, intent);

                    } else {

                        Intent intent = new Intent(StartAppActivity.this, AuthActivity.class);
                        startActivity(intent);

                        setResult(RESULT_OK, intent);

                    }

                }
            }
        };
        timeThread.start();
    }

    public String readFromFile(String filename) {
        // 파일에서 읽은 문자열을 append할 변수
        StringBuffer buffer = new StringBuffer();

        InputStream in = null; // file input stream
        InputStreamReader reader = null; // 인코딩된 문자열을 읽기 위해서
        BufferedReader br = null; //

        try {
            in = openFileInput(filename);
            reader = new InputStreamReader(in);
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null){
                buffer.append(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("gg", buffer.toString());
        return  buffer.toString();
    }

    private void createFile() {
        OutputStream out = null; // file output stream
        try {
            out = openFileOutput(MY_PHONE_FILE, MODE_PRIVATE);
            Toast.makeText(this, "파일 생성 성공", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } // end writeToFile()
}
