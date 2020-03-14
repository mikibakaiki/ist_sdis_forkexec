package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

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


public class AddFoodToCartIT extends BaseIT {

	// static members
	private static final String uid1 = "user1@dominio";
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
	public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
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
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void addFoodToCart(String userId, FoodId foodId, int foodQuantity) throws
	// InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception
	
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartNullUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		client.addFoodToCart(null, fId1, 5);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartWhitespaceUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart(" ", fId1, 5);
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartEmptyUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart("", fId1, 5);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartNewLineUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart("\n", fId1, 5);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartTabUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart("\t", fId1, 5);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartNonExistentUserIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart("user2@dominio", fId1, 5);
	}
	
	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartNullFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		client.addFoodToCart(uid1, null, 5);
	}
	
	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartEmptyFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		FoodId fId = new FoodId();
		fId.setMenuId("");
		fId.setRestaurantId("T26_Restaurant1");
		client.addFoodToCart(uid1, fId, 5);
	}
	
	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartWhitespaceFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		FoodId fId = new FoodId();
		fId.setMenuId(" ");
		fId.setRestaurantId("T26_Restaurant1");
		client.addFoodToCart(uid1, fId, 5);
	}
	
	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartNewlineFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		FoodId fId = new FoodId();
		fId.setMenuId("\n");
		fId.setRestaurantId("T26_Restaurant1");
		client.addFoodToCart(uid1, fId, 5);
	}
	
	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartTabFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		FoodId fId = new FoodId();
		fId.setMenuId("\t");
		fId.setRestaurantId("T26_Restaurant1");
		client.addFoodToCart(uid1, fId, 5);
	}
	
	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void addFoodToCartNegativeQuantityTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception   {
		client.addFoodToCart(uid1, fId1, -1);
	}
	
	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void addFoodToCartZeroQuantityTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart(uid1, fId1, 0);
	}

	// main tests
	
	@Test
	public void addFoodToCartOneFoodTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.addFoodToCart(uid1, fId1, 5);
		assertEquals(1, client.cartContents(uid1).size());
		assertEquals(5, client.cartContents(uid1).get(0).getFoodQuantity());
		assertEquals(fId1.getMenuId(), client.cartContents(uid1).get(0).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(),client.cartContents(uid1).get(0).getFoodId().getRestaurantId());
	}
	
	@Test
	public void addFoodToCartVariousFoodTest() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception  {
		client.addFoodToCart(uid1, fId1, 6);
		client.addFoodToCart(uid1, fId2, 4);
		FoodOrderItem item1 = new FoodOrderItem();
		item1.setFoodId(fId1);
		item1.setFoodQuantity(5);
		FoodOrderItem item2 = new FoodOrderItem();
		item2.setFoodId(fId2);
		item2.setFoodQuantity(5);
		assertEquals(2, client.cartContents(uid1).size());
		assertEquals(6, client.cartContents(uid1).get(0).getFoodQuantity());
		assertEquals(fId1.getMenuId(), client.cartContents(uid1).get(0).getFoodId().getMenuId());
		assertEquals(fId1.getRestaurantId(),client.cartContents(uid1).get(0).getFoodId().getRestaurantId());
		assertEquals(4, client.cartContents(uid1).get(1).getFoodQuantity());
		assertEquals(fId2.getMenuId(), client.cartContents(uid1).get(1).getFoodId().getMenuId());
		assertEquals(fId2.getRestaurantId(),client.cartContents(uid1).get(1).getFoodId().getRestaurantId());
	}

}