package com.example.car.HomePage.Report;

public class ReportCard {
    String reportId;
    String stuNumber;
    String type;
    String description;
    String carId;
    String carNumber;

    public ReportCard(String reportId, String stuNumber, String type, String description ,String carId, String carNumber) {
        this.reportId = reportId;
        this.stuNumber = stuNumber;
        this.type = type;
        this.description = description;
        this.carId = carId;
        this.carNumber = carNumber;
    }

    public String getDescriptionSimple(){
        if(description.length()>=10){
            return description.substring(0,10)+"...";
        }else {
            return description;
        }
    }
}
