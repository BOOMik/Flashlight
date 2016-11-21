package ru.boomik.flashlight;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by boomv on 22.05.2016.
 */
public class NotificationCenter {

    static int NotificationId = 1;

    public static void controlNotification(Context context, boolean enabled) {
        if (enabled) showNotify(context);
        else hideNotify(context);
    }

    private static void hideNotify(Context context) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(NotificationId);
    }

    private static void showNotify(Context context) {
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context, 1, new Intent(FlashlightReceiver.ACTION_OFF), PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_notify)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(context.getString(R.string.notify_flash_enable))
                .setContentText(context.getString(R.string.notify_click_for_disable))
                .build();
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NotificationId, notification);
    }
}
