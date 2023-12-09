package com.example.car.Car;

public class CarCard {
    String carType;
    String carName;
    String carNumber;
    Boolean isRegister;

    public CarCard(String carType, String carName, String carNumber, Boolean isRegister) {
        this.carType = carType;
        this.carName = carName;
        this.carNumber = carNumber;
        this.isRegister = isRegister;
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
