package com.alzatezabala.eslem.chatdemogcm.android.push;

import android.content.Context;
import android.util.Log;

import com.alzatezabala.eslem.chatdemogcm.android.MainActivity;
import com.alzatezabala.eslem.chatdemogcm.dommain.Message;
import com.alzatezabala.libreria.LibSharedPreferences;
import com.alzatezabala.libreria.Post;

import java.util.HashMap;

/**
 * Created by Eslem on 08/10/2014.
 */
public class SendMessage {

    public static void sendMessage(String message, Context context, int idUser) {
        LibSharedPreferences libSharedPreferences = new LibSharedPreferences(context, MainActivity.SHAREDNAME);
        Post post = new Post("http://push.alzatezabala.com/chat/android/message.php");
        HashMap<String, String> hashMap =  new HashMap<String, String>();
        hashMap.put("id", libSharedPreferences.getValue("idPush"));
        hashMap.put("username", libSharedPreferences.getValue("username"));
        Log.d("message", message);
        hashMap.put("message", message);
        hashMap.put("to", ""+idUser);
        post.setParams(hashMap);
        post.post();

    }
}
