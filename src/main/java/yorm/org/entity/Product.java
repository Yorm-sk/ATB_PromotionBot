package yorm.org.entity;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;

    private double storePrice;

    private double salesPrice;

    private String img;

    public Product(String name, double storePrice, double salesPrice, String img) {
        this.name = name;
        this.storePrice = storePrice;
        this.salesPrice = salesPrice;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
