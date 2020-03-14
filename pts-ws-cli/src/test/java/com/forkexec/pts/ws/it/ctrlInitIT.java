package com.forkexec.pts.ws.it;

import static org.junit.Assert.*;

import org.junit.*;

import com.forkexec.pts.ws.*;
import com.forkexec.pts.ws.it.*;
/*
 * Class tests if the user creation has succeeded or not
 */
public class ctrlInitIT extends BaseIT  {
	private static final int USER_POINTS = 100;
	private static final String VALID_USER = "utilizador@dominio";

	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

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
	
	@Test(expected = BadInitFault_Exception.class)
	public void ctrlInitInvalid() throws BadInitFault_Exception {
		client.ctrlInit(-1);
	}
	@Test
	public void ctrlInitValid1() throws BadInitFault_Exception {
		client.ctrlInit(0);
	}
	@Test
	public void ctrlInitValid2() throws BadInitFault_Exception {
		client.ctrlInit(10);
	}
}
