package com.alzatezabala.eslem.chatdemogcm.dommain;

import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public class Conversation {
    private int id;
    private int idUser;
    private String lastMessage;
    private String userName;
    private List<Message> messagesList;

    public Conversation(int id, int idUser){
        this.id=id;
        this.idUser=idUser;
    }

    public Conversation(int idUser){
        this.idUser=idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String toString(){
        return "Conversation id:"+this.id+" with user:"+this.idUser+" "+this.getUserName() ;
    }
}
