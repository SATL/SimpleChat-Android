package com.alzatezabala.eslem.chatdemogcm.dommain;

/**
 * Created by Eslem on 08/10/2014.
 */
public class User {
    private int id;
    private String name;
    private String hash;


    public User(int id, String name, String hash) {
        this.id = id;
        this.name = name;
        this.hash = hash;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
