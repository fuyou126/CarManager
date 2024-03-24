package com.example.car.Car;

public class CarCard {
    String carType;
    String carName;
    String carNumber;
    Boolean isRegister;
    String carId;

    public CarCard(String carBrand,String carModel, String carType, String carNumber, Boolean isRegister, String carId) {
        this.carType = carType;
        this.carName = carBrand + " " + carModel;
        this.carNumber = carNumber;
        this.isRegister = isRegister;
        this.carId = carId;
    }
    public String getFormatCarNumber(){
        String res = "";
        if(!carNumber.isEmpty()){
            String start = carNumber.substring(0,2);
            String end = carNumber.substring(2);
            res = start + "·" + end;
        }
        return res;
    }
}
