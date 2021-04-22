package com.cnit425.protectpurdueadmin;

public class Dose {
    private String dose_num;

    private String location;
    private String location_name;
    private String address;
    private String date;
    private String time;

    private String vaccineType;
    private String vaccineSerial;
    private String vaccineExpDate;

    private Boolean completed;

    public Dose(){}

    public String getDose_num() {
        return dose_num;
    }

    public void setDose_num(String dose_num) {
        this.dose_num = dose_num;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public String getVaccineSerial() {
        return vaccineSerial;
    }

    public void setVaccineSerial(String vaccine) {
        this.vaccineSerial = vaccine;
    }

    public String getVaccineExpDate() {
        return vaccineExpDate;
    }

    public void setVaccineExpDate(String vaccineExpDate) {
        this.vaccineExpDate = vaccineExpDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
