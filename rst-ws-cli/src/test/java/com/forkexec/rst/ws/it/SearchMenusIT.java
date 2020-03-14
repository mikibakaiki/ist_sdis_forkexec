package com.forkexec.rst.ws.it;

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
import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;

/**
 * Test suite
 */

public class SearchMenusIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws BadInitFault_Exception {
		// clear remote service state before all tests
		client.ctrlClear();

		// fill-in test menus
		// (since searchMenus is read-only the initialization below
		// can be done once for all tests in this suite)
		//List<Menu> menus = new ArrayList<Menu>();
		List<MenuInit> initMenus = new ArrayList<MenuInit>();
		
		{
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("M1");
			menu.setId(mid);
			menu.setEntree("Entrada1");
			menu.setPlate("Prato1");
			menu.setDessert("Sobremesa1");
			menu.setPrice(10);
			menu.setPreparationTime(10);
			minit.setMenu(menu);
			minit.setQuantity(10);
			initMenus.add(minit);			
		}
		{
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("M2");
			menu.setId(mid);
			menu.setEntree("Entrada2");
			menu.setPlate("Prato2");
			menu.setDessert("Sobremesa2");
			menu.setPrice(20);
			menu.setPreparationTime(20);
			minit.setMenu(menu);
			minit.setQuantity(20);
			initMenus.add(minit);
		}
		{
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("M3");
			menu.setId(mid);
			menu.setEntree("Entrada3");
			menu.setPlate("Prato3");
			menu.setDessert("Sobremesa3");
			menu.setPrice(30);
			menu.setPreparationTime(30);
			minit.setMenu(menu);
			minit.setQuantity(30);
			initMenus.add(minit);
		}
		{ //neste caso o Prato e Sobremesa estao intencionalmente trocados
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("B4");
			menu.setId(mid);
			menu.setEntree("Entree4");
			menu.setPlate("Sobremesa4");
			menu.setDessert("Prato4");
			menu.setPrice(10);
			menu.setPreparationTime(20);
			minit.setMenu(menu);
			minit.setQuantity(30);
			initMenus.add(minit);
		}
		client.ctrlInit(initMenus);
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

	// public List<Menu> searchMenus(String descriptionText) throws
	// BadTextFault_Exception

	// bad input tests

	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusNullTest() throws BadTextFault_Exception {
		client.searchMenus(null);
	}

	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusEmptyTest() throws BadTextFault_Exception {
		client.searchMenus("");
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusSpaceAtStartTest() throws BadTextFault_Exception {
		client.searchMenus(" Bacalhau");
	}

	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusSpaceAtTheEndTest() throws BadTextFault_Exception {
		client.searchMenus("Bacalhau ");
	}

	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusSpaceInTheMiddleTest() throws BadTextFault_Exception {
		client.searchMenus("Bacalhau a Bras");
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusNewLineTest() throws BadTextFault_Exception {
		client.searchMenus("Bacalhau\n");
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusTabTest() throws BadTextFault_Exception {
		client.searchMenus("Bacalhau\t");
	}

	@Test(expected = BadTextFault_Exception.class)
	public void searchMenusCarriageTest() throws BadTextFault_Exception {
		client.searchMenus("Bacalhau\r");
	}

	// main tests

	@Test
	public void searchMenusOneMatchTest() throws BadTextFault_Exception {
		List<Menu> menus = client.searchMenus("Prato1");
		assertNotNull(menus);
		assertEquals(1, menus.size());

		Menu menu = menus.get(0);
		assertEquals("M1", menu.getId().getId());
		assertEquals(10, menu.getPrice());
		assertEquals(10, menu.getPreparationTime());
		assertEquals("Entrada1", menu.getEntree());
		assertEquals("Prato1", menu.getPlate());
		assertEquals("Sobremesa1", menu.getDessert());
	}

	@Test
	public void searchMenusAllMatchTest() throws BadTextFault_Exception {
		List<Menu> menus = client.searchMenus("Prato");
		assertNotNull(menus);
		assertEquals(4, menus.size());

		// no ordering is imposed on results
		// check if menu plate all contain the search term Prato
		for (Menu menu : menus) {
			assertTrue(menu.getPlate().indexOf("rato") == 1 || menu.getDessert().indexOf("rato") == 1);
		}
	}

	@Test
	public void searchMenusSomeMatchTest() throws BadTextFault_Exception {
		List<Menu> menus = client.searchMenus("Entrada");
		assertNotNull(menus);
		assertEquals(3, menus.size());

		// no ordering is imposed on results
		// check if menu entrees on results all contain the search term Entrada
		for (Menu menu : menus) {
			assertTrue(menu.getEntree().indexOf("trada") == 2);
		}
	}

	@Test
	public void searchMenusNoMatchTest() throws BadTextFault_Exception {
		List<Menu> menus = client.searchMenus("RANDOm");
		// when menus are not found,
		// an empty list should be returned (not null)
		assertNotNull(menus);
		assertEquals(0, menus.size());
	}

	@Test
	public void searchMenusLowerCaseTest() throws BadTextFault_Exception {
		// menu entrees, plates and desserts are case sensitive,
		// so "entrada" is not the same as "Entrada"
		List<Menu> menus = client.searchMenus("entrada");
		assertNotNull(menus);
		assertEquals(0, menus.size());
	}

	@Test
	public void searchMenusPricePrepTimeTest() throws BadTextFault_Exception {
		// assure price and preparation time are not swapped
		List<Menu> menus = client.searchMenus("Entree4");
		assertNotNull(menus);
		assertEquals(1, menus.size());

		Menu menu = menus.get(0);
		assertEquals("B4", menu.getId().getId());
		assertEquals(10, menu.getPrice());
		assertEquals(20, menu.getPreparationTime());
		assertEquals("Entree4", menu.getEntree());
		assertEquals("Sobremesa4", menu.getPlate());
		assertEquals("Prato4", menu.getDessert());
	}

}
