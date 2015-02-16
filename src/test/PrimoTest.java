/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Cecco
 *
 */
public class PrimoTest {

	@BeforeClass
	public static void initClass() {
		System.out.println("init Class ");
	}

	@AfterClass
	public static void endClass() {
		System.out.println("end Class");
	}

	@Before
	public void initMethod() {
		System.out.println("init Method ");
	}

	@After
	public void endMethod() {
		System.out.println("end Method ");
	}
	
	@Test
	public void test1() {
		System.out.println("Test 1");
		assertTrue(true);
		assertFalse(false);
	}
	
	@Test
	public void Sum() {
		System.out.println("Test 2");
		int a = 5;
		int b = 10;
		int result = a + b;
		assertEquals(15, result);
	}
	
	
	
}
