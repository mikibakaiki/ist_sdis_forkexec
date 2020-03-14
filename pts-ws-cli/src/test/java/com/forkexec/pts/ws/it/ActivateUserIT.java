package com.forkexec.pts.ws.it;

import static org.junit.Assert.*;

import org.junit.*;

import com.forkexec.pts.ws.*;
import com.forkexec.pts.ws.it.*;

/*
 * Class tests if the user creation has succeeded or not
 */
public class ActivateUserIT extends BaseIT  {
	private static final int USER_POINTS = 100;
	private static final String VALID_USER = "utilizador@dominio";

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadInitFault_Exception {
		client.ctrlClear();
		client.ctrlInit(USER_POINTS);
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	@Test
	public void createUserValidTest() throws EmailAlreadyExistsFault_Exception {
		client.activateUser(VALID_USER);
		assertEquals(USER_POINTS, client.pointsBalance(VALID_USER));
	}
		 
	@Test
	public void createUserValidTest2() throws EmailAlreadyExistsFault_Exception {
		String email = new String("sd.teste@tecnico");
		client.activateUser(email);
		assertEquals(USER_POINTS, client.pointsBalance(email));
	}
	
	@Test
	public void createUserValidTest3() throws EmailAlreadyExistsFault_Exception {
		String email = new String("sd@tecnico");
		client.activateUser(email);
		assertEquals(USER_POINTS, client.pointsBalance(email));
	}
	 
	 
	/*s
	 * Tenta criar um user com o mesmo nome duas vezes 
	 * Devera retornar a excepcao AlreadyRegisteredUser
	 */
	@Test(expected = EmailAlreadyExistsFault_Exception.class)
	public void createUserDuplicateTest() throws EmailAlreadyExistsFault_Exception {
		client.activateUser(VALID_USER);
		client.activateUser(VALID_USER);
	}

}
