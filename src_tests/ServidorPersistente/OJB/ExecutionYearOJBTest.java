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
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ExecutionYearOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentExecutionYear persistentExecutionYear = null;

	
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
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();

	}
    
	protected void tearDown() {
		super.tearDown();
	}

	public void testreadExecutionYearByName(){
		IExecutionYear executionYear = null;
		try {
			persistentSupport.iniciarTransaccao();
	
			// Read Existing
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			assertEquals(executionYear.getYear(), "2002/2003");

			// Read Non Existing
			executionYear = null;
			executionYear = persistentExecutionYear.readExecutionYearByName("unkn");
			assertNull(executionYear);

			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testreadExecutionYearByName:fail read existing item");
	   }
	}
	
	public void testWriteExecutionYear(){
		IExecutionYear executionYear = null;
		try {
			persistentSupport.iniciarTransaccao();
			executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			executionYear = null;
			// Check Insert	
			executionYear = persistentExecutionYear.readExecutionYearByName("2000");
			assertNotNull(executionYear);
			assertEquals(executionYear.getYear(), "2000");
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			 fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
		}

		try {
			// Write Existing
			persistentSupport.iniciarTransaccao();
			executionYear = new ExecutionYear("2002/2003");
			persistentExecutionYear.writeExecutionYear(executionYear);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing: Expected an Excpetion");
	   	} catch (ExistingPersistentException ex) {
			// All is ok
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}
		} catch (ExcepcaoPersistencia ex) {
			 fail("Write Existing: Unexpected Exception");
		}
	}
	
	public void testDelete(){
		IExecutionYear executionYear = null;
		
		try {
			persistentSupport.iniciarTransaccao();
			// Read non deletable
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			// This fails because the execution year has associated classes and execution degrees
			assertEquals(persistentExecutionYear.delete(executionYear), false);

			// Create and write deletable
			executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);
			persistentSupport.confirmarTransaccao();
			
			// Read deletable
			persistentSupport.iniciarTransaccao();
			executionYear = null;
			executionYear = persistentExecutionYear.readExecutionYearByName("2000");
			assertNotNull(executionYear);
			assertTrue(persistentExecutionYear.delete(executionYear));
			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
	
	public void testReadAllExecutionYears(){
		try {
			persistentSupport.iniciarTransaccao();

			List executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears .isEmpty(), false);
			assertEquals(executionYears .size(), 2);

			persistentSupport.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item");
	   }
	}
    
	
	public void testDeleteAll(){
		try {

			persistentSupport.iniciarTransaccao();
			IExecutionYear executionYear = new ExecutionYear("2000");
			persistentExecutionYear.writeExecutionYear(executionYear);			
			persistentSupport.confirmarTransaccao();
			
			persistentSupport.iniciarTransaccao();
			List executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears.size(), 3);
			persistentExecutionYear.deleteAll();
			persistentSupport.confirmarTransaccao();

			
			persistentSupport.iniciarTransaccao();
			executionYears = persistentExecutionYear.readAllExecutionYear();
			assertEquals(executionYears.isEmpty(), false);
			
			// The result is Two because the Execution Years have execution Periods
			// and classes associated
			assertEquals(executionYears.size(), 2);

			persistentSupport.confirmarTransaccao();
			
			
	   } catch (ExcepcaoPersistencia ex) {
			fail("testReadExecutionPeriodByNameAndExecutionYear:fail read existing item" + ex);
	   }
	}
	
	
	public void testReadActualExecutionYear(){
		try {

			persistentSupport.iniciarTransaccao();
			
			IExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear().readActualExecutionYear();
			assertNotNull(executionYear);
			assertEquals(executionYear.getYear(), "2003/2004");
			persistentSupport.confirmarTransaccao();
		
		} catch (ExcepcaoPersistencia ex) {
			 fail("testReadActualExecutionYear:fail read existing item" + ex);
		}
		
		
	}
		
}