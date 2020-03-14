package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;

/**
 * Test suite
 */
public class GetMenuIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws BadMenuIdFault_Exception, BadInitFault_Exception {
		// clear remote service state before all tests
		client.ctrlClear();

		// fill-in test menus
		// (since getMenu is read-only the initialization below
		// can be done once for all tests in this suite)
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
			menu.setDessert("Sobremesa 3");
			menu.setPrice(30);
			menu.setPreparationTime(30);
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

	// public Menu getMenu(MenuId menuId) throws
	// BadMenuIdFault_Exception {

	// bad input tests

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuNullTest() throws BadMenuIdFault_Exception {
		client.getMenu(null);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuEmptyTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("");
		client.getMenu(mid);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuWhitespaceTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId(" ");
		client.getMenu(mid);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuTabTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("\t");
		client.getMenu(mid);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuNewlineTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("\n");
		client.getMenu(mid);
	}

	// main tests

	@Test
	public void getMenuExistsTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("M1");
		Menu menu = client.getMenu(mid);
		assertEquals("M1", menu.getId().getId());
		assertEquals(10, menu.getPrice());
		assertEquals(10, menu.getPreparationTime());
		assertEquals("Entrada1", menu.getEntree());
		assertEquals("Prato1", menu.getPlate());
		assertEquals("Sobremesa1", menu.getDessert());
		
	}

	@Test
	public void getMenuAnotherExistsTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("M2");
		Menu menu = client.getMenu(mid);
		assertEquals("M2", menu.getId().getId());
		assertEquals(20, menu.getPrice());
		assertEquals(20, menu.getPreparationTime());
		assertEquals("Entrada2", menu.getEntree());
		assertEquals("Prato2", menu.getPlate());
		assertEquals("Sobremesa2", menu.getDessert());
	}

	@Test
	public void getMenuYetAnotherExistsTest() throws BadMenuIdFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("M3");
		Menu menu = client.getMenu(mid);
		assertEquals("M3", menu.getId().getId());
		assertEquals(30, menu.getPrice());
		assertEquals(30, menu.getPreparationTime());
		assertEquals("Entrada3", menu.getEntree());
		assertEquals("Prato3", menu.getPlate());
		assertEquals("Sobremesa 3", menu.getDessert());
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuNotExistsTest() throws BadMenuIdFault_Exception {
		// when menu does not exist, null should be returned
		MenuId mid = new MenuId();
		mid.setId("B4");
		client.getMenu(mid);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuLowercaseNotExistsTest() throws BadMenuIdFault_Exception {
		// menu identifiers are case sensitive,
		// so "m1" is not the same as "M1"
		MenuId mid = new MenuId();
		mid.setId("m1");
		client.getMenu(mid);
	}

}