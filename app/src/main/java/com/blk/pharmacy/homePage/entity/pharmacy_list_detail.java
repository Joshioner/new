package com.blk.pharmacy.homePage.entity;

/**
 * Created by asus on 2017/11/4.
 */

public class pharmacy_list_detail {
    private String pharmacyName;
    private String time;
    private String distance;
    private String send;
    private String distribution;
    private String average;

    public pharmacy_list_detail(){}

    public pharmacy_list_detail(String pharmacyName, String time, String distance, String send,String distribution,String average)
    {
        this.pharmacyName = pharmacyName;
        this.time = time;
        this.distribution = distribution;
        this.distance = distance;
        this.send = send;
        this.average = average;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
