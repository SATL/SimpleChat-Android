package com.alzatezabala.eslem.chatdemogcm.android.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.adapter.ConversationsAdapter;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.dommain.ObserverBroadCast;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAO;
import com.alzatezabala.eslem.chatdemogcm.persistence.ConversationDAOImplSQLiteOpenHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment implements ObserverBroadCast {

    private ProgressBar progress;
    private List<Conversation> conversationList;
    private ConversationsAdapter conversationsAdapter;

    public ConversationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        ConversationDAO conversationDAO = new ConversationDAOImplSQLiteOpenHelper(getActivity());

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        conversationList = conversationDAO.getAll();
        if (conversationList != null) {
            Log.d("conversation list", conversationList.toString());
            conversationsAdapter = new ConversationsAdapter(getActivity(), conversationList);
            listView.setAdapter(conversationsAdapter);
        }
        progress.setVisibility(View.GONE);
        return rootView;
    }


    @Override
    public void onRecieve(Intent intent) {
        Log.d("Recieve", "recieved Fragment");
        int idConversation = intent.getIntExtra("chatId", 0);
        if (conversationList != null && conversationList.size() > 0) {
            for (Conversation conversation : conversationList) {
                if (conversation.getId() == idConversation) {
                    String messageString = intent.getStringExtra("messageString");
                    if (messageString.length() > 0) {
                        conversation.setLastMessage(messageString);
                        conversationsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
