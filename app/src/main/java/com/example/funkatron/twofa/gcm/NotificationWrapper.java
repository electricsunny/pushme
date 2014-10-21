package com.example.funkatron.twofa.gcm;

/**
 * Created by electricSunny on 16/05/2014.
 */
public class NotificationWrapper {

    private int id;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
