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

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.FoodOrderItem;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;


public class CartContentsIT extends BaseIT {

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
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public List<FoodOrderItem> cartContents(String userId) throws
	// InvalidUserIdFault_Exception
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsNullIdTest() throws InvalidUserIdFault_Exception   {
		client.cartContents(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsEmptyIdTest() throws InvalidUserIdFault_Exception  {
		client.cartContents("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsWhitespaceIdTest() throws InvalidUserIdFault_Exception  {
		client.cartContents(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsNewLineIdTest() throws InvalidUserIdFault_Exception {
		client.cartContents("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsTabIdTest() throws InvalidUserIdFault_Exception {
		client.cartContents("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsWrongIdTest() throws InvalidUserIdFault_Exception {
		client.cartContents("Wrong");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void cartContentsNonExistentIdTest() throws InvalidUserIdFault_Exception  {
		client.cartContents("user4@dominio");
	}
	
	// main tests	
	
	@Test
	public void cartContentsSuccessOnceTest() throws InvalidUserIdFault_Exception  {
		List<FoodOrderItem> list = client.cartContents(uid1);
		assertEquals(2, list.size());
		assertEquals(2, list.get(0).getFoodQuantity());
		assertEquals(fId2.getMenuId(), list.get(0).getFoodId().getMenuId());
		assertEquals(fId2.getRestaurantId(), list.get(0).getFoodId().getRestaurantId());
		assertEquals(1, list.get(1).getFoodQuantity());
		assertEquals(fId1.getMenuId(), list.get(1).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(), list.get(1).getFoodId().getRestaurantId());
	}
	
	@Test
	public void cartContentsSuccessTwiceTest() throws InvalidUserIdFault_Exception  {
		List<FoodOrderItem> list = client.cartContents(uid1);
		FoodOrderItem item1 = new FoodOrderItem();
		item1.setFoodId(fId2);
		item1.setFoodQuantity(2);
		FoodOrderItem item2 = new FoodOrderItem();
		item2.setFoodId(fId1);
		item2.setFoodQuantity(1);
		assertEquals(2, list.size());
		assertEquals(2, list.get(0).getFoodQuantity());
		assertEquals(fId2.getMenuId(), list.get(0).getFoodId().getMenuId());
		assertEquals(fId2.getRestaurantId(), list.get(0).getFoodId().getRestaurantId());
		assertEquals(1, list.get(1).getFoodQuantity());
		assertEquals(fId1.getMenuId(), list.get(1).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(), list.get(1).getFoodId().getRestaurantId());
		try {
			client.addFoodToCart(uid1, fId3, 1);
		} catch (InvalidFoodIdFault_Exception ifide) {
			fail(ifide.getMessage());
		} catch (InvalidFoodQuantityFault_Exception ifqe) {
			fail(ifqe.getMessage());
		}
		list = client.cartContents(uid1);
		assertEquals(3, list.size());
		assertEquals(1, list.get(2).getFoodQuantity());
		assertEquals(fId3.getMenuId(), list.get(2).getFoodId().getMenuId());
		assertEquals(fId3.getRestaurantId(), list.get(2).getFoodId().getRestaurantId());
	}

	@Test
	public void cartContentsEmptyCartTest() throws InvalidUserIdFault_Exception  {
		List<FoodOrderItem> list = client.cartContents(uid2);
		assertEquals(0, list.size());
	}
}