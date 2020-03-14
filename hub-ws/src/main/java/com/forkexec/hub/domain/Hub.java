package com.forkexec.hub.domain;


import com.forkexec.cc.ws.cli.CCClient;
import com.forkexec.cc.ws.cli.CCClientException;
import com.forkexec.hub.domain.exception.*;
import com.forkexec.hub.ws.*;
import com.forkexec.pts.ws.*;
import com.forkexec.pts.ws.cli.PointsFrontEnd;
import com.forkexec.rst.ws.*;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
@SuppressWarnings("Duplicates")
public class Hub {


	// Singleton -------------------------------------------------------------

	private UDDINaming binder;
	private ConcurrentHashMap<String, Cart> usersCarts = new ConcurrentHashMap<>();
	private String uddiURL;
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private Hub() {
		// Initialization of default values
		initUDDISearch();
		uddiURL = getUDDIUrlHub();
	}


	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}



	/** Method that activates a new account, if the userid is not yet registered.
	 *
	 * @param userId
	 * @throws Throwable 
	 */

	public void activateAccountHub(String userId)
			throws InvalidUserIdDomainException, EmailAlreadyExistsException, InvalidEmailException {
		try {
			if (checkUserName(userId)) {
				PointsFrontEnd cli = new PointsFrontEnd(uddiURL);
				System.out.println("@ActivateAccountHub server url: "+uddiURL);
				cli.activateUser(userId);
				Cart newUserCart = new Cart(userId);
				usersCarts.put(userId, newUserCart);
			}
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the URL for the T26_PTS server.");
		} catch (InvalidUserIdDomainException e) {
			throw new InvalidUserIdDomainException(e.getMessage());
		} catch (EmailAlreadyExistsFault_Exception e) {
			throw new EmailAlreadyExistsException("Error: The email " + userId + " already exists.");
		} catch (com.forkexec.pts.ws.cli.InvalidEmailFault_Exception e) {
			throw new InvalidEmailException("Error: The email " + userId + " is invalid.");
		} catch (Throwable e) {
			System.err.println("Got unexpected throwable: "+ e.getCause());
		}
	}



	/** Method that adds the given points to the account of a given username.
	 *
	 * @param userId
	 * @param moneyToAdd
	 * @param creditCardNumber
	 * @throws InvalidCreditAmountException
	 * @throws InvalidCreditCardNumberException
	 * @throws InvalidUserIdDomainException
	 */

	public void addPointsHub(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditAmountException, InvalidCreditCardNumberException, InvalidUserIdDomainException, InvalidEmailException, InvalidPointsException {
		int points = 0;
		boolean validCreditCard;
		try {
			if (checkUserName(userId) && creditCardNumber != null && !creditCardNumber.isEmpty()) {
				String ccServerUrl = binder.lookup("CC");

				try {
					points = convertMoneyToPointsHub(moneyToAdd);
					validCreditCard = verifyCreditCardHub(ccServerUrl, creditCardNumber);

					try {
						if (validCreditCard) {
							PointsFrontEnd cli = new PointsFrontEnd(uddiURL);
							cli.addPoints(userId, points);
						}
					} catch (com.forkexec.pts.ws.cli.InvalidEmailFault_Exception e) {
						throw new InvalidEmailException("Error: The email" + userId + " is invalid.");
					} catch (com.forkexec.pts.ws.cli.InvalidPointsFault_Exception e) {
						throw new InvalidPointsException("Error: Can't load the number " + points + " of points.");
					}

				} catch (InvalidCreditAmountException e) {
					throw new InvalidCreditAmountException(e.getMessage());
				} catch (InvalidCreditCardNumberException e) {
					throw new InvalidCreditCardNumberException(e.getMessage());
				}
			} else {
				throw new InvalidCreditCardNumberException("CardNumber cannot be null nor empty.");
			}
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for either PTS or CC servers.");
		}
	}




	/** Method that searches for the fastest meal and returns every meal ordered in crescent order,
	 *  whose names match the description.
	 *
	 * @param description
	 * @return hungryList - List containing the ordered meals, according to how fast they are prepared.
	 * @throws InvalidTextDescriptionException
	 */
	public List<FoodDomain> searchDealHungry(String description) throws InvalidTextDescriptionException {
		List<FoodDomain> hungryList;

		// searchDeal already checks if description is either null or has spaces.
		hungryList = searchDeal(description);

		Collections.sort(hungryList, new Comparator<FoodDomain>() {
			@Override
			public int compare(FoodDomain f1, FoodDomain f2) {
				return f1.getPreparationTime()-f2.getPreparationTime();
			}
		});

		return hungryList;
	}

	/** Method that searches for the cheapest meal and returns every meal ordered in crescent order,
	 *  whose names match the description.
	 *
	 * @param description
	 * @return
	 * @throws InvalidTextDescriptionException
	 */
	public List<FoodDomain> searchDealMoney(String description) throws InvalidTextDescriptionException {
		List<FoodDomain> moneyList;

		// searchDeal already checks if description is either null or has spaces.
		moneyList = searchDeal(description);

		Collections.sort(moneyList, new Comparator<FoodDomain>() {
			@Override
			public int compare(FoodDomain f1, FoodDomain f2) {
				return f1.getPrice()-f2.getPrice();
			}
		});

		return moneyList;

	}



	/** Method that gets the balance, from T26_PTS server, of a given user.
	 *
	 * @param userId
	 * @return
	 * @throws InvalidUserIdDomainException
	 */
	public int accountBalanceHub(String userId) throws InvalidUserIdDomainException{
		try {
			if (checkUserName(userId)) {
				PointsFrontEnd cli = new PointsFrontEnd(uddiURL);

				return cli.pointsBalance(userId);
			}
		} catch (com.forkexec.pts.ws.cli.InvalidEmailFault_Exception e) {
			throw new InvalidUserIdDomainException(e.getMessage());
		}
		return 0;
	}


	public void initFood(List<FoodInit> initialFoods) throws InvalidMenuInitException {

		if ( initialFoods != null) {
			Map<String, List<MenuInit>> menuList = new HashMap<>();

			String restId;

			for (FoodInit fi : initialFoods) {
				restId = fi.getFood().getId().getRestaurantId();

				Menu m = convertFoodToMenu(fi.getFood());
				MenuInit mi = new MenuInit();
				mi.setMenu(m);
				mi.setQuantity(fi.getQuantity());

				if (!menuList.containsKey(restId)) {
					menuList.put(restId, new ArrayList<>());
					menuList.get(restId).add(mi);
				} else {
					menuList.get(restId).add(mi);
				}
			}
			sendToRst(menuList);
		} else {
			throw new InvalidMenuInitException("List cannot be null");
		}
	}


	public void clearCart(String userId) throws InvalidUserIdDomainException {
		try {
			if (checkUserName(userId)) {
				if (!usersCarts.containsKey(userId)) {
					throw new InvalidUserIdDomainException("This user doesn't exist.");
				}
				usersCarts.get(userId).clearCart();
			}
		} catch (InvalidUserIdDomainException e) {
			throw new InvalidUserIdDomainException(e.getMessage());
		}
	}

	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidQuantityException, NonExistingMenuException, InvalidUserIdDomainException {
		try {
			if (checkUserName(userId) && foodId != null) {
				String rstUrl = binder.lookup(foodId.getRestaurantId());
				RestaurantClient cli = new RestaurantClient(rstUrl);

				MenuId mi = new MenuId();
				mi.setId(foodId.getMenuId());

				Menu m;

				try {
					m = cli.getMenu(mi);
				} catch (BadMenuIdFault_Exception e) {
					throw new NonExistingMenuException("Error: there is no menu with id "
							+ foodId.getMenuId() + " on restaurant " + foodId.getRestaurantId() + ".");
				}

				if (foodQuantity < 1) {
					throw new InvalidQuantityException("Error: quantity " + foodQuantity + " is invalid.");
				}
				FoodIdDomain fid = convertFoodIdToFoodIdDomain(foodId);
				FoodDomain fd = new FoodDomain(fid, m.getEntree(),m.getPlate(),
						m.getDessert(), m.getPrice(), m.getPreparationTime(), foodQuantity);


				if (!usersCarts.containsKey(userId)) {
					throw new InvalidUserIdDomainException("This user doesn't exist.");
				}
				usersCarts.get(userId).addToCart(fd);
			} else {
				throw new NonExistingMenuException("FoodId cannot be null");
			}
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for one of " + foodId.getRestaurantId() + ".");
		} catch (RestaurantClientException e) {
			System.err.println("Error creating client for " + foodId.getRestaurantId() + ".");
		}
	}


	public FoodDomain getFood(FoodId foodId) throws NonExistingMenuException {
		try {
			if (foodId != null) {
				String rstUrl = binder.lookup(foodId.getRestaurantId());

				RestaurantClient cli = new RestaurantClient(rstUrl);

				MenuId mi = new MenuId();
				mi.setId(foodId.getMenuId());
				Menu m;
				try {
					m = cli.getMenu(mi);
				} catch (BadMenuIdFault_Exception e) {
					throw new NonExistingMenuException(e.getMessage());
				}

				FoodIdDomain fid= convertFoodIdToFoodIdDomain(foodId);

				FoodDomain fd = new FoodDomain(fid, m.getEntree(), m.getPlate(),
						m.getDessert(),m.getPrice(), m.getPreparationTime());

				return fd;
			} else {
				throw new NonExistingMenuException("FoodId cannot be null");
			}
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for " + foodId.getRestaurantId() + "'s server.");
		} catch (RestaurantClientException e) {
			System.err.println("Error creating client for " + foodId.getRestaurantId() + ".");
		}
		return null;
	}

	public void changeInitPoints(int startPoints) throws InvalidStartPointsException {

		PointsFrontEnd cli = new PointsFrontEnd(uddiURL);
		try {
			cli.ctrlInit(startPoints);
		} catch (com.forkexec.pts.ws.BadInitFault_Exception e) {
			throw new InvalidStartPointsException("Error: Invalid number of points.");
		}
	}

	public List<FoodOrderItemDomain> listCartContents (String userId) throws InvalidUserIdDomainException {

		List<FoodOrderItemDomain> foodOrderList = new ArrayList<>();

		if (checkUserName(userId)) {
			if (!usersCarts.containsKey(userId)) {
				throw new InvalidUserIdDomainException("This is user doesn't exist.");
			}

			List<FoodDomain> cart = usersCarts.get(userId).getCart();

			for (FoodDomain fd : cart) {
				FoodOrderItemDomain foid = convertFoodDomainToFoodOrderItemDomain(fd);
				foodOrderList.add(foid);
			}
			return foodOrderList;
		}
		return foodOrderList;
	}

	public void clear() {
		try {
			try {
				Collection<UDDIRecord> registeredRestaurants = binder.listRecords("T26_Restaurant%");

				for (UDDIRecord rec : registeredRestaurants) {
					RestaurantClient cli = new RestaurantClient(rec.getUrl());
					cli.ctrlClear();
				}
			} catch (RestaurantClientException e) {
				System.err.println("Error creating Restaurant client.");
			}
			
			PointsFrontEnd cli = new PointsFrontEnd(uddiURL);
			cli.ctrlClear();

			usersCarts.clear();
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for the UDDI server.");
		}
	}

	public FoodOrderDomain orderCart(String userId) throws EmptyCartException, NonExistingMenuException,
			InvalidQuantityException, InsufficientQuantityException, InvalidEmailException,
			InvalidPointsException, NotEnoughBalanceException, InvalidUserIdDomainException {

		int totalPoints = 0;

		List<FoodOrderItemDomain> allOrdersMade = new ArrayList<>();

		if (checkUserName(userId)) {

			if(!usersCarts.containsKey(userId)) {
				throw new InvalidUserIdDomainException("User doesn't exist.");
			}
			Cart cart = usersCarts.get(userId);

			List<FoodDomain> foods = cart.getCart();

			if (foods.isEmpty()) {
				throw new EmptyCartException("The cart for user " + userId + " is empty.");
			} else {

				for (FoodDomain fd : foods) {
					FoodIdDomain foodId = fd.getId();
					int qtd = fd.getQuantity();

					Menu m = convertFoodDomainToMenu(fd);

					MenuOrder menuOrder = confirmOrderRst(foodId.getRestaurantId(), m.getId(), qtd);

					if (menuOrder != null) {
						totalPoints += menuOrder.getMenuQuantity() * fd.getPrice();
					}

					FoodOrderItemDomain orderItem = new FoodOrderItemDomain(foodId, qtd);
					allOrdersMade.add(orderItem);
				}

				if (confirmOrderPts(userId, totalPoints) != -1) {
					FoodOrderIdDomain orderId = new FoodOrderIdDomain();
					FoodOrderDomain orderDomain = new FoodOrderDomain(orderId);
					for (FoodOrderItemDomain i : allOrdersMade) {
						orderDomain.getItems().add(i);
					}
					usersCarts.get(userId).clearCart();
					return orderDomain;
				}
			}
		}
		return null;
	}


	//*******************************************************//
	//*            		Auxiliary Methods					*//
	//*******************************************************//

	private void initUDDISearch () {
		try {
			binder = new UDDINaming(getUDDIUrlHub());
		} catch (UDDINamingException e){
			System.err.println("Couldn't find the UDDI @ " + getUDDIUrlHub());
		}
	}

	private String getUDDIUrlHub() {
		Properties p = new Properties();

		try {
			p.load(Hub.class.getClassLoader().getResourceAsStream("project.properties"));
			return p.getProperty("uddi.url");
		} catch (IOException e) {
			System.err.println("ERROR ON THE URL");
		}
		return "";
	}


	/** Method that does a simple validity check on user id's.
	 *  Mainly checks if input userid is not null nor empty.
	 * @param userId
	 * @return
	 * @throws InvalidUserIdDomainException
	 */
	private boolean checkUserName(String userId) throws InvalidUserIdDomainException {
		if (userId == null ) {
			throw new InvalidUserIdDomainException("The username can't be null.");
		}
		String auxString = userId;
		auxString = auxString.trim();
		if (auxString.length() == 0) {
			throw new InvalidUserIdDomainException("The username can't be empty.");
		}
		return true;
	}


	/** Method that validates if the money given is convertible to points.
	 *
	 * @param money - money quantity to convert to points.
	 * @return points - converted quantity of points from money given.
	 * @throws InvalidCreditAmountException
	 */

	private int convertMoneyToPointsHub(int money) throws InvalidCreditAmountException {
		int points;
		if (money == 10) {
			points = 1000;
		} else if (money == 20) {
			points = 2100;
		} else if (money == 30) {
			points = 3300;
		} else if (money == 50) {
			points = 5500;
		} else {
			throw new InvalidCreditAmountException("Error: Can't convert the amount: " + money + " to points.");
		}
		return points;
	}

	/** Method that validates a given user's creditCardNumber.
	 *
	 * @param ccUrl
	 * @param creditCardNumber
	 * @return
	 * @throws InvalidCreditCardNumberException
	 */

	private boolean verifyCreditCardHub(String ccUrl, String creditCardNumber) throws InvalidCreditCardNumberException {
		try {
			CCClient ccClient = new CCClient(ccUrl);
			boolean valid;

			valid = ccClient.validateNumber(creditCardNumber);

			if (!valid) {
				throw new InvalidCreditCardNumberException("Error: The credit card number " + creditCardNumber + " is invalid.");
			}
			return valid;
		} catch (CCClientException e) {
			System.err.println("Error creating a CC Client.");
		}
		return false;
	}



	private void sendToRst(Map<String, List<MenuInit>> list) throws InvalidMenuInitException {
		try {
			Set<String> restaurantsSet = list.keySet();
			for (String s : restaurantsSet) {
				String url = binder.lookup(s);
				RestaurantClient cli = new RestaurantClient(url);

				cli.ctrlInit(list.get(s));
			}
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for the RST server.");
		} catch (RestaurantClientException e) {
			System.err.println("Error creating a new RST client");
		} catch (com.forkexec.rst.ws.BadInitFault_Exception e) {
			throw new InvalidMenuInitException(e.getMessage());
		}
	}

	private MenuOrder confirmOrderRst(String restId, MenuId menuId, int qtd)
			throws NonExistingMenuException, InvalidQuantityException, InsufficientQuantityException {
		try {
			String url = binder.lookup(restId);
			RestaurantClient cli = new RestaurantClient(url);
			return cli.orderMenu(menuId, qtd);
		} catch (UDDINamingException e) {
			System.err.println("Error retrieving the url for the UDDI server.");
		} catch (RestaurantClientException e) {
			System.err.println("Error creating " + restId + " client.");
		} catch (BadMenuIdFault_Exception e) {
			throw new NonExistingMenuException(e.getMessage());
		} catch (BadQuantityFault_Exception e) {
			throw new InvalidQuantityException(e.getMessage());
		} catch (InsufficientQuantityFault_Exception e) {
			throw new InsufficientQuantityException(e.getMessage());
		}

		return null;
	}

	private int confirmOrderPts(String userId, int points)
			throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		try {
			PointsFrontEnd cli = new PointsFrontEnd(uddiURL);
			return cli.spendPoints(userId, points);
		} catch (com.forkexec.pts.ws.cli.InvalidEmailFault_Exception e) {
			throw new InvalidEmailException(e.getMessage());
		} catch (com.forkexec.pts.ws.cli.InvalidPointsFault_Exception e) {
			throw new InvalidPointsException(e.getMessage());
		} catch (com.forkexec.pts.ws.cli.NotEnoughBalanceFault_Exception e) {
			throw new NotEnoughBalanceException(e.getMessage());
		}
	}

	/** Method that searches and returns every meal available from all restaurants,
	 *  whose names match the description.
	 *
	 * @param description -
	 * @return foodList - contains all available meals from all restaurants
	 * @throws InvalidTextDescriptionException
	 */
	private List<FoodDomain> searchDeal(String description) throws InvalidTextDescriptionException {
		List<FoodDomain> foodList = new ArrayList<>();

		try {
			if (description == null || description.trim().length() == 0) {
				throw new InvalidTextDescriptionException("The description " + description + " is invalid.");
			} else if (description.contains(" ") || description.contains("\t") || description.contains("\n") || description.contains("\r")) {
				throw new InvalidTextDescriptionException("The description " + description + " is invalid.");
			}

			Collection<UDDIRecord> listRestaurants = binder.listRecords("T26_Restaurant%");

			for (UDDIRecord rec : listRestaurants) {
				foodList.addAll(getDealsForRestaurant(rec.getUrl(), rec.getOrgName(), description));
			}
			return foodList;
		} catch (UDDINamingException e) {
			System.err.println("Caught exception when listing restaurants registered in hub. " + e);
		}
		return foodList;
	}

	/** Method that searches a restaurant for all the meals that match the given description.
	 *
	 * @param url
	 * @param restaurantName
	 * @param description
	 * @return
	 * @throws InvalidTextDescriptionException
	 */
	private List<FoodDomain> getDealsForRestaurant(String url, String restaurantName, String description)
			throws InvalidTextDescriptionException {
		List<FoodDomain> foodList = new ArrayList<>();

		try {
			if (url != null && restaurantName != null) {
				RestaurantClient rstCli = new RestaurantClient(url);

				// searchDeal already checks if description is either null or has spaces.
				List<Menu> menusList = rstCli.searchMenus(description);

				for (Menu m : menusList) {
					FoodIdDomain fid = new FoodIdDomain(restaurantName, m.getId().getId());
					FoodDomain f = new FoodDomain(fid, m.getEntree(), m.getPlate(), m.getDessert(), m.getPrice(), m.getPreparationTime());

					foodList.add(f);
				}
				return foodList;
			}
		} catch (RestaurantClientException e) {
			System.err.println("Error creating a new Restaurant Client");
		} catch (BadTextFault_Exception e) {
			throw new InvalidTextDescriptionException(e.getMessage());
		}
		return foodList;
	}

	//*******************************************************//
	//*            			Object Mappers					*//
	//*******************************************************//


	private Menu convertFoodToMenu(Food f) {
		Menu m = new Menu();

		FoodId fi = f.getId();
		String menuId = fi.getMenuId();

		String entree = f.getEntree();
		String plate = f.getPlate();
		String dessert = f.getDessert();
		int price = f.getPrice();
		int preparationTime = f.getPreparationTime();

		MenuId mi = new MenuId();
		mi.setId(menuId);
		m.setId(mi);
		m.setEntree(entree);
		m.setPlate(plate);
		m.setDessert(dessert);
		m.setPrice(price);
		m.setPreparationTime(preparationTime);
		
		return m;
	}

	private FoodIdDomain convertFoodIdToFoodIdDomain(FoodId foodId) {

		FoodIdDomain fid = new FoodIdDomain(foodId.getRestaurantId(), foodId.getMenuId());
		return fid;
	}

	private FoodOrderItemDomain convertFoodDomainToFoodOrderItemDomain(FoodDomain food) {
		return new FoodOrderItemDomain(food.getId(), food.getQuantity());
	}

	private Menu convertFoodDomainToMenu(FoodDomain fd) {
		Menu m = new Menu();
		m.setId(convertFoodIdDomainToMenuId(fd.getId()));
		m.setEntree(fd.getEntree());
		m.setPlate(fd.getPlate());
		m.setDessert(fd.getDessert());
		m.setPrice(fd.getPrice());
		m.setPreparationTime(fd.getPrice());

		return m;
	}

	private MenuId convertFoodIdDomainToMenuId(FoodIdDomain fid) {
		MenuId mi = new MenuId();
		mi.setId(fid.getMenuId());
		return mi;
	}
}

