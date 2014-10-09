package com.alzatezabala.eslem.chatdemogcm.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.ChatRoomActivity;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.dommain.User;

import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public class UsersAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;


    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.users_adapter, null);

        TextView name = (TextView) convertView.findViewById(R.id.user_name);


        final User user = userList.get(position);
        name.setText(user.getName());

        convertView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userName", user.getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
