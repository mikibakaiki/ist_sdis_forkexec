package com.forkexec.rst.domain;

/**
 * Product entity. Only the product quantity is mutable so its get/set methods
 * are synchronized.
 */
public class MenuDomain {
	/** Menu identifier. */
	private String menuId;
	/** Menu entree. */
	private String menuEntree;
	/** Menu plate. */
	private String menuPlate;
	/** Menu dessert. */
	private String menuDessert;
	/** Menu price */
	private int menuPrice;
	/** Menu preparation time */
	private int menuPreparationTime;
	/** Available quantity of this menu. */
	private volatile int quantity = 0;

	/** Create a new product */
	public MenuDomain(String mid, String entree, String plate, String dessert, int price, int preparationTime) {
		this.menuId = mid;
		this.menuEntree = entree;
		this.menuPlate = plate;
		this.menuDessert = dessert;
		this.menuPrice = price;
		this.menuPreparationTime = preparationTime;
		quantity = 0;
	}

	public String getId() {
		return menuId;
	}

	public String getEntree() {
		return menuEntree;
	}

	public String getPlate() {
		return menuPlate;
	}
	
	public String getDessert() {
		return menuDessert;
	}

	public int getPrice() {
		return menuPrice;
	}

	public int getPreparationTime() {
		return menuPreparationTime;
	}

	/** Synchronized locks object before returning quantity */
	public synchronized int getQuantity() {
		return quantity;
	}

	/** Synchronized locks object before setting quantity */
	public synchronized void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Menu [menuId=").append(menuId);
		builder.append(", entree=").append(menuEntree);
		builder.append(", plate=").append(menuPlate);
		builder.append(", dessert").append(menuDessert);
		builder.append(", price=").append(menuPrice);
		builder.append(", preparationTime=").append(menuPreparationTime);
		builder.append("]");
		return builder.toString();
	}

}