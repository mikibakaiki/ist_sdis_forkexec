package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;


public class LoadAccountIT extends BaseIT {

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
	public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
		{

			client.activateAccount(uid1);
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	// public void loadAccount(String userId, int moneyToAdd, String creditCardNumber) throws
	// InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception
	
	
	// bad input tests

	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountNullCreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, null);
	}
	
	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountEmptyCreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, "");
	}

	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountWhitespaceCreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, " ");
	}
	
	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountNewLineCreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, "\n");
	}
	
	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountTabCreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, "\t");
	}
	
	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountInvalid1CreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, "invalid");
	}
	
	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void loadAccountInvalid2CreditCardTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, "1234");
	}	
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountNullUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(null, 10, ccn);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountEmptyUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("", 10, ccn);
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountWhitespaceUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(" ", 10, ccn);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountNewLineUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("\n", 10, ccn);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountTabUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("\t", 10, ccn);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountInvalidUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("user@", 10, ccn);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void loadAccountNonExistentUserIdTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception  {
		client.loadAccount("user2@dominio", 10, ccn);
	}
	
	@Test(expected = InvalidMoneyFault_Exception.class)
	public void loadAccountZeroQuantityTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 0, ccn);
	}
	
	@Test(expected = InvalidMoneyFault_Exception.class)
	public void loadAccountNegativeQuantityTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, -1, ccn);
	}
	
	@Test(expected = InvalidMoneyFault_Exception.class)
	public void loadAccountInvalidQuantityTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 15, ccn);
	}

	// main tests
	
	@Test
	public void loadAccountOnceTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 20, ccn);
		assertEquals(2200, client.accountBalance(uid1));
	}
	
	@Test
	public void loadAccountVariousFoodTest() throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(uid1, 10, ccn);
		client.loadAccount(uid1, 20, ccn);
		assertEquals(3200, client.accountBalance(uid1));
	}
}
