package com.henribambangs.kapronpetshop.Model;

public class ModelPaymentMethod {

    String payment_method_id, payment_method_name, payment_method_image;

    public ModelPaymentMethod() {
    }

    public ModelPaymentMethod(String payment_method_id, String payment_method_name, String payment_method_image) {
        this.payment_method_id = payment_method_id;
        this.payment_method_name = payment_method_name;
        this.payment_method_image = payment_method_image;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getPayment_method_name() {
        return payment_method_name;
    }

    public void setPayment_method_name(String payment_method_name) {
        this.payment_method_name = payment_method_name;
    }

    public String getPayment_method_image() {
        return payment_method_image;
    }

    public void setPayment_method_image(String payment_method_image) {
        this.payment_method_image = payment_method_image;
    }
}
