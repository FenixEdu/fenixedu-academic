/*
 * ExecutionYearOJBTest.java
 *
 * Created on 20 of February 2003, 11:09
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
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;

public class ExecutionYearOJBTest extends TestCaseOJB {
	public ExecutionYearOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ExecutionYearOJBTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
	}
    
	protected void tearDown() {
		super.tearDown();
	}

	public void testreadExecutionYearByName(){
		IExecutionYear executionYear = null;
		try {
			_suportePersistente.iniciarTransaccao();
	
			// Read Existing
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			assertEquals(executionYear.getYear(), "2002/2003");

			// Read Non Existing
			executionYear = null;
			executionYear = persistentExecutionYear.readExecutionYearByName("unkn");
			assertNull(executionYear);

			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testreadExecutionYearByName:fail read existing item");
	   }
	}
	
	public void testWriteExecutionYear(){
		IExecutionYear executionYear = null;
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);
			_suportePersistente.confirmarTransaccao();
			
			
			_suportePersistente.iniciarTransaccao();
			executionYear = null;
			// Check Insert	
			executionYear = persistentExecutionYear.readExecutionYearByName("2000");
			assertNotNull(executionYear);
			assertEquals(executionYear.getYear(), "2000");
			_suportePersistente.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			 fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
		}

		try {
			// Write Existing
			_suportePersistente.iniciarTransaccao();
			executionYear = new ExecutionYear("2002/2003");
			persistentExecutionYear.writeExecutionYear(executionYear);
			_suportePersistente.confirmarTransaccao();
			fail("Espected Error");
	   } catch (ExcepcaoPersistencia ex) {
			// All is ok
	   }
	}
	
	public void testDelete(){
		IExecutionYear executionYear = null;
		
		try {
			_suportePersistente.iniciarTransaccao();
			// Read non deletable
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			// This fails because the execution year has associated classes and execution degrees
			assertEquals(persistentExecutionYear.delete(executionYear), false);

			// Create and write deletable
			executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);
			_suportePersistente.confirmarTransaccao();
			
			// Read deletable
			_suportePersistente.iniciarTransaccao();
			executionYear = null;
			executionYear = persistentExecutionYear.readExecutionYearByName("2000");
			assertNotNull(executionYear);
			assertTrue(persistentExecutionYear.delete(executionYear));
			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	public void testReadAllExecutionYears(){
		try {
			_suportePersistente.iniciarTransaccao();

			List executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears .isEmpty(), false);
			assertEquals(executionYears .size(), 2);

			_suportePersistente.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
    
	
	public void testDeleteAll(){
		try {
			_suportePersistente.iniciarTransaccao();
			executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);
			_suportePersistente.confirmarTransaccao();
			
			
						
			_suportePersistente.iniciarTransaccao();
			List executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears.size(), 3);
			persistentExecutionYear.deleteAll();
			_suportePersistente.confirmarTransaccao();

			
			_suportePersistente.iniciarTransaccao();
			executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears.isEmpty(), false);
			
			// The result is Two because the Execution Years have execution Periods
			// and classes associated
			assertEquals(executionYears.size(), 2);

			_suportePersistente.confirmarTransaccao();
			
			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
		
}