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
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidTextFault_Exception;


public class InitFoodIT extends BaseIT {
	
	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp(){
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
	FoodId fId4 = new FoodId();
	// initialization and clean-up for each test
	@Before
	public void setUp() throws InvalidInitFault_Exception {
		
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
		if(initFoods == null) {			
			System.err.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
		}
		else {
			client.ctrlInitFood(initFoods);	
		}
		}
		

		@After
		public void tearDown() {
			client.ctrlClear();
		}

		// bad input tests
		
		@Test(expected = InvalidInitFault_Exception.class)
		public void ctrlInitFoodNullTest() throws InvalidInitFault_Exception {
			client.ctrlInitFood(null);
		}
		
		@Test(expected = InvalidInitFault_Exception.class)
		public void ctrlInitFoodNegativeQuantityTest() throws InvalidInitFault_Exception  {
			List<FoodInit> initFoods = new ArrayList<>();
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
			fInit.setQuantity(-1);
			initFoods.add(fInit);
			client.ctrlInitFood(initFoods);			
		}
		
		@Test(expected = InvalidInitFault_Exception.class)
		public void ctrlInitFoodZeroQuantityTest() throws InvalidInitFault_Exception  {
			List<FoodInit> initFoods = new ArrayList<>();
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
			fInit.setQuantity(0);
			initFoods.add(fInit);
			client.ctrlInitFood(initFoods);			
		}
		
		@Test(expected = InvalidInitFault_Exception.class)
		public void ctrlInitFoodFoodAlreadyExistsTest() throws InvalidInitFault_Exception  {
			List<FoodInit> initFoods = new ArrayList<>();
			Food food = new Food();
			FoodInit fInit = new FoodInit();
			fId4.setMenuId("M3");
			fId4.setRestaurantId("T26_Restaurant1");
			food.setId(fId4);
			food.setEntree("Entrada4");
			food.setPlate("Prato4");
			food.setDessert("Sobremesa4");
			food.setPrice(40);
			food.setPreparationTime(40);
			fInit.setFood(food);
			fInit.setQuantity(40);
			initFoods.add(fInit);
			client.ctrlInitFood(initFoods);			
		}
		
		// main tests
		
		@Test
		public void ctrlInitFoodAnotherFoodTest() throws InvalidInitFault_Exception, InvalidTextFault_Exception {
			List<FoodInit> initFoods = new ArrayList<>();
			Food food = new Food();
			FoodInit fInit = new FoodInit();
			fId4.setMenuId("M4");
			fId4.setRestaurantId("T26_Restaurant1");
			food.setId(fId4);
			food.setEntree("Entrada4");
			food.setPlate("Prato4");
			food.setDessert("Sobremesa4");
			food.setPrice(40);
			food.setPreparationTime(40);
			fInit.setFood(food);
			fInit.setQuantity(40);
			initFoods.add(fInit);
			client.ctrlInitFood(initFoods);	
			
			List<Food> foods = client.searchHungry("Prato");
			assertEquals(4, foods.size());			
			assertEquals(fId4.getMenuId(), foods.get(3).getId().getMenuId());
		}
}