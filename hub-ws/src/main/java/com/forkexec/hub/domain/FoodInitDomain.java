package com.forkexec.hub.domain;

public class FoodInitDomain {

    protected FoodDomain food;
    protected int quantity;

    public FoodDomain getFood() {
        return food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFood(FoodDomain food) {
        this.food = food;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
