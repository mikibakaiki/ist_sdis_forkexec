package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class AccountBalanceIT extends BaseIT {

	// static members
	private static final String uid1 = "user1@dominio";
	private static final String ccn = "4024007102923926";
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
	public void setUp() throws InvalidUserIdFault_Exception {
		client.activateAccount(uid1);
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void accountBalance(String userId) throws
	// InvalidUserIdFault_Exception
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceNullIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceEmptyIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceWhitespaceIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceNewLineIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceTabIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void accountBalanceInvalidIdTest() throws InvalidUserIdFault_Exception {
		client.accountBalance("Invalid");
	}
		
	// main tests
	
	@Test
	public void accountBalanceAtStartTest() throws InvalidUserIdFault_Exception {	
		assertEquals(100, client.accountBalance(uid1));
	}
	
	@Test
	public void accountBalanceAfterLoadTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
		client.loadAccount(uid1, 30, ccn);
		assertEquals(3400, client.accountBalance(uid1));
	}	

}