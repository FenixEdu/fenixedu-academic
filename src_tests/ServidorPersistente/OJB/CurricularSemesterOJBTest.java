package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularSemester;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularSemesterOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentCurricularSemester persistentCurricularSemester = null;
	IPersistentCurricularYear persistentCurricularYear = null;

	public CurricularSemesterOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class CurricularSemesterOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class CurricularSemesterOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CurricularSemesterOJBTest.class);
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
		persistentCurricularSemester = persistentSupport.getIPersistentCurricularSemester();
		persistentCurricularYear = persistentSupport.getIPersistentCurricularYear();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteCurricularSemester() {

		ICurricularYear curricularYear = null;

		System.out.println("\n- Test 1.1 : Write Existing CurricularSemester\n");

		try {
			persistentSupport.iniciarTransaccao();
			curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularYear Just Writen Before");
		}

		assertNotNull(curricularYear);

		// CurricularSemester ja existente
		ICurricularSemester curricularSemester = new CurricularSemester(new Integer(1), curricularYear);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularSemester.lockWrite(curricularSemester);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing CurricularSemester");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing CurricularSemester");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing CurricularSemester");
		}

		// CurricularSemester inexistente
		curricularSemester = new CurricularSemester(new Integer(3), curricularYear);

		System.out.println("\n- Test 1.2 : Write Non Existing CurricularSemester\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularSemester.lockWrite(curricularSemester);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing CurricularSemester");
		}

		ICurricularSemester cs = null;

		try {
			persistentSupport.iniciarTransaccao();
			cs = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(curricularSemester.getSemester(), curricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularSemester Just Writen Before");
		}

		assertNotNull(cs);

		assertTrue(cs.getSemester().equals(curricularSemester.getSemester()));

		assertTrue(cs.getCurricularYear().equals(curricularYear));

		assertNull(cs.getAssociatedCurricularCourses());

	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllCurricularSemesters() {

		System.out.println("\n- Test 2 : Delete All CurricularSemesters");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularSemester.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete All CurricularSemesters");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentCurricularSemester.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All CurricularSemesters");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadCurricularSemester() {

		ICurricularSemester curricularSemester = null;

		ICurricularYear curricularYear = null;

		System.out.println("\n- Test 3.1 : Read Existing CurricularSemester\n");

		try {
			persistentSupport.iniciarTransaccao();
			curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularYear Just Writen Before");
		}

		assertNotNull(curricularYear);

		// CurricularSemester ja existente
		try {
			persistentSupport.iniciarTransaccao();
			curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularSemester");
		}
		assertNotNull(curricularSemester);
		assertTrue(curricularSemester.getSemester().intValue() == 1);
		assertNotNull(curricularSemester.getAssociatedCurricularCourses());
		assertTrue(curricularSemester.getCurricularYear().equals(curricularYear));

		// CurricularSemester inexistente
		curricularSemester = null;
		System.out.println("\n- Test 3.2 : Read Non Existing CurricularSemester");
		try {
			persistentSupport.iniciarTransaccao();
			curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(3), curricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing CurricularSemester");
		}
		assertNull(curricularSemester);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteCurricularSemester() {

		ICurricularSemester curricularSemester = null;

		ICurricularYear curricularYear = null;

		System.out.println("\n- Test 4.1 : Delete Existing CurricularSemester\n");
		try {
			persistentSupport.iniciarTransaccao();
			curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularYear Just Writen Before");
		}

		assertNotNull(curricularYear);

		// CurricularSemester ja existente
		try {
			persistentSupport.iniciarTransaccao();
			curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing CurricularSemester To Delete");
		}
		assertNotNull(curricularSemester);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularSemester.delete(curricularSemester);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing CurricularSemester");
		}

		ICurricularSemester cs = null;
		try {
			persistentSupport.iniciarTransaccao();
			cs = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(3), curricularYear);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted CurricularSemester");
		}
		assertNull(cs);

		// CurricularSemester inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing CurricularSemester\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularSemester.delete(new CurricularSemester());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing CurricularSemester");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllCurricularSemesters() {

		ArrayList list = null;

		System.out.println("\n- Test 5 : Read All Existing CurricularSemester\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentCurricularSemester.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All CurricularSemesters");
		}
		assertNotNull(list);
		assertEquals(list.size(), 10);
	}

}