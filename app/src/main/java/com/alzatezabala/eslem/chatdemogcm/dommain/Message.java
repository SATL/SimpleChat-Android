package com.alzatezabala.eslem.chatdemogcm.dommain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eslem on 07/10/2014.
 */
public class Message {
   private int id;
    private int idConversation;
    private int idUsuario;
    private boolean own;
    private String message;
    private long time;

    public Message(int idConversation, boolean own, String message) {
        this.idConversation = idConversation;
        this.own = own;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public Message(int idUsuario, String message) {
        this.idUsuario = idUsuario;
        this.message = message;
    }

    public Message(int id, int idConversation, boolean own, String message, long time) {
        this.id = id;
        this.idConversation = idConversation;
        this.own = own;
        this.message = message;
        this.time = time;
    }

    public Message(int idConversation, boolean own, String message, long time) {
        this.idConversation = idConversation;
        this.own = own;
        this.message = message;
        this.time = time;
    }

    public String getStringDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(this.time);
        return sdf.format(resultdate);
    }

    public String getStringTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date resultdate = new Date(this.time);
        return sdf.format(resultdate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String toString(){
        return "From: "+this.getIdConversation()+" at "+this.getStringDate()+": "+this.getMessage();
    }
}
