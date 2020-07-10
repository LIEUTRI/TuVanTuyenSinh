package com.b1610701.tuvantuyensinh.model;

public class Chat {
    private String sender;
    private String receiver;
    private String message;

    public Chat(){}
    public Chat(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getSender(){
        return this.sender;
    }
    public String getReceiver(){
        return this.receiver;
    }
    public String getMessage(){
        return this.message;
    }
}
