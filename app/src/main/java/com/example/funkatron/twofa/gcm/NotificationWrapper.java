package com.example.funkatron.twofa.gcm;

/**
 * Created by electricSunny on 16/05/2014.
 */
public class NotificationWrapper {

    private int id;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
