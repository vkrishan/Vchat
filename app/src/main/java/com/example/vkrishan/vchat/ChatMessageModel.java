package com.example.vkrishan.vchat;

import java.util.Date;

public class ChatMessageModel {

    private String Message;
    private String Username;
    private long Time;


    public ChatMessageModel(String Message, String Username){

        this.Message = Message;
        this.Username = Username;
        Time = new Date().getTime();

    }

    public ChatMessageModel(){

    }

    // Getter, Setter

    public String getMessage(){
        return Message;
    }

    public void setMessage(String Message){
        this.Message = Message;
    }

    public String getUsername(){
        return Username;
    }

    public void setUsername(String username){
        this.Username = username;
    }

    public long getTime(){
        return Time;
    }

    public void setTime(long time) {
        this.Time = time;
    }
}
