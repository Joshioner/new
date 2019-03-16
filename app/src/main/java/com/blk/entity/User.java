package com.blk.entity;

public class User {
    private int id;

    private String name;

    private String password;

    private int gender;

    private String birthday;

    private String photoImage;

    private String relation;

    public User(){

    }

    public User(String photoImage,String name,String relation,int gender,String birthday){
        this.photoImage = photoImage;
        this.name = name;
        this.relation = relation;
        this.gender = gender;
        this.birthday = birthday;
    }
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(String photoImage) {
        this.photoImage = photoImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", photoImage='" + photoImage + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
