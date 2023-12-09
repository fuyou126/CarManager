package com.example.car.HomePage.CarRescue;

public class RescueCard {
    String username;
    int rescue_type;
    int rescueId;
    double latitude;
    double longitude;
    String address;
    String description;
    String phone;

    public RescueCard(String username, int rescue_type, int rescueId, double latitude, double longitude, String address, String description,String phone) {
        this.username = username;
        this.rescue_type = rescue_type;
        this.rescueId = rescueId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.phone = phone;
    }

    public String getRescue_description_str(){
        if (rescue_type == 1){
            return "拖车:"+description;
        } else if (rescue_type == 2) {
            return "换胎:"+description;
        }else {
            return "其他:"+description;
        }
    }

    public String getRescue_type_str(){
        if (rescue_type == 1){
            return "拖车";
        } else if (rescue_type == 2) {
            return "换胎";
        }else {
            return "其他";
        }
    }
}
