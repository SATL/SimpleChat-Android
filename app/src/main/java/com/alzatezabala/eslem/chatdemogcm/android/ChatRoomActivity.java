package com.alzatezabala.eslem.chatdemogcm.android;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.adapter.ChatAdapter;
import com.alzatezabala.eslem.chatdemogcm.android.push.SendMessage;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.dommain.Message;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAO;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAOImplSQLiteOpenHelper;
import com.alzatezabala.eslem.chatdemogcm.persistence.MessageDAOImplSQLiteOpenHelper;
import com.alzatezabala.eslem.chatdemogcm.persistence.MessagesDAO;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends Activity {
    ConversationDAO conversationDAO = new ConversationDAOImplSQLiteOpenHelper(this);
    MessagesDAO messagesDAO = new MessageDAOImplSQLiteOpenHelper(this);
    List<Message> messageList = new ArrayList<Message>();
    Conversation conversation;
    ChatAdapter chatAdapter;
    ListView listView;
    private BroadcastReceiver mReceiver;
    public static String usernameRunning = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        if (this.getIntent().getIntExtra("chatId", 0) != 0) {
            setUpChat();
        }
        if (this.getIntent().getIntExtra("userId", 0) != 0) {
            setUpChatUser();
        }
        //setUpReciever();

        setUp();
    }

    public void setUpReciever() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int idConversation = intent.getIntExtra("chatId", 0);
                Log.d("broadcast", "From conversation: " + idConversation);
                if (idConversation != 0) {
                    if (conversation.getId() == idConversation) {
                        String messageString = intent.getStringExtra("messageString");
                        if (messageString.length() > 0) {
                            Message message = new Message(conversation.getId(), false, messageString);
                            messageList.add(message);
                            chatAdapter.notifyDataSetChanged();
                            listView.setSelection(chatAdapter.getCount() - 1);
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter("com.alzatezabala.eslem.chatedemogcm.Broadcast");
        registerReceiver(mReceiver, filter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        usernameRunning = conversation.getUserName();
        setUpReciever();
        cancelNotification(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        usernameRunning = "";
        //unregister our receiver
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        usernameRunning = "";
        //unregister our receiver
    }

    public void setUp() {
        final EditText editText = (EditText) findViewById(R.id.edit_message);
        Button button_send = (Button) findViewById(R.id.send_button);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringMessage = editText.getText().toString();
                editText.setText("");
                if (stringMessage.length() > 0) {
                    Message message = new Message(conversation.getId(), true, stringMessage);
                    conversation.setLastMessage(stringMessage);
                    conversationDAO.update(conversation);
                    notifyNewMessage(message);
                }
            }
        });
    }

    public void notifyNewMessage(Message message) {
        SendMessage.sendMessage(message.getMessage(), this, conversation.getIdUser());
        messagesDAO.insert(message);
        messageList.add(message);
        chatAdapter.notifyDataSetChanged();
        listView.setSelection(chatAdapter.getCount() - 1);
    }

    public void setUpChat() {
        int id = this.getIntent().getIntExtra("chatId", 0);
        conversation = conversationDAO.get(id);
        setUpMessages(conversation);
    }

    public void setUpMessages(Conversation conversation) {

        getActionBar().setTitle("Chat: " + conversation.getUserName());
        usernameRunning = conversation.getUserName();

        List<Message> messageListCheck = messagesDAO.getAllConversation(conversation.getId());
        if (messageListCheck != null) {
            messageList = messageListCheck;
        }
        chatAdapter = new ChatAdapter(this, messageList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setStackFromBottom(true);
        listView.setAdapter(chatAdapter);
        listView.setSelection(chatAdapter.getCount() - 1);
    }

    public void setUpChatUser() {
        int idUser = this.getIntent().getIntExtra("userId", 0);
        if (idUser != 0) {
            conversation = conversationDAO.getFromUser(idUser);
            if (conversation == null) {
                conversation = new Conversation(idUser);
                String name = this.getIntent().getStringExtra("userName");
                Log.d("nameUser", name);
                conversation.setUserName(name);
                conversation = conversationDAO.insert(conversation);
            }
            setUpMessages(conversation);

        } else {
            Toast.makeText(getApplicationContext(), "Error leyendo usuario", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(conversation.getId());
        setUpMessages(conversation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
