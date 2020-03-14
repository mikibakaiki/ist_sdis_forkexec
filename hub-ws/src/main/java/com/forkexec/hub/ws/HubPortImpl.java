package com.forkexec.hub.ws;

import java.util.*;

import javax.jws.WebService;

import com.forkexec.hub.domain.*;
import com.forkexec.hub.domain.exception.*;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map"  .
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType",
            wsdlLocation = "HubService.wsdl",
            name ="HubService",
            portName = "HubPort",
            targetNamespace="http://ws.hub.forkexec.com/",
            serviceName = "HubService"
)
@SuppressWarnings("Duplicates")
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;


	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().activateAccountHub(userId);
		} catch (InvalidUserIdDomainException | EmailAlreadyExistsException | InvalidEmailException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		}
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().addPointsHub(userId, moneyToAdd, creditCardNumber);
		} catch (InvalidCreditAmountException | InvalidPointsException e) {
			throwInvalidMoneyFaultException(e.getMessage());
		} catch (InvalidCreditCardNumberException e) {
			throwInvalidCreditCardFaultException(e.getMessage());
		} catch (InvalidUserIdDomainException | InvalidEmailException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		// TODO return lowest price menus first
		List<Food> foodList = new ArrayList<>();

		try {
			List<FoodDomain> foodDomainList = Hub.getInstance().searchDealMoney(description);
			for(FoodDomain fd: foodDomainList) {
				foodList.add(newFood(fd));
			}

			return foodList;

		} catch (InvalidTextDescriptionException e) {
			throwInvalidTextFaultException(e.getMessage());
		}
		return foodList;
	}
	
	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		// TODO return lowest preparation time first
		List<Food> foodList = new ArrayList<>();

		try {
			List<FoodDomain> foodDomainList = Hub.getInstance().searchDealHungry(description);
			for(FoodDomain fd: foodDomainList) {
				foodList.add(newFood(fd));
			}

			return foodList;

		} catch (InvalidTextDescriptionException e) {
			throwInvalidTextFaultException(e.getMessage());
		}
		return foodList;
	}

	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		if (foodId == null) {
			throwInvalidFoodIdFaultException("Food identifier cannot be null!");
		}
		try {
			Hub.getInstance().addFoodToCart(userId, foodId, foodQuantity);
		} catch (InvalidQuantityException e) {
			throwInvalidFoodQuantityFaultException(e.getMessage());
		} catch (InvalidUserIdDomainException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		} catch (NonExistingMenuException e) {
			throwInvalidFoodIdFaultException(e.getMessage());
		}
	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().clearCart(userId);
		} catch (InvalidUserIdDomainException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		}
	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {
		try {
			FoodOrderDomain orderDomain = Hub.getInstance().orderCart(userId);

			FoodOrderId orderId = newFoodOrderId(orderDomain.getFoodOrderId());

			FoodOrder foodOrder = new FoodOrder();
			foodOrder.setFoodOrderId(orderId);

			for (FoodOrderItemDomain foid : orderDomain.getItems()) {
				FoodOrderItem it = newFoodOrderItem(foid);
				foodOrder.getItems().add(it);
			}
			return foodOrder;
		} catch (EmptyCartException e) {
			throwEmptyCartFaultException(e.getMessage());
		} catch (InvalidUserIdDomainException e) {
			e.printStackTrace();
			throwInvalidUserIdFaultException(e.getMessage());
		} catch (NotEnoughBalanceException e) {
			throwNotEnoughPointsFaultException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
		try {
			return Hub.getInstance().accountBalanceHub(userId);
		} catch (InvalidUserIdDomainException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		}
		return 0;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		try {
			return newFood(Hub.getInstance().getFood(foodId));
		} catch (NonExistingMenuException e) {
			throwInvalidFoodIdFaultException(e.getMessage());
		}
		return null;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {
		List<FoodOrderItem> list = new ArrayList<>();
		try {
			for (FoodOrderItemDomain c : Hub.getInstance().listCartContents(userId)) {
				list.add(newFoodOrderItem(c));
			}
			return list;
		} catch (InvalidUserIdDomainException e) {
			throwInvalidUserIdFaultException(e.getMessage());
		}
		return list;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {

		try {
			Collection<UDDIRecord> registeredRestaurants = endpointManager.
					getUddiNaming().listRecords("T26_Restaurant%");
			String answ = "";

			for (UDDIRecord rec : registeredRestaurants) {
				RestaurantClient cli = new RestaurantClient(rec.getUrl());
				String temp = cli.ctrlPing("hub");

				answ = answ + temp + "\n";
			}

			return answ;
		} catch (UDDINamingException e) {
			System.err.println("Caught exception when listing restaurants registered in hub. " + e);
		} catch (RestaurantClientException e) {
			System.err.println("Error creating a new Restaurant Client");		}
		return null;
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Hub.getInstance().clear();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		if (initialFoods.isEmpty() || initialFoods == null) {
			throwInvalidInitFaultException("eawdw");
		}
		try {
			Hub.getInstance().initFood(initialFoods);

		} catch (InvalidMenuInitException e) {
			throwInvalidInitFaultException(e.getMessage());
		}
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		try {
			Hub.getInstance().changeInitPoints(startPoints);
		} catch (InvalidStartPointsException e) {
			throwInvalidInitFaultException(e.getMessage());
		}
	}


	// View helpers ----------------------------------------------------------

	// /** Helper to convert a domain object to a view. */
	private Food newFood(FoodDomain food) {
		Food view = new Food();
		view.setId(newFoodID(food.getId()));
		view.setEntree(food.getEntree());
		view.setPlate(food.getPlate());
		view.setDessert(food.getDessert());
		view.setPrice(food.getPrice());
		view.setPreparationTime(food.getPreparationTime());
		return view;
	}

	private FoodId newFoodID(FoodIdDomain food) {
		FoodId view = new FoodId();
		view.setRestaurantId(food.getRestaurantId());
		view.setMenuId(food.getMenuId());
		return view;
	}

	private FoodOrderItem newFoodOrderItem(FoodOrderItemDomain foid) {
		FoodOrderItem view = new FoodOrderItem();
		view.setFoodId(newFoodID(foid.getFoodId()));
		view.setFoodQuantity(foid.getFoodQuantity());
		return view;
	}

	private FoodOrderId newFoodOrderId (FoodOrderIdDomain order) {
		FoodOrderId view = new FoodOrderId();
		view.setId(order.getId());
		return view;
	}



	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new InvalidUserIdFault exception. */
	private void throwInvalidUserIdFaultException(final String message) throws InvalidUserIdFault_Exception{
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.message = message;
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new EmptyCartFault exception. */
	private void throwEmptyCartFaultException(final String message) throws EmptyCartFault_Exception {
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.message = message;
		throw new EmptyCartFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new NotEnoughPointsFault exception. */
	private void throwNotEnoughPointsFaultException(final String message) throws NotEnoughPointsFault_Exception {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.message = message;
		throw new NotEnoughPointsFault_Exception(message, faultInfo);

	}

	/** Helper to throw a new InvalidInitFault exception. */
	private void throwInvalidInitFaultException(final String message) throws InvalidInitFault_Exception {
		InvalidInitFault faultInfo = new InvalidInitFault();
		faultInfo.message = message;
		throw new InvalidInitFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidFoodIdFault exception. */
	private void throwInvalidFoodIdFaultException(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.message = message;
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidTextFault exception. */
	private void throwInvalidTextFaultException(final String message) throws InvalidTextFault_Exception {
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.message = message;
		throw new InvalidTextFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidFoodQuantityFault exception. */
	private void throwInvalidFoodQuantityFaultException(final String message) throws InvalidFoodQuantityFault_Exception {
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.message = message;
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidCreditCardFault exception. */
	private void throwInvalidCreditCardFaultException(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.message = message;
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidMoneyFault exception. */
	private void throwInvalidMoneyFaultException(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.message = message;
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}

}
