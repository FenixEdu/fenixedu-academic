/*
 * ExecutionPeriodOJBTest.java JUnit based test
 *
 * Created on 20 of February 2003, 10:20
 * 
 * 
 */
 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */ 

package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExecutionPeriodOJBTest extends TestCaseOJB {
	public ExecutionPeriodOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ExecutionPeriodOJBTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
	}
    
	protected void tearDown() {
		super.tearDown();
	}

	public void testReadExecutionPeriodByNameAndExecutionYear(){
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		
		try {
			persistentSupport.iniciarTransaccao();
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			// Read Existing
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			assertEquals(executionPeriod.getName(), "2º Semestre");
			assertEquals(executionPeriod.getExecutionYear(), executionYear);

			// Read Non Existing
			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("Semestre", executionYear);
			assertNull(executionPeriod);

			
			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
  	   }
	}

	public void testDelete(){
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		
		try {
			persistentSupport.iniciarTransaccao();
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			// Read non deletable
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			assertEquals(persistentExecutionPeriod.delete(executionPeriod), false);
			
			// Read deletable
			
			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("3º Semestre", executionYear);
			assertNotNull(executionPeriod);
			
			assertTrue(persistentExecutionPeriod.delete(executionPeriod));

			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	public void testReadAllExecutionPeriod(){
		try {
			persistentSupport.iniciarTransaccao();

			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.isEmpty(), false);
			assertEquals(executionPeriods.size(), 2);

			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
    
	
	public void testWriteExecutionPeriod(){
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
	
		try {
			persistentSupport.iniciarTransaccao();
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
		
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);

			// Write Changed Object
			executionPeriod.setName("new");			
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			// Read Changed
			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("new", executionYear);
			assertNotNull(executionPeriod);
			assertEquals(executionPeriod.getName(), "new");
			persistentSupport.confirmarTransaccao();
			
			// Write new Object
			
			persistentSupport.iniciarTransaccao();
			executionPeriod = new ExecutionPeriod("2º Semestre", executionYear);
			persistentExecutionPeriod.writeExecutionPeriod(executionPeriod);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.size(), 3);
			persistentSupport.confirmarTransaccao();

			try {
				persistentSupport.iniciarTransaccao();
				executionPeriod = new ExecutionPeriod("2º Semestre", executionYear);
				persistentExecutionPeriod.writeExecutionPeriod(executionPeriod);
				persistentSupport.confirmarTransaccao();
				fail("Write existing");
			} catch (ExistingPersistentException ex) {
				assertNotNull("ExistingPersistentException", ex);
			}
			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	
	public void testDeleteAll(){
		try {
			persistentSupport.iniciarTransaccao();
			persistentExecutionPeriod.deleteAll();
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();


			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.isEmpty(), false);
			
			// The result is one because one of the Execution Periods has classes and execution
			// courses associated, and so it cannot be deleted
			assertEquals(executionPeriods.size(), 1);

			persistentSupport.confirmarTransaccao();
			
			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
		
	public void testReadActualExecutionPeriod(){
		// FIXME: The method doesn't work it if there's more than one execution Period
	}
    
}