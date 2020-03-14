package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

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
import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.NotEnoughPointsFault_Exception;

public class TestIT extends BaseIT {
	
	private static final String uid1 = "user1@dominio";
	private static final String ccn = "4024007102923926";
	
	@BeforeClass
	public static void oneTimeSetUp() {
		client.ctrlClear();
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}	

	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		client.ctrlClear();
	}
	
	@Test
	public void test() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InterruptedException {
		//client.activateAccount("user3@dominio");
		//client.loadAccount("user3@dominio", 30, ccn);
		client.activateAccount(uid1);
		client.loadAccount(uid1, 30, ccn);
		assertEquals(3400, client.accountBalance(uid1));
		System.out.println("STOP");
		Thread.sleep(5000);
		assertEquals(100, client.accountBalance("user2@dominio"));
		System.out.println("START");
		Thread.sleep(5000);
		List<FoodInit> initFoods = new ArrayList<>();		
		FoodId fId1 = new FoodId();		
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
		
		/*FoodId fId2 = new FoodId();
		Food food2 = new Food();
		FoodInit fInit2 = new FoodInit();
		fId2.setMenuId("M2");
		fId2.setRestaurantId("T26_Restaurant2");
		food2.setId(fId2);
		food2.setEntree("Entrada2");
		food2.setPlate("Prato2");
		food2.setDessert("Sobremesa2");
		food2.setPrice(20);
		food2.setPreparationTime(20);
		fInit2.setFood(food);
		fInit2.setQuantity(20);
		initFoods.add(fInit2);*/
		
		client.ctrlInitFood(initFoods);
		//client.addFoodToCart(uid1, fId2, 2);
		client.addFoodToCart(uid1, fId1, 10);
		client.orderCart(uid1);
		assertEquals(3300, client.accountBalance(uid1));		
	}
	
}
