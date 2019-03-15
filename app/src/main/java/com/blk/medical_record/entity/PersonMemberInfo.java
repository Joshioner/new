package com.blk.medical_record.entity;

/**
 * Created by asus on 2018/1/28.
 */

public class PersonMemberInfo {
    private String person_name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonMemberInfo(String person_name, int id) {
        this.person_name = person_name;
        this.id = id;
    }

    public PersonMemberInfo(String person_name)
    {
        this.person_name = person_name;
    }
    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_name() {
        return person_name;
    }


    @Override
    public int hashCode() {
       return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PersonMemberInfo){
            return ((PersonMemberInfo) obj).id == this.id && ((PersonMemberInfo) obj).person_name.equals(this.person_name);
        }
        return false;
    }
}
