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
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.NotEnoughPointsFault_Exception;


public class ClearCartIT extends BaseIT {

	// static members
	private static final String uid1 = "user1@dominio";
	private static final String uid2 = "user2@dominio";
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
			client.activateAccount(uid2);
			client.addFoodToCart(uid1, fId2, 2);
			client.addFoodToCart(uid1, fId1, 1);
			client.addFoodToCart(uid2, fId3, 1);
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void clearCart(String userId) throws
	// InvalidUserIdFault_Exception
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartNullIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartEmptyIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartWhitespaceIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartNewLineIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartTabIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartWrongIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart("Wrong");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void clearCartNonExistentIdTest() throws InvalidUserIdFault_Exception {
		client.clearCart("user4@dominio");
	}

	
	// main tests	
	
	@Test
	public void clearCartSuccessOnceTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		assertEquals(2, client.cartContents(uid1).size());
		client.clearCart(uid1);
		assertEquals(0, client.cartContents(uid1).size());
	}
	
	@Test
	public void clearCartSuccessTwiceTest() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		assertEquals(1, client.cartContents(uid2).size());
		client.clearCart(uid2);
		assertEquals(0, client.cartContents(uid2).size());
		

		try {
			client.addFoodToCart(uid2, fId2, 1);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		
		try {
			client.addFoodToCart(uid2, fId1, 1);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		}
		
		assertEquals(2, client.cartContents(uid2).size());
		client.clearCart(uid2);
		assertEquals(0, client.cartContents(uid2).size());
	}
	
}