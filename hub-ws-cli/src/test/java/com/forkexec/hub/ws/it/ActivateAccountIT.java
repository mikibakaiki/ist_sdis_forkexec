package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class ActivateAccountIT extends BaseIT {

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
	
	// initialization and clean-up for each test
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void activateAccount(String userId) throws
	// InvalidUserIdFault_Exception
	
	// bad input tests

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountNullIdTest() throws InvalidUserIdFault_Exception {
		client.activateAccount(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountEmptyIdTest() throws InvalidUserIdFault_Exception {
		client.activateAccount("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountWhitespaceIdTest() throws InvalidUserIdFault_Exception {
		client.activateAccount(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountNewLineIdTest() throws InvalidUserIdFault_Exception {
		client.activateAccount("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountTabIdTest() throws InvalidUserIdFault_Exception {
		client.activateAccount("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountInvalidTest() throws InvalidUserIdFault_Exception {
		client.activateAccount("Invalid");
	}
		
	// main tests
	
	@Test
	public void activateAccountOnceTest() throws InvalidUserIdFault_Exception {		
		client.activateAccount(uid1);
		assertEquals(0, client.cartContents(uid1).size());
		assertEquals(100, client.accountBalance(uid1));
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void activateAccountAlreadyExistsTest() throws InvalidUserIdFault_Exception {
		try{
			client.activateAccount("test@dominio");
		} catch (InvalidUserIdFault_Exception iuie) {
			fail(iuie.getMessage());
		}
		client.activateAccount("test@dominio");
	}	

}