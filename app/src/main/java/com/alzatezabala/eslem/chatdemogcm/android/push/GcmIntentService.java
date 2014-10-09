package com.alzatezabala.eslem.chatdemogcm.android.push;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.ChatRoomActivity;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.dommain.Message;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAO;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAOImplSQLiteOpenHelper;
import com.alzatezabala.eslem.chatdemogcm.persistence.MessageDAOImplSQLiteOpenHelper;
import com.alzatezabala.eslem.chatdemogcm.persistence.MessagesDAO;
import com.alzatezabala.libreria.LibSharedPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GcmIntentService extends IntentService {

    private static final String TAG = "push-Service";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                // / sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                // sendNotification("Deleted messages on server: " +
                // extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 2; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
					/*
					 * try { Thread.sleep(5000); } catch (InterruptedException
					 * e) { }
					 */
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReciever.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle msg) {
        String messageString = msg.getString("message");
        Log.d("Message recieved", messageString);
        String userName = msg.getString("username");
        int idUser = Integer.parseInt(msg.getString("id"));

        ConversationDAO conversationDAO = new ConversationDAOImplSQLiteOpenHelper(getApplicationContext());
        Conversation conversation = conversationDAO.getFromUser(idUser);
        if (conversation == null) {
            conversation = new Conversation(idUser);
            conversation.setUserName(userName);
            conversation = conversationDAO.insert(conversation);
        }

        conversation.setLastMessage(messageString);
        conversationDAO.update(conversation);
        int conversationId = conversation.getId();
        Message message = new Message(conversationId, false, messageString);

        MessagesDAO messagesDAO = new MessageDAOImplSQLiteOpenHelper(getApplicationContext());
        messagesDAO.insert(message);

        int notificationId = conversation.getId();

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New message from " + userName)
                .setAutoCancel(true)
                .setStyle(
                        new NotificationCompat.BigTextStyle().bigText(messageString))
                .setContentText(messageString);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


        Intent intent = new Intent(this, ChatRoomActivity.class);
        Intent broadCast = new Intent("com.alzatezabala.eslem.chatedemogcm.Broadcast");
        intent.putExtra("chatId", conversationId);
        broadCast.putExtra("chatId", conversationId);
        intent.putExtra("messageString", messageString);
        broadCast.putExtra("messageString", messageString);

        getApplicationContext().sendBroadcast(broadCast);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(contentIntent);


        if(!ChatRoomActivity.usernameRunning.equals(userName)){
            mNotificationManager.notify(notificationId, mBuilder.build());
        }



    }

}
