package com.example.moneycare.ui.view.account;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.moneycare.R;
import com.example.moneycare.ui.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent toMain = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, toMain, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TransactionReminder")
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setContentTitle("Nhắc nhở thêm giao dịch")
                .setContentText("Đã đến giờ thêm giao dịch trong ngày, bạn đã thêm giao dịch chưa nào?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(100, builder.build());
    }
}
