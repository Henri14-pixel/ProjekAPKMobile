package com.henribambangs.kapronpetshop.Model;

public class ModelProductCategory {

    String product_type_id, product_type_name, product_type_description, product_type_image;

    public ModelProductCategory() {
    }

    public ModelProductCategory(String product_type_id, String product_type_name, String product_type_description, String product_type_image) {
        this.product_type_id = product_type_id;
        this.product_type_name = product_type_name;
        this.product_type_description = product_type_description;
        this.product_type_image = product_type_image;
    }

    public String getId() {
        return product_type_id;
    }

    public void setId(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getName() {
        return product_type_name;
    }

    public void setName(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    public String getDescription() {
        return product_type_description;
    }

    public void setDescription(String product_type_description) {
        this.product_type_description = product_type_description;
    }

    public String getImage() {
        return product_type_image;
    }

    public void setImage(String product_type_image) {
        this.product_type_image = product_type_image;
    }
}
