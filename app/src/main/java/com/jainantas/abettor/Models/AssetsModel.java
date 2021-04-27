package com.jainantas.abettor.Models;

public class AssetsModel {
    String Price,Quantity,Name,Type;
    public AssetsModel(){}
    public AssetsModel(String price, String quantity, String name, String type) {
        Price = price;
        Quantity = quantity;
        Name = name;
        Type = type;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
