package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private String userId;

    private List<FoodDomain> cart = new ArrayList<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<FoodDomain> getCart() {
        return cart;
    }

    public void addToCart(FoodDomain f) {
        cart.add(f);
    }

    public void removeFromCart(FoodDomain f) {
        cart.remove(f);
    }

    public void clearCart() {
        cart.clear();
    }

    public int getSizeOfCart() {
        return cart.size();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }
}
