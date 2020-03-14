package com.forkexec.hub.domain;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;

public class FoodDomain {

    protected FoodIdDomain id;
    protected String entree;
    protected String plate;
    protected String dessert;
    protected int price;
    protected int preparationTime;
    private int quantity;


    public FoodDomain(FoodIdDomain id, String entree, String plate, String dessert, int price, int preparationTime) {
        this.id = id;
        this.entree = entree;
        this.plate = plate;
        this.dessert = dessert;
        this.price = price;
        this.preparationTime = preparationTime;
    }

    public FoodDomain(FoodIdDomain id, String entree, String plate, String dessert, int price, int preparationTime, int quantity) {
        this.id = id;
        this.entree = entree;
        this.plate = plate;
        this.dessert = dessert;
        this.price = price;
        this.preparationTime = preparationTime;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FoodIdDomain getId() {
        return id;
    }

    public void setId(FoodIdDomain id) {
        this.id = id;
    }

    public String getEntree() {
        return entree;
    }

    public void setEntree(String entree) {
        this.entree = entree;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getDessert() {
        return dessert;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
}
