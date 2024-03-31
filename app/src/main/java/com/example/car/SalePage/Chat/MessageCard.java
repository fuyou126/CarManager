package com.example.car.SalePage.Chat;

public class MessageCard {
    String name,lastMessage;
    // icon
    String stuNumber;
    // carimage
    String sellId;
    int unseenMessages;

    public MessageCard(String name, String lastMessage, String stuNumber, String sellId, int unseenMessages) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.stuNumber = stuNumber;
        this.sellId = sellId;
        this.unseenMessages = unseenMessages;
    }

    public String getName_viewer(){
        String name_viewer;
        if(name.length()>5){
            name_viewer = name.substring(0,5) + "...";
        }else{
            name_viewer = name;
        }
        return name_viewer;
    }

    public String getLastMessage_viewer(){
        String LastMessage_viewer;
        if(lastMessage.length()>10){
            LastMessage_viewer = lastMessage.substring(0,10) + "...";
        }else{
            LastMessage_viewer = lastMessage;
        }
        return LastMessage_viewer;
    }

    public int getUnseenMessages_viewer(){
        int UnseenMessages_viewer;
        if(this.unseenMessages>9){
            UnseenMessages_viewer = -1;
        }else{
            UnseenMessages_viewer = unseenMessages;
        }
        return UnseenMessages_viewer;
    }
}
