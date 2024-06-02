package com.example.noteit.entities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.noteit.R;

public class NotificationUtils {

    public static final String DEFAULT_CHANNEL_ID = "default_channel_id"; // Change this to your desired channel ID
    public static final String DEFAULT_CHANNEL_NAME = "Default Channel"; // Change this to your desired channel name
    public static final int DEFAULT_CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH; // Use IMPORTANCE_HIGH for heads-up behavior

    public static void createNotificationChannel(Context context) {
        // Create the notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, DEFAULT_CHANNEL_IMPORTANCE);
            // Customize additional channel properties if needed
            // channel.setDescription("Your channel description");
           channel.enableVibration(true);
            // channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500}); // Example vibration pattern

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void showHeadsUpNotification(Context context, String title, String message) {
        // Create the notification channel (for Android Oreo and above)
        createNotificationChannel(context);

        // Create the heads-up notification with high importance
        Notification headsUpNotification = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to high for heads-up behavior
                .setCategory(NotificationCompat.CATEGORY_CALL) // Allow heads-up behavior
                .setDefaults(Notification.DEFAULT_ALL) // Default sound, vibration, and lights for the notification
                .setColor(Color.RED) // Set the notification color (customize as needed)
                .setAutoCancel(true) // Auto-cancel the notification when clicked
                .build();

        // Show the heads-up notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, headsUpNotification); // Use a unique notification ID if you need to update or cancel the notification later
    }
}
