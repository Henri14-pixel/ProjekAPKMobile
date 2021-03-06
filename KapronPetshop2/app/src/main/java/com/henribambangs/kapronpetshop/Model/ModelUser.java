package com.henribambangs.kapronpetshop.Model;

public class ModelUser {
    private int customer_id;
    private String login_id, login_pw, customer_name, email, gender, address, phone_number, customer_image, province_id, city_id, subdistrict_id, province_name, city_name, subdistrict_name;

    public ModelUser() {
    }

    public ModelUser(int customer_id, String login_id, String customer_name, String email, String gender, String address, String phone_number, String customer_image, String province_id, String city_id, String subdistrict_id, String province_name, String city_name, String subdistrict_name, String login_pw) {
        this.customer_id = customer_id;
        this.login_id = login_id;
        this.customer_name = customer_name;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.phone_number = phone_number;
        this.customer_image = customer_image;
        this.province_id = province_id;
        this.city_id = city_id;
        this.subdistrict_id = subdistrict_id;
        this.province_name = province_name;
        this.city_name = city_name;
        this.subdistrict_name = subdistrict_name;
        this.login_pw = login_pw;
    }

    //--------------------------------//
    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    //--------------------------------//
    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    //--------------------------------//
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    //--------------------------------//
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //--------------------------------//
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //--------------------------------//
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //--------------------------------//
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    //--------------------------------//
    public String getCustomer_image() {
        return customer_image;
    }

    public void setCustomer_image(String customer_image) {
        this.customer_image = customer_image;
    }

    //--------------------------------//
    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    //--------------------------------//
    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    //--------------------------------//
    public String getSubdistrict_id() {
        return subdistrict_id;
    }

    public void setSubdistrict_id(String subdistrict_id) {
        this.subdistrict_id = subdistrict_id;
    }

    //--------------------------------//
    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    //--------------------------------//
    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    //--------------------------------//
    public String getSubdistrict_name() {
        return subdistrict_name;
    }

    public void setSubdistrict_name(String subdistrict_name) {
        this.subdistrict_name = subdistrict_name;
    }

    //--------------------------------//
    public String getLogin_pw() {
        return login_pw;
    }

    public void setLogin_pw(String login_pw) {
        this.login_pw = login_pw;
    }
}

