package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderDomain {

    private FoodOrderIdDomain foodOrderId;
    private List<FoodOrderItemDomain> items;

    public FoodOrderDomain(FoodOrderIdDomain foodOrderId) {
        this.foodOrderId = foodOrderId;
    }

    public FoodOrderIdDomain getFoodOrderId() {
        return foodOrderId;
    }

    public List<FoodOrderItemDomain> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return this.items;
    }


}
