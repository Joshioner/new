package com.blk.medical_record.entity;

/**
 * Created by asus on 2018/1/28.
 */

public class person_member_info {
    private String person_name;
    public person_member_info(String person_name)
    {
        this.person_name = person_name;
    }
    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_name() {
        return person_name;
    }


}
