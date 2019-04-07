package com.blk.medical_record.entity;

/**
 * Created by asus on 2018/1/28.
 */

public class PersonMemberInfo {
    private int uid;
    private int fid;
    private String name;
    private String profile;
    private boolean flag;

    public PersonMemberInfo() {
    }

    public PersonMemberInfo(int uid, int fid, String name, String profile, boolean flag) {
        this.uid = uid;
        this.fid = fid;
        this.name = name;
        this.profile = profile;
        this.flag = flag;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

//    @Override
//    public int hashCode() {
//       return this.id;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof PersonMemberInfo){
//            return ((PersonMemberInfo) obj).id == this.id && ((PersonMemberInfo) obj).person_name.equals(this.person_name);
//        }
//        return false;
//    }
}
