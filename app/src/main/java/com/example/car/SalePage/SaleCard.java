package com.example.car.SalePage;

public class SaleCard {
//    Image
    public String carName;
    public String carDescription;
    public String price;
    public String saleID;


    public SaleCard(String carName, String carDescription, String price, String saleID) {
        this.carName = carName;
        this.carDescription = carDescription;
        this.price = price;
        this.saleID = saleID;
    }

    public String getCarName_viewer() {
        String carName_viewer;
        if(carName.length()>5){
            carName_viewer = carName.substring(0,5) + "...";
        }else{
            carName_viewer = carName;
        }
        return carName_viewer;
    }

    public String getCarDescription_viewer() {
        String carDescription_viewer;
        if(carDescription.length()>10){
            carDescription_viewer = carDescription.substring(0,10) + "...";
        }else{
            carDescription_viewer = carDescription;
        }
        return carDescription_viewer;
    }
}
