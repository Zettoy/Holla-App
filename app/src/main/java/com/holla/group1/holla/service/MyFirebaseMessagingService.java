package com.holla.group1.holla.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.holla.group1.holla.R;
import com.holla.group1.holla.ViewPostActivity;
import com.holla.group1.holla.api.RestAPIClient;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FBTEST";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Received From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Received Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Received Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        Map<String, String> data = remoteMessage.getData();
        String postID = data.get("post");
        String commentID = data.get("comment");

        // Add channel check
        String channelId = getResources().getString(R.string.notification_channel_comment);
        sendNotification(title, message, channelId, postID);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        RestAPIClient apiClient = new RestAPIClient(getApplicationContext(), null, null);
        apiClient.updateDeviceToken(s);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody, String channelId, String postID) {
        Intent intent = new Intent(this, ViewPostActivity.class);
        intent.putExtra(ViewPostActivity.BUNDLED_POST_ID, postID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_locate)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) manager.notify(1, notification);
    }
}
