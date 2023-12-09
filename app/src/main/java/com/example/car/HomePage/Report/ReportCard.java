package com.example.car.HomePage.Report;

public class ReportCard {
    String ReportId;
    String username;
    String type;
    String description;
    String image;

    public ReportCard(String reportId, String username, String type, String description) {
        ReportId = reportId;
        this.username = username;
        this.type = type;
        this.description = description;
    }
    public String getDescriptionSimple(){
        if(description.length()>=10){
            return description.substring(0,10)+"...";
        }else {
            return description;
        }
    }
}
