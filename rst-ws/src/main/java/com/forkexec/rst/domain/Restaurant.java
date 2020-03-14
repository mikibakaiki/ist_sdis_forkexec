package com.forkexec.rst.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.forkexec.rst.domain.exception.BadInitException;
import com.forkexec.rst.domain.exception.BadMenuIdException;
import com.forkexec.rst.domain.exception.BadQuantityException;
import com.forkexec.rst.domain.exception.BadTextException;
import com.forkexec.rst.domain.exception.InsufficientQuantityException;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;


/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {

	/**
	 * Map of existing menus. Uses concurrent hash table implementation
	 * supporting full concurrency of retrievals and high expected concurrency
	 * for updates.
	 */
	private Map<String, MenuDomain> menus = new ConcurrentHashMap<>();

	/**
	 * Global menu order identifier counter. Uses lock-free thread-safe single
	 * variable.
	 */
	private AtomicInteger menuOrderIdCounter = new AtomicInteger(0);

	/** Map of Menu Orders. Also uses concurrent hash table implementation. */
	private Map<String, MenuOrderDomain> menuOrders = new ConcurrentHashMap<>();

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}


	
	// Menu ---------------------------------------------------------------

	public void reset() {
		menus.clear();
		menuOrders.clear();
		menuOrderIdCounter.set(0);
	}

	public Boolean menuExists(String mid) {
		return menus.containsKey(mid);
	}

	public Set<String> getMenusIDs() {
		return menus.keySet();
	}

	public MenuDomain getMenu(String menuId) throws BadMenuIdException {
		String aux = menuId.trim();
		if (aux.length() == 0)
			throw new BadMenuIdException("Menu identifier cannot be empty or whitespace!");
		MenuDomain res = menus.get(menuId);
		if(res == null)
			throw new BadMenuIdException("Menu doesn't exist!");
		return res;
	}

	public void registerMenu(String menuId, String entree, String plate, String dessert, int price, int preparationTime) {
		if (acceptMenu(menuId, entree, plate, dessert, price, preparationTime)) {
			menus.put(menuId, new MenuDomain(menuId, entree, plate, dessert, price, preparationTime));
		}
	}

	private Boolean acceptMenu(String menuId, String entree, String plate, String dessert, int price, int preparationTime) {
		return menuId != null && !"".equals(menuId) && entree != null && !"".equals(entree) && plate != null && !"".equals(plate)
				&& dessert != null && !"".equals(dessert) && price > 0 && preparationTime > 0;
	}
	
	public List<MenuDomain> searchMenus(String descriptionText) throws BadTextException{
		String aux = descriptionText.trim();
		if (aux.length() == 0)
			throw new BadTextException("Menu entree/plate/dessert cannot be empty or whitespace!");
		if (descriptionText.contains(" ") || descriptionText.contains("\n") || descriptionText.contains("\t") || descriptionText.contains("\r")){
			throw new BadTextException("Menu entree/plate/dessert cannot have whitespaces");
		}
		
		List<MenuDomain> results = new ArrayList<>();
		for (MenuDomain menud : menus.values()) {
			if(menud.getEntree().contains(descriptionText) || menud.getPlate().contains(descriptionText) || menud.getDessert().contains(descriptionText)) {
				results.add(menud);
			}
		}
		return results;
	}

	public String orderMenu(String menuId, int quantity) throws InsufficientQuantityException, BadQuantityException, BadMenuIdException {
		MenuDomain menu = getMenu(menuId);
		if (menu == null)
			throw new BadMenuIdException("Menu does not exist!");
		if (quantity <= 0)
			throw new BadQuantityException("Quantity must be a positive number!");
		decreaseMenuQuantity(menu, quantity);
		// create menu order record
		String menuOrderId = generateMenuOrderId(menuId);
		MenuOrderDomain menuOrder = new MenuOrderDomain(menuOrderId, menuId, quantity);
		menuOrders.put(menuOrderId, menuOrder);
		return menuOrderId;
	}

	private void decreaseMenuQuantity(MenuDomain menu, int quantity) throws InsufficientQuantityException, BadQuantityException {
		if (quantity <= 0)
			throw new BadQuantityException("Quantity must be a positive number!");
		// acquire lock to perform get and set together
		synchronized (menu) {
			int currentQuantity = menu.getQuantity();
			if (currentQuantity < quantity) {
				String message = String.format("Tried to buy %d units of menu %s but only %d units are available.",
						quantity, menu.getId(), currentQuantity);
				throw new InsufficientQuantityException(message);
				// throw also releases lock
			}
			// set new quantity
			menu.setQuantity(currentQuantity - quantity);
		}
		// release lock
	}
	
	public void ctrlInit(List<MenuInit> initialMenus) throws BadMenuIdException, BadInitException, BadQuantityException {
		for (MenuInit minit : initialMenus) {
			if(getMenusIDs().contains(minit.getMenu().getId().getId())) 
				throw new BadMenuIdException("MenuId already exists!");
			checkMenuInit(minit.getMenu(),minit.getQuantity());
			int quantity = minit.getQuantity();
			MenuId mid = minit.getMenu().getId();
			// create new MenuInit
			registerMenu(mid.getId(), minit.getMenu().getEntree(), minit.getMenu().getPlate(), minit.getMenu().getDessert(), minit.getMenu().getPrice(), minit.getMenu().getPreparationTime());
			getMenu(mid.getId()).setQuantity(quantity);
		}
	}

	// Auxiliary --------------------------------------------------------------
	
	public void checkMenuInit(Menu menu, int quantity) throws BadInitException, BadMenuIdException, BadQuantityException {
		//check null
		if (menu == null)
			throw new BadInitException("Menu cannot be null!");
		// check id
		String menuId = menu.getId().getId();
		if (menuId == null)
			throw new BadMenuIdException("Menu identifier cannot be null! (on init)");
		String aux = menuId.trim();
		if (aux.length() == 0)
			throw new BadMenuIdException("Menu identifier cannot be empty or whitespace! (on init)");
		// check entree, plate, dessert
		String menuEntree = menu.getEntree();
		if (menuEntree == null || menuEntree.equals("") || menuEntree.trim().length() == 0 )
			throw new BadInitException("Menu Entree cannot be null or empty or whitespaces (init)");
		String menuPlate = menu.getPlate();
		if (menuPlate == null || menuPlate.equals("") || menuPlate.trim().length() == 0)
			throw new BadInitException("Menu Plate cannot be null or empty or whitespaces (init)");
		String menuDessert = menu.getDessert();
		if (menuDessert == null || menuDessert.equals("") || menuDessert.trim().length() == 0)
			throw new BadInitException("Menu Dessert cannot be null or empty or whitespaces (init)");
		// check price
		int price = menu.getPrice();
		if (price <= 0)
			throw new BadInitException("Price must be a positive number!");
		// check preparation time
		int preparationTime = menu.getPreparationTime();
		if (preparationTime <= 0)
			throw new BadInitException("Preparation time must be a positive number!");
		// check quantity
		if (quantity <= 0)
			throw new BadQuantityException("Quantity must be a positive number!");
	}
	
	public MenuOrderDomain getMenuOrder(String menuOrderId) {
		return menuOrders.get(menuOrderId);
	}

	private String generateMenuOrderId(String moid) {
		// relying on AtomicInteger to make sure assigned number is unique
		int menuOrderId = menuOrderIdCounter.incrementAndGet();
		return Integer.toString(menuOrderId);
	}

	public List<String> getMenuOrdersIDs() {
		List<MenuOrderDomain> menuOrdersList = new ArrayList<>(menuOrders.values());
		List<String> idsList = new ArrayList<String>();
		for (MenuOrderDomain mo : menuOrdersList) {
			idsList.add(mo.getMenuOrderId());
		}
		return idsList;
	}
}
