package com.forkexec.rst.domain;


/**
 * MenuOrderDomain entity. Immutable i.e. once an object is created it cannot be
 * changed.
 */
public class MenuOrderDomain {
	/** MenuOrder identifier. */
	private String menuOrderId;
	/** MenuOrdered menu identifier. */
	private String menuId;
	/** MenuOrdered quantity. */
	private int quantity;


	/** Create a new MenuOrderDomain. */
	public MenuOrderDomain(String menuOrderId, String menuId, int quantity) {
		this.menuOrderId = menuOrderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public String getMenuOrderId() {
		return menuOrderId;
	}

	public String getMenuId() {
		return menuId;
	}

	public int getQuantity() {
		return quantity;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuOrder [menuOrderId=").append(menuOrderId);
		builder.append(", menuId=").append(menuId);
		builder.append(", quantity=").append(quantity);
		builder.append("]");
		return builder.toString();
	}

}