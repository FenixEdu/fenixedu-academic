package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularYearOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentCurricularYear persistentCurricularYear = null;

	public CurricularYearOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class CurricularYearOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class CurricularYearOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CurricularYearOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error in SetUp.");
		}
		persistentCurricularYear = persistentSupport.getIPersistentCurricularYear();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteCurricularYear() {

		// CurricularYear ja existente
		ICurricularYear CurricularYear = new CurricularYear(new Integer(2003));

		System.out.println("\n- Test 1.1 : Write Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularYear.writeCurricularYear(CurricularYear);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing CurricularYear");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing CurricularYear");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing CurricularYear");
		}

		// CurricularYear inexistente
		CurricularYear = new CurricularYear(new Integer(2000));

		System.out.println("\n- Test 1.2 : Write Non Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularYear.writeCurricularYear(CurricularYear);
			persistentSupport.confirmarTransaccao();
			assertTrue("Write Non Existing CurricularYear: Unexisting Object", true);
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing CurricularYear");
		}

		ICurricularYear cy2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			cy2 = persistentCurricularYear.readCurricularYearByYear(new Integer(2000));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularYear Just Writen Before");
		}

		assertNotNull(cy2);

		assertTrue(cy2.getYear().intValue() == CurricularYear.getYear().intValue());
		
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllCurricularYears() {

		System.out.println("\n- Test 2 : Delete All Curricularyears");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularYear.deleteAllCurricularYears();
			persistentSupport.confirmarTransaccao();
			assertTrue("Delete All Curricularyears: Deleted CurricularYears", true);
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete All CurricularYears");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentCurricularYear.readAllCurricularYears();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All CurricularYears");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadCurricularYear() {

		ICurricularYear CurricularYear = null;

		// CurricularYear ja existente
		System.out.println("\n- Test 3.1 : Read Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			CurricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(2003));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularYear");
		}
		assertNotNull(CurricularYear);
		assertTrue(CurricularYear.getYear().intValue() == 2003);

		// CurricularYear inexistente
		CurricularYear = null;
		System.out.println("\n- Test 3.2 : Read Non Existing CurricularYear");
		try {
			persistentSupport.iniciarTransaccao();
			CurricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(2000));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing CurricularYear");
		}
		assertNull(CurricularYear);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteCurricularYear() {

		ICurricularYear CurricularYear = null;

		// CurricularYear ja existente
		System.out.println("\n- Test 4.1 : Delete Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			CurricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(2003));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing CurricularYear To Delete");
		}
		assertNotNull(CurricularYear);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularYear.deleteCurricularYear(CurricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing CurricularYear");
		}

		ICurricularYear cy = null;
		try {
			persistentSupport.iniciarTransaccao();
			cy = persistentCurricularYear.readCurricularYearByYear(new Integer(2003));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted CurricularYear");
		}
		assertNull(cy);

		// CurricularYear inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularYear.deleteCurricularYear(new CurricularYear());
			persistentSupport.confirmarTransaccao();
			assertTrue("Delete Non Existing CurricularYear: CurricularYear Deleted", true);
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing CurricularYear");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllCurricularYears() {

		ArrayList list = null;

		System.out.println("\n- Test 5 : Read All Existing CurricularYear\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentCurricularYear.readAllCurricularYears();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All CurricularYears");
		}
		assertNotNull(list);
		assertEquals(list.size(), 2);
	}

}