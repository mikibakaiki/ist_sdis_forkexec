package com.forkexec.hub.domain;

public class FoodOrderItemDomain {

    private FoodIdDomain foodId;
    private int foodQuantity;

    public FoodOrderItemDomain(FoodIdDomain foodId, int foodQuantity) {
        this.foodId = foodId;
        this.foodQuantity = foodQuantity;
    }

    public FoodIdDomain getFoodId() {
        return foodId;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }
}
