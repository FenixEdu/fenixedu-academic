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
			_suportePersistente.iniciarTransaccao();
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

			
			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
  	   }
	}

	public void testDelete(){
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		
		try {
			_suportePersistente.iniciarTransaccao();
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

			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	public void testReadAllExecutionPeriod(){
		try {
			_suportePersistente.iniciarTransaccao();

			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.isEmpty(), false);
			assertEquals(executionPeriods.size(), 2);

			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
    
	
	public void testWriteExecutionPeriod(){
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
	
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
		
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);
			assertNotNull(executionPeriod);

			// Write Changed Object
			executionPeriod.setName("new");			
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			// Read Changed
			executionPeriod = null;
			executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("new", executionYear);
			assertNotNull(executionPeriod);
			assertEquals(executionPeriod.getName(), "new");
			_suportePersistente.confirmarTransaccao();
			
			
			// Write new Object
			
			_suportePersistente.iniciarTransaccao();
			executionPeriod = new ExecutionPeriod("2º Semestre", executionYear);
			persistentExecutionPeriod.writeExecutionPeriod(executionPeriod);
			_suportePersistente.confirmarTransaccao();

			_suportePersistente.iniciarTransaccao();
			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.size(), 3);
			_suportePersistente.confirmarTransaccao();


			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	
	public void testDeleteAll(){
		try {
			_suportePersistente.iniciarTransaccao();
			persistentExecutionPeriod.deleteAll();
			_suportePersistente.confirmarTransaccao();
			
			_suportePersistente.iniciarTransaccao();


			List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
			assertEquals(executionPeriods.isEmpty(), false);
			
			// The result is one because one of the Execution Periods has classes and execution
			// courses associated, and so it cannot be deleted
			assertEquals(executionPeriods.size(), 1);

			_suportePersistente.confirmarTransaccao();
			
			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
		
	public void testReadActualExecutionPeriod(){
		// FIXME: The method doesn't work it if there's more than one execution Period
	}
    
}