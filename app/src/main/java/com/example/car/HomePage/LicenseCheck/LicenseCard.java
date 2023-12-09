package com.example.car.HomePage.LicenseCheck;

public class LicenseCard {
    String username;
    String carBrand;
    String carModel;
    String carNumber;

    public LicenseCard(String username, String carBrand, String carModel, String carNumber) {
        this.username = username;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }

    public String getInfo(){
        return carNumber + " " +carBrand + " " + carModel;
    }
}
