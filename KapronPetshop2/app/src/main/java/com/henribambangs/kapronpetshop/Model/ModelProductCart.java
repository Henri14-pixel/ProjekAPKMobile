package com.henribambangs.kapronpetshop.Model;

public class ModelProductCart {

    String product_id, cart_amount_item, product_name, product_price, product_image, product_stock;

    public ModelProductCart() {
    }

    public ModelProductCart(String product_id, String cart_amount_item, String product_name, String product_price, String product_image, String product_stock) {
        this.product_id = product_id;
        this.cart_amount_item = cart_amount_item;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_stock = product_stock;
    }

    public String getId() {
        return product_id;
    }

    public void setId(String product_id) {
        this.product_id = product_id;
    }

    public String getAmount() {
        return cart_amount_item;
    }

    public void setAmount(String cart_amount_item) {
        this.cart_amount_item = cart_amount_item;
    }

    public String getName() {
        return product_name;
    }

    public void setName(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return product_price;
    }

    public void setPrice(String product_price) {
        this.product_price = product_price;
    }

    public String getImage() {
        return product_image;
    }

    public void setImage(String product_image) {
        this.product_image = product_image;
    }

    public String getStock() {
        return product_stock;
    }

    public void setStock(String product_stock) {
        this.product_stock = product_stock;
    }
}
