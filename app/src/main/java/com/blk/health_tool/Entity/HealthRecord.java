package com.blk.health_tool.Entity;

public class HealthRecord {
    //唯一id
    private int id;
    //日期
    private String date;
    //时间
    private String time;
    //详细地点
    private String address;
    //食物、饮料的摄入
    private String food;
    //运动情况描述
    private String sport;
    //症状描述
    private String symptom;

    public HealthRecord() {
    }

    public HealthRecord(String date, String time, String address, String food, String sport, String symptom) {
        this.date = date;
        this.time = time;
        this.address = address;
        this.food = food;
        this.sport = sport;
        this.symptom = symptom;
    }

    public HealthRecord(int id, String date, String time, String address, String food, String sport, String symptom) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.food = food;
        this.sport = sport;
        this.symptom = symptom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
}
