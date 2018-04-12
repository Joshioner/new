package com.blk.health_tool.Entity;

/**
 * Created by lzx on 2018/3/28.
 */

public class auscultation_detail_list {
    private String auscultation_message;
    private String auscultation_date;
    private String auscultation_time;
    public auscultation_detail_list(){

    }
    public auscultation_detail_list(String message,String date , String time){
        this.auscultation_message = message;
        this.auscultation_date = date ;
        this.auscultation_time = time ;
    }

    public String getAuscultation_date() {
        return auscultation_date;
    }

    public String getAuscultation_message() {
        return auscultation_message;
    }

    public String getAuscultation_time() {
        return auscultation_time;
    }

    public void setAuscultation_message(String auscultation_message) {
        this.auscultation_message = auscultation_message;
    }

    public void setAuscultation_time(String auscultation_time) {
        this.auscultation_time = auscultation_time;
    }

    public void setAuscultation_date(String auscultation_date) {
        this.auscultation_date = auscultation_date;
    }
}
