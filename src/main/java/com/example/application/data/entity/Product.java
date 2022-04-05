package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "application_product")
public class Product extends AbstractEntity{

    private String name;
    private String sku;
    private double cost;
    private double suggestedCellPrice;
    private int currentInventory;
    private int minimumInventory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSuggestedCellPrice() {
        return suggestedCellPrice;
    }

    public void setSuggestedCellPrice(double suggestedCellPrice) {
        this.suggestedCellPrice = suggestedCellPrice;
    }

    public int getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(int currentInventory) {
        this.currentInventory = currentInventory;
    }

    public int getMinimumInventory() {
        return minimumInventory;
    }

    public void setMinimumInventory(int minimumInventory) {
        this.minimumInventory = minimumInventory;
    }
}
