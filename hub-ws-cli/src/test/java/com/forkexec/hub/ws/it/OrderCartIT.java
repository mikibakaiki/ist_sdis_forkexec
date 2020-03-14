package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.EmptyCartFault_Exception;
import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.FoodOrder;
import com.forkexec.hub.ws.FoodOrderItem;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.NotEnoughPointsFault_Exception;


public class OrderCartIT extends BaseIT {

	// static members
	private static final String uid1 = "user1@dominio";
	private static final String uid2 = "user2@dominio";
	private static final String uid3 = "user3@dominio";
	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		client.ctrlClear();
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members
	List<FoodInit> initFoods = new ArrayList<>();
	FoodId fId1 = new FoodId();
	FoodId fId2 = new FoodId();
	FoodId fId3 = new FoodId();
	// initialization and clean-up for each test
	@Before
	public void setUp() throws InvalidInitFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		{

			{
				Food food = new Food();
				FoodInit fInit = new FoodInit();
				fId1.setMenuId("M1");
				fId1.setRestaurantId("T26_Restaurant1");
				food.setId(fId1);
				food.setEntree("Entrada1");
				food.setPlate("Prato1");
				food.setDessert("Sobremesa1");
				food.setPrice(10);
				food.setPreparationTime(10);
				fInit.setFood(food);
				fInit.setQuantity(10);
				initFoods.add(fInit);			
			}
			{
				Food food = new Food();
				FoodInit fInit = new FoodInit();
				fId2.setMenuId("M2");
				fId2.setRestaurantId("T26_Restaurant2");
				food.setId(fId2);
				food.setEntree("Entrada2");
				food.setPlate("Prato2");
				food.setDessert("Sobremesa2");
				food.setPrice(20);
				food.setPreparationTime(20);
				fInit.setFood(food);
				fInit.setQuantity(20);
				initFoods.add(fInit);
			}
			{
				Food food = new Food();
				FoodInit fInit = new FoodInit();
				fId3.setMenuId("M3");
				fId3.setRestaurantId("T26_Restaurant1");
				food.setId(fId3);
				food.setEntree("Entrada3");
				food.setPlate("Prato3");
				food.setDessert("Sobremesa3");
				food.setPrice(30);
				food.setPreparationTime(30);
				fInit.setFood(food);
				fInit.setQuantity(30);
				initFoods.add(fInit);
			}
			client.ctrlInitFood(initFoods);
			client.activateAccount(uid1);
			client.addFoodToCart(uid1, fId2, 2);
			client.addFoodToCart(uid1, fId1, 1);
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public FoodOrder orderCart(String userId) throws
	// EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartNullIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception  {
		client.orderCart(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartEmptyIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartWhitespaceIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception  {
		client.orderCart(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartNewLineIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartTabIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartWrongIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("Wrong");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void orderCartNonExistentIdTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("user4@dominio");
	}
	
	@Test(expected = EmptyCartFault_Exception.class)
	public void orderCartEmptyCartTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.activateAccount(uid2);
		client.orderCart(uid2);
	}

	@Test(expected = NotEnoughPointsFault_Exception.class)
	public void orderCartOnceFewPointsTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.activateAccount(uid3);
		try {
			client.addFoodToCart(uid3, fId3, 10);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		client.orderCart(uid3);
	}

	@Test(expected = NotEnoughPointsFault_Exception.class)
	public void orderCartTwiceFewPointsTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.activateAccount(uid3);
		try {
			client.addFoodToCart(uid3, fId3, 3);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		
		//primeira order deve passar
		try{
			client.orderCart(uid3);
		} catch (NotEnoughPointsFault_Exception nepe) {
			fail(nepe.getMessage());
		}
		
		try {
			client.addFoodToCart(uid3, fId3, 3);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		client.orderCart(uid3); //aqui e' que e suposto falhar
	}
	
	// main tests	
	
	@Test
	public void orderCartSuccessOnceTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		FoodOrder fo = client.orderCart(uid1);
		assertEquals(2, fo.getItems().size());
		assertEquals(fId2.getMenuId(), fo.getItems().get(0).getFoodId().getMenuId());
		assertEquals(fId2.getRestaurantId(), fo.getItems().get(0).getFoodId().getRestaurantId());
		assertEquals(2, fo.getItems().get(0).getFoodQuantity());
		assertEquals(fId1.getMenuId(), fo.getItems().get(1).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(), fo.getItems().get(1).getFoodId().getRestaurantId());
		assertEquals(1, fo.getItems().get(1).getFoodQuantity());
	}
	
	@Test
	public void orderCartSuccessTwiceTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		FoodOrder fo1 = client.orderCart(uid1);
		assertEquals(2, fo1.getItems().size());
		assertEquals(fId2.getMenuId(), fo1.getItems().get(0).getFoodId().getMenuId());
		assertEquals(fId2.getRestaurantId(), fo1.getItems().get(0).getFoodId().getRestaurantId());
		assertEquals(2, fo1.getItems().get(0).getFoodQuantity());
		assertEquals(fId1.getMenuId(), fo1.getItems().get(1).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(), fo1.getItems().get(1).getFoodId().getRestaurantId());
		assertEquals(1, fo1.getItems().get(1).getFoodQuantity());
		try {
			client.addFoodToCart(uid1, fId3, 1);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		FoodOrder fo2 = client.orderCart(uid1);
		assertEquals(1, fo2.getItems().size());
		assertEquals(fId3.getMenuId(), fo2.getItems().get(0).getFoodId().getMenuId());
		assertEquals(fId3.getRestaurantId(), fo2.getItems().get(0).getFoodId().getRestaurantId());
		assertEquals(1, fo2.getItems().get(0).getFoodQuantity());
	}
	
}