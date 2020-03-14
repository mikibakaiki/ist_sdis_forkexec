package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidTextFault_Exception;


/**
 * Test suite
 */

public class SearchDealIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws InvalidInitFault_Exception {
		// clear remote service state before all tests
		client.ctrlClear();

		// fill-in test foods
		// (since searchDeal is read-only the initialization below
		// can be done once for all tests in this suite)
		//List<Food> foods = new ArrayList<>();
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
		{ //neste caso o Prato e Sobremesa estao intencionalmente trocados
			Food food = new Food();
			FoodId fId = new FoodId();
			FoodInit fInit = new FoodInit();
			fId.setMenuId("B4");
			fId.setRestaurantId("T26_Restaurant2");
			food.setId(fId);
			food.setEntree("Entree4");
			food.setPlate("Sobremesa4");
			food.setDessert("Prato4");
			food.setPrice(25);
			food.setPreparationTime(17);
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

	// public List<Food> searchDeal(String description) throws
	// InvalidTextFault_Exception

	// bad input tests

	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealNullTest() throws InvalidTextFault_Exception {
		client.searchDeal(null);
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealEmptyTest() throws InvalidTextFault_Exception {
		client.searchDeal("");
	}
	
	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealSpaceAtStartTest() throws InvalidTextFault_Exception {
		client.searchDeal(" Bacalhau");
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealSpaceAtTheEndTest() throws InvalidTextFault_Exception {
		client.searchDeal("Bacalhau ");
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealSpaceInTheMiddleTest() throws InvalidTextFault_Exception {
		client.searchDeal("Bacalhau a Bras");
	}
	
	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealNewLineTest() throws InvalidTextFault_Exception {
		client.searchDeal("Bacalhau\n");
	}
	
	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealTabTest() throws InvalidTextFault_Exception {
		client.searchDeal("Bacalhau\t");
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void searchDealCarriageTest() throws InvalidTextFault_Exception {
		client.searchDeal("Bacalhau\r");
	}

	// main tests

	@Test
	public void searchDealOneMatchTest() throws InvalidTextFault_Exception {
		List<Food> foods = client.searchDeal("Prato1");
		assertNotNull(foods);
		assertEquals(1, foods.size());

		Food food = foods.get(0);
		assertEquals("M1", food.getId().getMenuId());
		assertEquals("T26_Restaurant1",food.getId().getRestaurantId());
		assertEquals(10, food.getPrice());
		assertEquals(10, food.getPreparationTime());
		assertEquals("Entrada1", food.getEntree());
		assertEquals("Prato1", food.getPlate());
		assertEquals("Sobremesa1", food.getDessert());
	}

	@Test
	public void searchDealAllMatchTest() throws InvalidTextFault_Exception {
		List<Food> foods = client.searchDeal("Prato");
		assertNotNull(foods);
		assertEquals(4, foods.size());

		// no ordering is imposed on results
		// check if menu plate all contain the search term Prato
		for (Food food : foods) {
			assertTrue(food.getPlate().indexOf("rato") == 1 || food.getDessert().indexOf("rato") == 1);
		}
		//verify if the order is right
		assertEquals("M1", foods.get(0).getId().getMenuId());
		assertEquals("Entrada2", foods.get(1).getEntree());
		assertEquals("Prato4", foods.get(2).getDessert());
		assertEquals("Prato3", foods.get(3).getPlate());
	}

	@Test
	public void searchDealSomeMatchTest() throws InvalidTextFault_Exception {
		List<Food> foods = client.searchDeal("Entrada");
		assertNotNull(foods);
		assertEquals(3, foods.size());

		// no ordering is imposed on results
		// check if food entrees on results all contain the search term Entrada
		for (Food food : foods) {
			assertTrue(food.getEntree().indexOf("trada") == 2);
		}
		//verify if the order is right
		assertEquals("M1", foods.get(0).getId().getMenuId());
		assertEquals("Entrada2", foods.get(1).getEntree());
		assertEquals("Prato3", foods.get(2).getPlate());
	}

	@Test
	public void searchDealNoMatchTest() throws InvalidTextFault_Exception {
		List<Food> foods = client.searchDeal("RANDOm");
		// when menus are not found,
		// an empty list should be returned (not null)
		assertNotNull(foods);
		assertEquals(0, foods.size());
	}

	@Test
	public void searchDealLowerCaseTest() throws InvalidTextFault_Exception {
		// food entrees, plates and desserts are case sensitive,
		// so "entrada" is not the same as "Entrada"
		List<Food> foods = client.searchDeal("entrada");
		assertNotNull(foods);
		assertEquals(0, foods.size());
	}

	@Test
	public void searchDealPricePrepTimeTest() throws InvalidTextFault_Exception {
		// assure price and preparation time are not swapped
		List<Food> foods = client.searchDeal("Entree4");
		assertNotNull(foods);
		assertEquals(1, foods.size());

		Food food = foods.get(0);
		assertEquals("B4", food.getId().getMenuId());
		assertEquals("T26_Restaurant2",food.getId().getRestaurantId());
		assertEquals(25, food.getPrice());
		assertEquals(17, food.getPreparationTime());
		assertEquals("Entree4", food.getEntree());
		assertEquals("Sobremesa4", food.getPlate());
		assertEquals("Prato4", food.getDessert());
	}

}
