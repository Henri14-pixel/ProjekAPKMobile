package com.henribambangs.kapronpetshop.Model;

public class ModelUser {
    private int customer_id;
    private String login_id, customer_name, email, gender, address, phone_number, customer_image, province_id, city_id, subdistrict_id;

    public ModelUser() {
    }

    public ModelUser(int customer_id, String login_id, String customer_name, String email, String gender, String address, String phone_number, String customer_image, String province_id, String city_id, String subdistrict_id) {
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
}

