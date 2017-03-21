package edu.android.chatting_game;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class MyService extends Service implements Runnable {

    public static final String TAG_SERVICE = "service";

    // 서비스 종료시 재부팅 딜레이 시간, activity의 활성 시간 벌기 위함
    private static final int REBOOT_DELAY_TIMER = 5 * 1000;

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


} // end class MyService






















