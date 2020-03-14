package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;


public class InitUserPointsIT extends BaseIT {

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
	
	// initialization and clean-up for each test
	@Before
	public void setUp() throws InvalidInitFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		{
			client.activateAccount(uid1);
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void ctrlInitUserPoints(String userId) throws
	// InvalidUserIdFault_Exception
	
	// bad input tests

	@Test(expected = InvalidInitFault_Exception.class)
	public void ctrlInitUserPointsNegativePointsTest() throws InvalidInitFault_Exception {
		client.ctrlInitUserPoints(-2);
	}
		
	// main tests	
	
	@Test
	public void ctrlInitUserPointsSuccessTest() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
		assertEquals(100, client.accountBalance(uid1));
		client.ctrlInitUserPoints(1500);
		client.activateAccount(uid2);
		assertEquals(1500, client.accountBalance(uid2));
		assertEquals(100, client.accountBalance(uid1));
	}
	
	@Test
	public void ctrlInitUserPointsAtZeroTest() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
		assertEquals(100, client.accountBalance(uid1));
		client.ctrlInitUserPoints(0);
		client.activateAccount(uid2);
		assertEquals(0, client.accountBalance(uid2));
		assertEquals(100, client.accountBalance(uid1));
	}
	
	
}