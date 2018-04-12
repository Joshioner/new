package com.blk.medical_record.entity;

/**
 * Created by asus on 2017/11/4.
 */

public class medicalRecordDetail {
    private String date;
    private String hospital_name;
    private String department;
    private String list_id;

    public medicalRecordDetail(){}

    public medicalRecordDetail(String list_id,String date,String hospital_name,String department)
    {
        this.list_id = list_id;
        this.date = date;
        this.hospital_name = hospital_name;
        this.department = department;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
