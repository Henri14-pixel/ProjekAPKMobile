package com.henribambangs.kapronpetshop.Model;

public class ModelProductList {
    String product_id, product_price, product_name, product_image, product_weight, product_stock, product_short_description, product_type_name;

    public ModelProductList() {
    }

    public ModelProductList(String product_id, String product_name, String product_price, String product_image, String product_weight, String product_stock, String product_short_description) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_weight = product_weight;
        this.product_stock = product_stock;
        this.product_short_description = product_short_description;
        this.product_type_name = product_type_name;
    }

    public String getId() {
        return product_id;
    }

    public void setId(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory() {
        return product_type_name;
    }

    public void setCategory(String product_type_name) {
        this.product_type_name = product_type_name;
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

    public String getWeight() {
        return product_weight;
    }

    public void setWeight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getStock() {
        return product_stock;
    }

    public void setStock(String product_stock) {
        this.product_stock = product_stock;
    }

    public String getDescription() {
        return product_short_description;
    }

    public void setDescription(String product_short_description) {
        this.product_short_description = product_short_description;
    }
}
