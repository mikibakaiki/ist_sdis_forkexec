package com.forkexec.hub.domain;


public class FoodIdDomain {

    protected String restaurantId;
    protected String menuId;

    public FoodIdDomain(String restaurantId,  String menuId) {
        this.restaurantId = restaurantId;
        this.menuId = menuId;
    }

    public String getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
