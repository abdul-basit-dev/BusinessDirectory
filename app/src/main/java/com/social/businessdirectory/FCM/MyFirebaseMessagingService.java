package com.social.businessdirectory.FCM;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.social.businessdirectory.MainActivity;
import com.social.businessdirectory.R;


import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    String currentLoggedInUserID, currentUserPostId, currentUserName;
    private DatabaseReference dbRef;
    FirebaseDatabase database;

//    @Override
//    public void onNewToken(String s) {
//        Log.e("NEW_TOKEN", s);
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        preferences = getSharedPreferences("MyPref", 0);
//        editor = preferences.edit();
//        currentLoggedInUserID = preferences.getString("uniqueId", "N/A");
//        currentUserPostId = preferences.getString("postID", "N/A");
//        currentUserName = preferences.getString("firstName", "N/A");
//        database = FirebaseDatabase.getInstance();
//        dbRef = database.getReference("astrology_app:").child("NewUser");
//        Date messageTime = new Date();
//        String finalTime, finalDate;
//        finalTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(messageTime);
//        finalDate = DateFormat.getDateInstance(DateFormat.LONG).format(messageTime);
//
//        final Intent intent = new Intent(this, MainActivity.class);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        int notificationID = new Random().nextInt(3000);
//
//
//        String notiTitle = remoteMessage.getData().get("title");
//        String notiDetails = remoteMessage.getData().get("message");
//        String notiDetailsFinal = "<b>" +currentUserName+"</b>" +", "+ notiDetails;
//
//
//        if (currentLoggedInUserID!=null && currentUserPostId!=null){
//            String pushKey = dbRef.child(preferences.getString("postID", "N/A")).push().getKey();
//            Map<String, Object> statusMap = new HashMap<>();
//            statusMap.put("notiTitle", notiTitle);
//            statusMap.put("notiBody", notiDetails);
//            statusMap.put("messageTime", finalTime);
//            statusMap.put("messageDate", finalDate);
//            statusMap.put("readStatus",0);
//            statusMap.put("notiPostID",pushKey);
//            statusMap.put("messageType", "notification");
//            statusMap.put("userId", currentLoggedInUserID);
//            ///dbRef.child(preferences.getString("postID", "N/A")).child("Notifications").child(pushKey).setValue(statusMap);
//        }
//
//
//
//
//
//      /*
//        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
//        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
//      */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setupChannels(notificationManager);
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.ic_notification);
//
//        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setLargeIcon(largeIcon)
//                .setContentTitle(notiTitle)
//                .setContentText(Html.fromHtml(notiDetailsFinal))
//                .setAutoCancel(true)
//                .setSound(notificationSoundUri)
//                .setContentIntent(pendingIntent);
//
//        //Set notification color to match your app color template
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setColor(this.getResources().getColor(R.color.white));
//        }
//        notificationManager.notify(notificationID, notificationBuilder.build());
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(NotificationManager notificationManager) {
//        CharSequence adminChannelName = "BUSINESSDIRECTORY";
//        String adminChannelDescription = "NEWUSERS";
//
//        NotificationChannel adminChannel;
//        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
//        adminChannel.setDescription(adminChannelDescription);
//        adminChannel.enableLights(true);
//        adminChannel.setLightColor(Color.RED);
//        adminChannel.enableVibration(true);
//        if (notificationManager != null) {
//            notificationManager.createNotificationChannel(adminChannel);
//        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//
//    }
}