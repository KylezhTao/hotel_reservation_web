package com.example.hotel.enums;


public enum UserType {
    Client("1"),
    HotelManager("2"),
    Admin("3"),
    Marketer("4");
    private String value;

    UserType(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }

}
