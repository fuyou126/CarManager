package com.example.car.SalePage.Chat;

import java.sql.Timestamp;

public class ChatDetailCard {
    String content;
    String time;
    Boolean isSend;

    public ChatDetailCard(String content, String time, Boolean isSend) {
        this.content = content;
        this.time = time;
        this.isSend = isSend;
    }
}
