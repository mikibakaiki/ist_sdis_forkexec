package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;

/**
 * Test suite
 */
public class GetFoodIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws InvalidInitFault_Exception {
		// clear remote service state before all tests
		client.ctrlClear();

		// fill-in test menus
		// (since getFood is read-only the initialization below
		// can be done once for all tests in this suite)
		List<FoodInit> initFoods = new ArrayList<>();
		
		{
			Food food = new Food();
			FoodId fId = new FoodId();
			FoodInit fInit = new FoodInit();
			fId.setMenuId("M1");
			fId.setRestaurantId("T26_Restaurant1");
			food.setId(fId);
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
			FoodId fId = new FoodId();
			FoodInit fInit = new FoodInit();
			fId.setMenuId("M2");
			fId.setRestaurantId("T26_Restaurant2");
			food.setId(fId);
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
			FoodId fId = new FoodId();
			FoodInit fInit = new FoodInit();
			fId.setMenuId("M3");
			fId.setRestaurantId("T26_Restaurant1");
			food.setId(fId);
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
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		client.ctrlClear();
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	// tests
	// assertEquals(expected, actual);

	// public Food getFood(FoodId foodId) throws
	// InvalidFoodIdFault_Exception {

	// bad input tests

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodNullTest() throws InvalidFoodIdFault_Exception {
		client.getFood(null);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodEmptyTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodWhitespaceTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId(" ");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodTabTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("\t");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodNewlineTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("\n");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

	// main tests

	@Test
	public void getFoodExistsTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("M1");
		fId.setRestaurantId("T26_Restaurant1");
		Food food = client.getFood(fId);
		assertEquals("M1", food.getId().getMenuId());
		assertEquals("T26_Restaurant1",food.getId().getRestaurantId());
		assertEquals(10, food.getPrice());
		assertEquals(10, food.getPreparationTime());
		assertEquals("Entrada1", food.getEntree());
		assertEquals("Prato1", food.getPlate());
		assertEquals("Sobremesa1", food.getDessert());
		
	}

	@Test
	public void getFoodAnotherExistsTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("M2");
		fId.setRestaurantId("T26_Restaurant2");
		Food food = client.getFood(fId);
		assertEquals("M2", food.getId().getMenuId());
		assertEquals("T26_Restaurant2",food.getId().getRestaurantId());
		assertEquals(20, food.getPrice());
		assertEquals(20, food.getPreparationTime());
		assertEquals("Entrada2", food.getEntree());
		assertEquals("Prato2", food.getPlate());
		assertEquals("Sobremesa2", food.getDessert());
	}

	@Test
	public void getFoodYetAnotherExistsTest() throws InvalidFoodIdFault_Exception {
		FoodId fId = new FoodId();
		fId.setMenuId("M3");
		fId.setRestaurantId("T26_Restaurant1");
		Food food = client.getFood(fId);
		assertEquals("M3", food.getId().getMenuId());
		assertEquals("T26_Restaurant1",food.getId().getRestaurantId());
		assertEquals(30, food.getPrice());
		assertEquals(30, food.getPreparationTime());
		assertEquals("Entrada3", food.getEntree());
		assertEquals("Prato3", food.getPlate());
		assertEquals("Sobremesa3", food.getDessert());
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodNotExistsTest() throws InvalidFoodIdFault_Exception {
		// when food does not exist, null should be returned
		FoodId fId = new FoodId();
		fId.setMenuId("B4");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void getFoodLowercaseNotExistsTest() throws InvalidFoodIdFault_Exception {
		// food (menu) identifiers are case sensitive,
		// so "m1" is not the same as "M1"
		FoodId fId = new FoodId();
		fId.setMenuId("m1");
		fId.setRestaurantId("T26_Restaurant1");
		client.getFood(fId);
	}

}