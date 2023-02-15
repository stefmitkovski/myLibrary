package com.example.mylibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class LendingNotificationService extends Service {

    public static final String NOTIFY_BORROWER = "borrower_complete";
    public static final String NOTIFY_OWNER = "owner_complete";
    public static final int ID_NOTIFICATION_DL_COMPLETE = 1234;


    public LendingNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if(action.equals(NOTIFY_BORROWER)){
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                //mChannel.enableLights(true);
                //mChannel.setLightColor(Color.RED);
                //mChannel.enableVibration(true);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                //mChannel.setShowBadge(true);
                manager.createNotificationChannel(mChannel);
            }

            // прикажување нотификација
            NotificationCompat.Builder builder = new NotificationCompat.Builder(LendingNotificationService.this, CHANNEL_ID)
                    .setContentTitle("Успешно ја позајми книгата")
                    .setContentText("Успешно ја позајми книгата "+ intent.getStringExtra("title") + " од авторот " + intent.getStringExtra("author"))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.baseline_notifications);
            Notification notification = builder.build();

            manager.notify(ID_NOTIFICATION_DL_COMPLETE, notification);

            // broadcast на порака кон апликацијата за да ја информира дека симнувањето е завршено
            Intent done = new Intent();
            done.setAction(NOTIFY_BORROWER);
            sendBroadcast(done);
        }else if(action.equals(NOTIFY_OWNER)){
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                //mChannel.enableLights(true);
                //mChannel.setLightColor(Color.RED);
                //mChannel.enableVibration(true);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                //mChannel.setShowBadge(true);
                manager.createNotificationChannel(mChannel);
            }

            // прикажување нотификација
            NotificationCompat.Builder builder = new NotificationCompat.Builder(LendingNotificationService.this, CHANNEL_ID)
                    .setContentTitle("Позајмена книга")
                    .setContentText("Книгата "+ intent.getStringExtra("title") + " од авторот " + intent.getStringExtra("author") + " е позајмена")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.baseline_notifications);
            Notification notification = builder.build();

            manager.notify(ID_NOTIFICATION_DL_COMPLETE, notification);

            // broadcast на порака кон апликацијата за да ја информира дека симнувањето е завршено
            Intent done = new Intent();
            done.setAction(NOTIFY_OWNER);
            sendBroadcast(done);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}