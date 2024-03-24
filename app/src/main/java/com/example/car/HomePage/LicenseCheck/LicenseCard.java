package com.example.car.HomePage.LicenseCheck;

public class LicenseCard {
    String username;
    String carBrand;
    String carModel;
    String carNumber;
    String carId;

    public LicenseCard(String username, String carBrand, String carModel, String carNumber, String carId) {
        this.username = username;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carId = carId;
    }

    public String getInfo(){return carNumber.substring(0,2)+"·"+carNumber.substring(2) + " " +carBrand + " " + carModel;}

}
