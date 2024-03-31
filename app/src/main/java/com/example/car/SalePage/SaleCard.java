package com.example.car.SalePage;

public class SaleCard {
    public String carName;
    public String description;
    public String price;
    public String sellId;
    public String stuNumber;


    public SaleCard(String carName, String description, String price, String sellId,String stuNumber) {
        this.carName = carName;
        this.description = description;
        this.price = price;
        this.sellId = sellId;
        this.stuNumber = stuNumber;
    }

    public String getCarName_viewer() {
        String carName_viewer;
        if(carName.length()>6){
            carName_viewer = carName.substring(0,6) + "...";
        }else{
            carName_viewer = carName;
        }
        return carName_viewer;
    }

    public String getCarDescription_viewer() {
        String carDescription_viewer;
        if(description.length()>10){
            carDescription_viewer = description.substring(0,10) + "...";
        }else{
            carDescription_viewer = description;
        }
        return carDescription_viewer;
    }
}
