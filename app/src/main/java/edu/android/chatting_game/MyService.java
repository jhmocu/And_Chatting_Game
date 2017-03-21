package edu.android.chatting_game;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyService extends Service implements Runnable {

    public static final String TAG_SERVICE = "service";
    public String my_phone = "";
    private ArrayList<ChatMessageReceiveVO> list = new ArrayList<>();

    // 서비스 종료시 재부팅 딜레이 시간, activity의 활성 시간 벌기 위함
    private static final int REBOOT_DELAY_TIMER = 1 * 1000;

    // 업데이트 주기
    private static final int UPDATE_DELAY = 10 * 1000;

    private Handler mHandler;
    private boolean mIsRunning;
    private int mStartId = 0;

    public MyService() {
    }

    /**
     * 컴포넌트가 bindService() 메서드를 호출하여 서비스에 바인딩할 때 호출됨
     * 바운드 서비스를 구현할 때는 이 메서드에서 IBinder 객체를 반환해야 함
     * 이 객체는 서비스가 클라이언트와 통신할 때 사용됨
     * 스타트 서비스의 경우는 이 메서드에서 null 값을 반환하도록 구현해야 함
     * */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("PersistentService", "onBind()");
        return null;
    }

    /**
     * 서비스가 생성될 때 호출됨
     * 이 메서드가 호출된 바로 다음에 onStartCommand() 메서드가 호출되거나
     * onBind() 메서드가 최초 호출됨
     * */
    @Override
    public void onCreate() {
        // 등록된 알람은 제거
        Log.d("PersistentService", "onCreate()");
        unregisterRestartAlarm();
        super.onCreate();
        mIsRunning = false;

    }

    /**
     * 서비스가 소멸될 때 호출됨
     * */
    @Override
    public void onDestroy() {
        // 서비스가 죽었을때 알람 등록
        Log.d("PersistentService", "onDestroy()");
        registerRestartAlarm();
        super.onDestroy();
        mIsRunning = false;
    }


    /**
     * @see android.app.Service#onStart(android.content.Intent, int)
     * 서비스가 시작되었을때 run()이 실행되기까지 delay를 handler를 통해서 주고 있다.
     */
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("PersistentService", "onStart()");
        super.onStart(intent, startId);
        mStartId = startId;

        // UPDATE_DELAY초 후에 시작
        mHandler = new Handler();
        mHandler.postDelayed(this, UPDATE_DELAY);
        mIsRunning = true;

    }

    /**
     * @see java.lang.Runnable#run()
     * 서비스가 돌아가고 있을때 실제로 내가 원하는 기능을 구현하는 부분
     */
    @Override
    public void run() {

        Log.e(TAG_SERVICE, "run()");

        if (!mIsRunning) {
            Log.d("PersistentService", "run(), mIsRunning is false");
            Log.d("PersistentService", "run(), alarm service end");
            return;

        } else {
            Log.d("PersistentService", "run(), mIsRunning is true");
            Log.d("PersistentService", "run(), alarm repeat after few minutes");
            function();
            mHandler.postDelayed(this, UPDATE_DELAY);
            mIsRunning = true;
        }
    }

    private void function() {
        // TODO: 2017-03-20 서비스가 실행할 일
        Log.d(TAG_SERVICE, "========================");
        Log.d(TAG_SERVICE, "function()");
        Log.d(TAG_SERVICE, "========================");

        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
        HttpReceiveAsyncTask task = new HttpReceiveAsyncTask();
        task.execute(my_phone);

    }

    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 5초 후에 서비스가 실행되도록 한다.
     */
    private void registerRestartAlarm() {

        Log.d("PersistentService", "registerRestartAlarm()");

        Intent intent = new Intent(MyService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER; // 5초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, REBOOT_DELAY_TIMER, sender);
    }

    /**
     * 기존 등록되어 있는 알람을 해제
     */
    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(MyService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

    private class HttpReceiveAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG_SERVICE, "doInBackground");
            String result = receiveData(params[0]);
            Log.i(TAG_SERVICE, result);
            Gson gson = new Gson();
            TypeToken<ArrayList<ChatMessageReceiveVO>> typeToken = new TypeToken<ArrayList<ChatMessageReceiveVO>>() {};
            Type type = typeToken.getType();
            Log.i(TAG_SERVICE, "MyService// onPostExecute()// String s" + result);
            list = gson.fromJson(result, type);
            Log.i(TAG_SERVICE, "MyService// onPostExecute()// list" + list.toString());
            for(int i = 0; i <list.size(); i++) {
                ChatMessageReceiveVO vo = list.get(i);
                Log.i(TAG_SERVICE, "MyService// for(list)// 채팅방" + vo.getChecked());
                getMessage(getApplicationContext(), vo);
                updateData(vo.getMy_phone(), vo.getChatroom_name());
            }
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String receiveData(String my_phone){
        String result = "";
        String checked = "true";
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatReceive";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        Log.i(TAG_SERVICE, my_phone);
        // 데이터 넣는 부분
        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("checked", checked, ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;

        try {
            // http 통신 send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost); // 연결 실행

            // http 통신 receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            Log.i(TAG_SERVICE, "업데이트 연결 성공");
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferdReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String updateData(String my_phone, String chatroom_name){
        String result = "";
        String checked = "false";
        String requestURL = "http://192.168.11.11:8081/Test3/UpdateChatReceive";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        Log.i(TAG_SERVICE, my_phone);
        // 데이터 넣는 부분
        builder.addTextBody("my_phone", my_phone, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("checked", checked, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("chatroom_name", chatroom_name, ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;

        try {
            // http 통신 send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost); // 연결 실행

            // http 통신 receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            Log.i(TAG_SERVICE, "연결 성공");
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferdReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
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
            while (line != null) {
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
        return buffer.toString();
    }

    private void getMessage(Context context, ChatMessageReceiveVO vo) {
        // TODO: 2017-03-20 메세지가 도착하면 상태바에 알림 띄우기
        // 알림을 띄우기 위해 서비스 불러옴
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity/** OR getService() OR getBroadcastReceiver() */
                (context, 0, new Intent(context,
                                ChatRoomActivity.class/**알림 터치했을 때 호출할 액티비티*/
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification
                = new Notification.Builder(context)
                .setContentTitle(vo.getChat_member())
                .setContentText(vo.getMsg())
                .setTicker("알림 발생시 잠깐 나오는 텍스트 - 실제 디바이스에서만 나옴")
                .setSmallIcon(R.drawable.p2)
                .setContentIntent(contentIntent)/**알림 터치시 실행할 작업 인텐트*/
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) /**알림 발생시 진동, 사운드등 설정*/
                .setAutoCancel(true)/**알림 터치시 자동 삭제*/
                .setPriority(Notification.PRIORITY_MAX)/**헤드업 알림*/
                .build();

        // Send the notification.
        notificationManager.notify(22, notification);
    }

} // end class MyService






















