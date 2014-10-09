package com.alzatezabala.eslem.chatdemogcm.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alzatezabala.eslem.chatdemogcm.android.ChatRoomActivity;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Eslem on 07/10/2014.
 */
public class ConversationsAdapter extends BaseAdapter {
    private Context context;
    private List<Conversation> conversations;
    private LayoutInflater inflater;


    public ConversationsAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return conversations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.conversation_adapter, null);

        TextView name = (TextView) convertView.findViewById(R.id.user_name);
        TextView lastmessage = (TextView) convertView.findViewById(R.id.last_message);


        final Conversation conversation = conversations.get(position);
        name.setText(conversation.getUserName());
        lastmessage.setText(conversation.getLastMessage());

        convertView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("chatId", conversation.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
