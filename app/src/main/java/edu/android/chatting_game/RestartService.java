package edu.android.chatting_game;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by stu on 2017-03-20.
 */

public class RestartService extends BroadcastReceiver {
    public static final String ACTION_RESTART_PERSISTENTSERVICE
            = "ACTION.Restart.PersistentService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartService", "RestartService called!");
        // 리시버가 뭔가를 받았을 때
        /***/
        getMessage(context, intent);
        /***/

        /* 서비스 죽일때 알람으로 다시 서비스 등록 */
        if (intent.getAction().equals(ACTION_RESTART_PERSISTENTSERVICE)) {
            Log.d("RestartService", "Service dead, but resurrection");

            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }

        /* 폰 재부팅할때 서비스 등록 */
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("RestartService", "ACTION_BOOT_COMPLETED");

            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }
    }
    private void getMessage(Context context, Intent intent) {
        // TODO: 2017-03-20 메세지가 도착하면 상태바에 알림 띄우기
        // 알림을 띄우기 위해 서비스 불러옴
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

//                SharedPreferences shrdPref = PreferenceManager.getDefaultSharedPreferences(getContext());
//                // 상태바에 알림을 표시할 건지
//                boolean isNoti = shrdPref.getBoolean("noti", true);
//                // 소리로 알릴건지
//                boolean isSound = shrdPref.getBoolean("sound", true);
//                // 진동으로 알릴건지
//                boolean isVib = shrdPref.getBoolean("vib", true);
//
//                if (isSound && isVib) {
//                    notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
//                } else if (isSound && !isVib) {
//                    notification.defaults = Notification.DEFAULT_SOUND;
//                } else if (!isSound && isVib) {
//                    notification.defaults = Notification.DEFAULT_VIBRATE;
//                }

        PendingIntent contentIntent = PendingIntent.getActivity/** OR getService() OR getBroadcastReceiver() */
                (context, 0, new Intent(context,
                                ChatRoomActivity.class/**알림 터치했을 때 호출할 액티비티*/
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification
                = new Notification.Builder(context)
                .setContentTitle("알림 제목")
                .setContentText("내용")
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

}
