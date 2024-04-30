package com.example.happytailhub;

public class HelperClass {

    public HelperClass(){

    }

    String name;
    String email;
    String username;
    String password;
    String userType;

    private String dataName;
    private String dataPetName;
    private String dataType;
    private String dataAge;
    private String dataSex;
    private String dataColor;
    private String dataSpec;
    private String dataImage;
    private String key;

    public HelperClass(String owner, String pet, String type, String age, String sex, String color, String spec) {
        this.name = owner;
        this.dataPetName = pet;
        this.dataType = type;
        this.dataAge = age;
        this.dataSex = sex;
        this.dataColor = color;
        this.dataSpec = spec;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HelperClass(String dataName, String dataPetName, String dataType, String dataAge, String dataSex, String dataColor, String dataSpec, String dataImage) {
        this.dataName = dataName;
        this.dataPetName = dataPetName;
        this.dataType = dataType;
        this.dataAge = dataAge;
        this.dataSex = dataSex;
        this.dataColor = dataColor;
        this.dataSpec = dataSpec;
        this.dataImage = dataImage;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataPetName() {
        return dataPetName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDataAge() {
        return dataAge;
    }

    public String getDataSex() {
        return dataSex;
    }

    public String getDataColor() {
        return dataColor;
    }

    public String getDataSpec() {
        return dataSpec;
    }
    public String getDataImage(){ return dataImage;}




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



    public HelperClass(String name, String email, String username, String password, String userType) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

}
