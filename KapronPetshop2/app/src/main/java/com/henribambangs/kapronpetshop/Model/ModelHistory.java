package com.henribambangs.kapronpetshop.Model;

public class ModelHistory {

    String order_id, order_bukti, order_date, payment_amount, order_status, product_name, product_image, jml;

    public ModelHistory() {
    }

    public ModelHistory(String order_id, String order_bukti, String order_date, String payment_amount, String order_status, String product_name, String product_image, String jml) {
        this.order_id = order_id;
        this.order_bukti = order_bukti;
        this.order_date = order_date;
        this.payment_amount = payment_amount;
        this.order_status = order_status;
        this.product_name = product_name;
        this.product_image = product_image;
        this.jml = jml;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_bukti() {
        return order_bukti;
    }

    public void setOrder_bukti(String order_bukti) {
        this.order_bukti = order_bukti;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }
}
