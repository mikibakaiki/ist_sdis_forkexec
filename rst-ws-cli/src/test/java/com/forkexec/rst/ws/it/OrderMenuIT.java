package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.MenuOrder;

public class OrderMenuIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		client.ctrlClear();
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members
	MenuId mid1 = new MenuId();
	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadMenuIdFault_Exception, BadInitFault_Exception {
		{
			List<MenuInit> initMenus = new ArrayList<MenuInit>();
			
			{
				Menu menu = new Menu();
				MenuInit minit = new MenuInit();
				mid1.setId("M1");
				menu.setId(mid1);
				menu.setEntree("Entrada1");
				menu.setPlate("Prato1");
				menu.setDessert("Sobremesa1");
				menu.setPrice(10);
				menu.setPreparationTime(10);
				minit.setMenu(menu);
				minit.setQuantity(10);
				initMenus.add(minit);			
			}
			client.ctrlInit(initMenus);
		}
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}
	
	// public MenuOrder orderMenu(MenuId arg0, int arg1) throws
	// BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception

	// bad input tests

	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuNullIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
		client.orderMenu(null,1);
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuEmptyIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("");
		client.orderMenu(mid,1);
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuWhitespaceIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId(" ");
		client.orderMenu(mid,1);
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuNewLineIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("\n");
		client.orderMenu(mid,1);
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuTabIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("\t");
		client.orderMenu(mid,1);
	}
	
	@Test(expected = BadQuantityFault_Exception.class)
	public void orderMenuNegativeQuantityTest() throws BadQuantityFault_Exception, BadMenuIdFault_Exception, InsufficientQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("M1");
		client.orderMenu(mid,-1);
	}

	@Test(expected = BadQuantityFault_Exception.class)
	public void orderMenuZeroQuantityTest() throws BadQuantityFault_Exception, BadMenuIdFault_Exception, InsufficientQuantityFault_Exception {
		MenuId mid = new MenuId();
		mid.setId("M1");
		client.orderMenu(mid,0);
	}

	// main tests
	
	@Test(expected = InsufficientQuantityFault_Exception.class)
	public void orderMenuInsufficientQuantityTest() throws InsufficientQuantityFault_Exception, BadMenuIdFault_Exception, BadQuantityFault_Exception  {		
		client.orderMenu(mid1,11);
	}
	
	@Test(expected = InsufficientQuantityFault_Exception.class)
	public void orderMenuOneTooManyTest() throws InsufficientQuantityFault_Exception, BadMenuIdFault_Exception, BadQuantityFault_Exception  {
		try {
			client.orderMenu(mid1,6);
			client.orderMenu(mid1,4);
		} catch (InsufficientQuantityFault_Exception iqfe) {
			fail();
		}		
		client.orderMenu(mid1,1); //this is the call that is supposed to fail
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void orderMenuNonExistentTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception  {
		MenuId mid = new MenuId();
		mid.setId("NonExistent");
		client.orderMenu(mid,1);
	}

	@Test
	public void orderMenuSuccessTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception  {
		MenuOrder mo = client.orderMenu(mid1,1);
		assertEquals(mid1.getId(),mo.getMenuId().getId());
		assertEquals(1,mo.getMenuQuantity());
	}
	
	@Test
	public void orderMenuAllTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception  {
		MenuOrder mo = client.orderMenu(mid1,10);
		assertEquals(mid1.getId(), mo.getMenuId().getId());
		assertEquals(10, mo.getMenuQuantity());
	}

}