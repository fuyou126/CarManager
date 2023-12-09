package com.example.car.SalePage.Chat;

import java.sql.Timestamp;

public class ChatDetailCard {
    String content;
    Timestamp timestamp;
    String timeString;
    Boolean isSend;

    public ChatDetailCard(String content, Timestamp timestamp, Boolean isSend) {
        this.content = content;
        this.timestamp = timestamp;
        this.isSend = isSend;
    }

    public String getTimeString(){
        return "2023/11/12 18:29";
    }
}
