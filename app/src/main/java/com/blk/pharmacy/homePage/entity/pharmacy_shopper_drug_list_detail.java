package com.blk.pharmacy.homePage.entity;

/**
 * Created by asus on 2017/11/4.
 */

public class pharmacy_shopper_drug_list_detail {
    private String drugName;
    private String drugFunction;
    private String priceInt;
    private String priceDouble;
    private String payNumber;

    public pharmacy_shopper_drug_list_detail(){}

    public pharmacy_shopper_drug_list_detail( String drugName, String drugFunction, String priceInt, String priceDouble, String payNumber)
    {
        this.drugName = drugName;
        this.drugFunction = drugFunction;
        this.priceInt = priceInt;
        this.priceDouble = priceDouble;
        this.payNumber = payNumber;
    }


    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugFunction() {
        return drugFunction;
    }

    public void setDrugFunction(String drugFunction) {
        this.drugFunction = drugFunction;
    }

    public String getPriceInt() {
        return priceInt;
    }

    public void setPriceInt(String priceInt) {
        this.priceInt = priceInt;
    }

    public String getPriceDouble() {
        return priceDouble;
    }

    public void setPriceDouble(String priceDouble) {
        this.priceDouble = priceDouble;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }
}
