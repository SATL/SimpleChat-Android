package com.alzatezabala.eslem.chatdemogcm.persistence;

import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;

import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public interface ConversationDAO {
    public Conversation insert(Conversation conversation);
    public Conversation get(int id);
    public Conversation getFromUser(int idUser);
    public boolean delete(int id);
    public List<Conversation> getAll();
    public void deleteAllData();
    public void update(Conversation conversation);
}
