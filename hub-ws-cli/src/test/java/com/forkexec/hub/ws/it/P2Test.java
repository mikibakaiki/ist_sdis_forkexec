package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class P2Test extends BaseIT {

    private static final String uid1 = "user1@dominio";
    private static final String uid2 = "user2@dominio";
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

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void sameAccountActivationTest() throws InvalidUserIdFault_Exception {

        client.activateAccount(uid1);
        client.activateAccount(uid1);

    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void wrongUserFormatTest() throws InvalidUserIdFault_Exception{
        client.activateAccount(uid2);
        client.activateAccount("userdominio");
    }

    @Test
    public void loadBalanceTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception{
        client.activateAccount(uid1);
        assertEquals(100, client.accountBalance(uid1));

        client.loadAccount(uid1, 30, ccn);
        assertEquals(3400, client.accountBalance(uid1));

    }

    @Test
    public void loadUnexistingAccountTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception{
        client.loadAccount(uid1, 30, ccn);
        assertEquals(3400, client.accountBalance(uid1));
    }

    @Test
    public void overloadCreditAccount() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InterruptedException {
    	
        System.out.println("STOP");
        Thread.sleep(10000);
        client.loadAccount(uid1, 10, ccn);
        assertEquals(1100, client.accountBalance(uid1));
        client.loadAccount(uid1, 10, ccn);
        client.loadAccount(uid1, 10, ccn);
        client.loadAccount(uid1, 10, ccn);
        client.loadAccount(uid1, 10, ccn);
        client.loadAccount(uid1, 10, ccn);

        assertEquals(6100, client.accountBalance(uid1));
        System.out.println("START");
        Thread.sleep(10000);
    }
    
    @Test
    public void buyFoodTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception,
    InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidInitFault_Exception {
        client.activateAccount(uid1);
        assertEquals(100, client.accountBalance(uid1));

        client.loadAccount(uid1, 30, ccn);
        assertEquals(3400, client.accountBalance(uid1));

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
        client.ctrlInitFood(initFoods);
        //client.addFoodToCart(uid1, fId2, 2);
        client.addFoodToCart(uid1, fId1, 10);
        client.orderCart(uid1);
        assertEquals(3300, client.accountBalance(uid1));

    }


}
