package com.alzatezabala.eslem.chatdemogcm.persistence;

import com.alzatezabala.eslem.chatdemogcm.dommain.Message;

import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public interface MessagesDAO {
    public Message insert(Message message);
    public Message get(int id);
    public boolean delete(int id);
    public void deleteAllData();
    public List<Message> getAll();
    public List<Message> getAllConversation(int idConversation);
}
