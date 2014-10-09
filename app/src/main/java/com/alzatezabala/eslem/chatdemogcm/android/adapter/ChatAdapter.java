package com.alzatezabala.eslem.chatdemogcm.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alzatezabala.eslem.chatdemogcm.R;


import com.alzatezabala.eslem.chatdemogcm.dommain.Message;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Eslem on 07/10/2014.
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messageList;
    LayoutInflater inflater;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messageList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Message message = messageList.get(position);

        if (message.isOwn()) {
            convertView = inflater.inflate(R.layout.message_adapter_own, null);
        } else {
            convertView = inflater.inflate(R.layout.message_adapter_user, null);
        }


        TextView messageText = (TextView) convertView.findViewById(R.id.message);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        messageText.setText(message.getMessage());
        time.setText(message.getStringTime());
        return convertView;
    }
}
