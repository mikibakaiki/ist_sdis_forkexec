package com.forkexec.rst.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.rst.domain.MenuDomain;
import com.forkexec.rst.domain.MenuOrderDomain;
import com.forkexec.rst.domain.Restaurant;
import com.forkexec.rst.domain.exception.BadInitException;
import com.forkexec.rst.domain.exception.BadMenuIdException;
import com.forkexec.rst.domain.exception.BadQuantityException;
import com.forkexec.rst.domain.exception.BadTextException;
import com.forkexec.rst.domain.exception.InsufficientQuantityException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType",
            wsdlLocation = "RestaurantService.wsdl",
            name ="RestaurantWebService",
            portName = "RestaurantPort",
            targetNamespace="http://ws.rst.forkexec.com/",
            serviceName = "RestaurantService"
)
public class RestaurantPortImpl implements RestaurantPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private RestaurantEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		// check menu id
		if (menuId == null)
			throwBadMenuId("Menu identifier cannot be null!");
		Restaurant rest = Restaurant.getInstance();
		MenuDomain m = null;
		try {
			m = rest.getMenu(menuId.getId());
		
		} catch (BadMenuIdException bmie) {
			throwBadMenuId(bmie.getMessage());
		}
		Menu menu = newMenu(m);
		return menu;
		
	}
	
	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		
		if (descriptionText == null)
			throwBadText("Menu entree/plate/dessert cannot be null!");
		
		Restaurant rest = Restaurant.getInstance();
		List<MenuDomain> results = new ArrayList<>();
		List<Menu> res = new ArrayList<>();
		
		try {
			results = rest.searchMenus(descriptionText);
		} catch (BadTextException bte) {
			throwBadText(bte.getMessage());
		}
		
		for(MenuDomain m : results) {
			res.add(newMenu(m));
		}		
		return res;
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		
		if (arg0 == null)
			throwBadMenuId("Menu identifier cannot be null!");
		
		Restaurant rest = Restaurant.getInstance();
		MenuOrderDomain mo;
		String request = null;
		try {
			request = rest.orderMenu(arg0.getId(), arg1);
		} catch (InsufficientQuantityException iqe) {
			throwInsufficientQuantity(iqe.getMessage());
		} catch (BadQuantityException bqe) {
			throwBadQuantity(bqe.getMessage());
		} catch (BadMenuIdException bmie) {
			throwBadMenuId(bmie.getMessage());
		} 
		if (request != null) {
			mo = rest.getMenuOrder(request);
			MenuOrder res = newMenuOrder(mo);
			return res;
		}		
		return null;
	}	

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Restaurant";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Restaurant.getInstance().reset();
	}

	/** Set variables with specific values. */
	@Override	
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		if (initialMenus.isEmpty() || initialMenus == null)
			throwBadInit("Initial menus cannot be null!");
		Restaurant r = Restaurant.getInstance();
		
		try {
			r.ctrlInit(initialMenus);
		} catch (BadMenuIdException bmie) {
			throwBadInit(bmie.getMessage());
		} catch (BadInitException bie) {
			throwBadInit(bie.getMessage());
		} catch (BadQuantityException bqe) {
			throwBadInit(bqe.getMessage());
		}
	}
	
	// Auxiliary operations --------------------------------------
		
	public List<Menu> listMenus() {
		Restaurant rest = Restaurant.getInstance();
		List<Menu> menus = new ArrayList<Menu>();
		for (String mid : rest.getMenusIDs()) {
			MenuDomain m = null;
			try {
				m = rest.getMenu(mid);
			} catch (BadMenuIdException e) {
			}
			Menu menu = newMenu(m);
			menus.add(menu);
		}
		return menus;
	}	

	// View helpers ----------------------------------------------------------

	private Menu newMenu(MenuDomain menu) {
		Menu view = new Menu();
		MenuId id = new MenuId();
		id.setId(menu.getId());
		view.setId(id);
		view.setEntree(menu.getEntree());
		view.setPlate(menu.getPlate());
		view.setDessert(menu.getDessert());
		view.setPrice(menu.getPrice());
		view.setPreparationTime(menu.getPreparationTime());
		return view;
	}

	private MenuOrder newMenuOrder(MenuOrderDomain menuOrder) {
		MenuOrder view = new MenuOrder();
		MenuOrderId orderId = new MenuOrderId();
		MenuId menuId = new MenuId();
		orderId.setId(menuOrder.getMenuOrderId());
		menuId.setId(menuOrder.getMenuId());
		view.setId(orderId);
		view.setMenuId(menuId);
		view.setMenuQuantity(menuOrder.getQuantity());
		return view;
	}
	
	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	private void throwBadMenuId(final String message) throws BadMenuIdFault_Exception {
		BadMenuIdFault faultInfo = new BadMenuIdFault();
		faultInfo.message = message;
		throw new BadMenuIdFault_Exception(message, faultInfo);
	}

	/** Helper method to throw new BadText exception */
	private void throwBadText(final String message) throws BadTextFault_Exception {
		BadTextFault faultInfo = new BadTextFault();
		faultInfo.message = message;
		throw new BadTextFault_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new BadQuantity exception */
	private void throwBadQuantity(final String message) throws BadQuantityFault_Exception {
		BadQuantityFault faultInfo = new BadQuantityFault();
		faultInfo.message = message;
		throw new BadQuantityFault_Exception(message, faultInfo);
	}

	/** Helper method to throw new InsufficientQuantity exception */
	private void throwInsufficientQuantity(final String message) throws InsufficientQuantityFault_Exception {
		InsufficientQuantityFault faultInfo = new InsufficientQuantityFault();
		faultInfo.message = message;
		throw new InsufficientQuantityFault_Exception(message, faultInfo);
	}


}
