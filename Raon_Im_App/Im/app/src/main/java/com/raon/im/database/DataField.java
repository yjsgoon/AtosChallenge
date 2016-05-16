package com.raon.im.database;

/**
 * Created by Na Young on 2016-01-20.
 *
 * This class is for the data fields for both tables, personal information table and application lock password table
 */
public class DataField {
    private long id_1;
    private String email;
    private String password;
    private String name;
    private String country;
    private String city;
    private String address;
    private String phone;
    private String birthday_year;
    private String birthday_month;
    private String birthday_day;

    private long id_2;
    private String appPW;

    public long getId_2() {
        return id_2;
    }

    public void setId_2(long id_2) {
        this.id_2 = id_2;
    }

    public long getId_1() {
        return id_1;
    }

    public void setId_1(long id_1) {
        this.id_1 = id_1;
    }

    public String getAppPW() {
        return appPW;
    }

    public void setAppPW(String appPW) {
        this.appPW = appPW;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday_year() {
        return birthday_year;
    }

    public void setBirthday_year(String birthday_year) {
        this.birthday_year = birthday_year;
    }

    public String getBirthday_month() {
        return birthday_month;
    }

    public void setBirthday_month(String birthday_month) {
        this.birthday_month = birthday_month;
    }

    public String getBirthday_day() {
        return birthday_day;
    }

    public void setBirthday_day(String birthday_day) {
        this.birthday_day = birthday_day;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
