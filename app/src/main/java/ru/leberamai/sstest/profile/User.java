package ru.leberamai.sstest.profile;

public class User {
    // phoneNumber, а то непонятно, что номер
    private String number;
    private String name;
    private String email;
    private double latitude;
    private double longitude;

    public User() { }

    // очень странные 2 конструктора, в которых поля присваиваются сами себе
    public User(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(double latitude, double longitude){
        this.name = name;
        this.number = number;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
