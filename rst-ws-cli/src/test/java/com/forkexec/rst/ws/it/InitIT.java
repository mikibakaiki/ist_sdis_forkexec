package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

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

public class InitIT extends BaseIT {
	
	// static members

		// one-time initialization and clean-up
		@BeforeClass
		public static void oneTimeSetUp(){
			client.ctrlClear();			
		}

		@AfterClass
		public static void oneTimeTearDown() {
		}

		// members

		// initialization and clean-up for each test
		@Before
		public void setUp() throws BadInitFault_Exception {
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
			client.ctrlInit(initMenus);			
		}
		

		@After
		public void tearDown() {
			client.ctrlClear();
		}

		// bad input tests
		
		@Test(expected = BadInitFault_Exception.class)
		public void ctrlInitNullTest() throws BadInitFault_Exception {
			client.ctrlInit(null);
		}
		
		@Test(expected = BadInitFault_Exception.class)
		public void ctrlInitMenuAlreadyExistsTest() throws BadInitFault_Exception {
			List<MenuInit> initMenus = new ArrayList<MenuInit>();
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("M1");
			menu.setId(mid);
			menu.setEntree("Entrada4");
			menu.setPlate("Prato4");
			menu.setDessert("Sobremesa4");
			menu.setPrice(40);
			menu.setPreparationTime(40);
			minit.setMenu(menu);
			minit.setQuantity(40);
			initMenus.add(minit);
			client.ctrlInit(initMenus);			
		}
		
		// main tests
		
		@Test
		public void ctrlInitAnotherMenuTest() throws BadInitFault_Exception, BadTextFault_Exception {
			List<MenuInit> initMenus = new ArrayList<MenuInit>();
			Menu menu = new Menu();
			MenuId mid = new MenuId();
			MenuInit minit = new MenuInit();
			mid.setId("M4");
			menu.setId(mid);
			menu.setEntree("Entrada4");
			menu.setPlate("Prato4");
			menu.setDessert("Sobremesa4");
			menu.setPrice(40);
			menu.setPreparationTime(40);
			minit.setMenu(menu);
			minit.setQuantity(40);
			initMenus.add(minit);
			client.ctrlInit(initMenus);
			
			assertEquals(4, client.searchMenus("Prato").size());
			assertEquals(mid.getId(), client.searchMenus("Prato4").get(0).getId().getId());
		}
}