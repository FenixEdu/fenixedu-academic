package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularCourseScopeOJBTest extends TestCaseOJB {

	private SuportePersistenteOJB persistentSupport = null;
	private IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
	private IPersistentCurricularSemester persistentCurricularSemester = null;
	private IPersistentCurricularCourse persistentCurricularCourse = null;
	private IPersistentCurricularYear persistentCurricularYear = null;
	private IPersistentBranch persistentBranch = null;

	private ICurricularCourse curricularCourse = null;
	private ICurricularSemester curricularSemester = null;
	private IBranch branch = null;

	public CurricularCourseScopeOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class CurricularCourseScopeOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class CurricularCourseScopeOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CurricularCourseScopeOJBTest.class);
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
		persistentCurricularCourseScope = persistentSupport.getIPersistentCurricularCourseScope();
		persistentCurricularSemester = persistentSupport.getIPersistentCurricularSemester();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentCurricularYear = persistentSupport.getIPersistentCurricularYear();
		persistentBranch = persistentSupport.getIPersistentBranch();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteCurricularCourseScope() {

		// CurricularCourseScope ja existente
		System.out.println("\n- Test 1.1 : Write Existing CurricularCourseScope\n");
		this.loadDataFromDB(true);
		ICurricularCourseScope curricularCourseScope = new CurricularCourseScope(this.curricularCourse, this.curricularSemester, this.branch);

		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourseScope.lockWrite(curricularCourseScope);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing CurricularCourseScope");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing CurricularCourseScope");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing CurricularCourseScope");
		}

		System.out.println("\n- Test 1.2 : Write Non Existing CurricularCourseScope\n");
		// CurricularCourseScope inexistente
		this.loadDataFromDB(false);
		curricularCourseScope = new CurricularCourseScope(this.curricularCourse, this.curricularSemester, this.branch);
		curricularCourseScope.getCurricularCourse().setMandatory(new Boolean(true));
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourseScope.lockWrite(curricularCourseScope);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing CurricularCourseScope");
		}

		ICurricularCourseScope curricularCourseScope2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourseScope2 = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(this.curricularCourse, this.curricularSemester, this.branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing CurricularCourseScope Just Writen Before");
		}

		assertNotNull(curricularCourseScope2);
		assertTrue(curricularCourseScope2.equals(curricularCourseScope));
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllCurricularCourseScopes() {

		System.out.println("\n- Test 2 : Delete All CurricularCourseScopes");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourseScope.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Delete All CurricularCourseScopes");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentCurricularCourseScope.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All CurricularCourseScopes");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadCurricularCourseScope() {

		//		CurricularCourseScope ja existente
		System.out.println("\n- Test 3.1 : Read Existing CurricularCourseScope\n");
		this.loadDataFromDB(true);
		ICurricularCourseScope curricularCourseScope = null;
			
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(this.curricularCourse, this.curricularSemester, this.branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularCourseScope");
		}
		assertNotNull(curricularCourseScope);
		assertTrue(curricularCourseScope.getCurricularCourse().equals(this.curricularCourse));
		assertTrue(curricularCourseScope.getCurricularSemester().equals(this.curricularSemester));
		assertTrue(curricularCourseScope.getBranch().equals(this.branch));

		// CurricularCourseScope inexistente
		this.loadDataFromDB(false);
		System.out.println("\n- Test 3.2 : Read Non Existing CurricularCourseScope");
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(this.curricularCourse, this.curricularSemester, this.branch);			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing CurricularCourseScope");
		}
		assertNull(curricularCourseScope);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteCurricularCourseScope() {

		
		// CurricularCourseScope ja existente
		System.out.println("\n- Test 4.1 : Delete Existing CurricularCourseScope\n");
		this.loadDataFromDB(true);
		ICurricularCourseScope curricularCourseScope = null;
			
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(this.curricularCourse, this.curricularSemester, this.branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularCourseScope");
		}
		assertNotNull(curricularCourseScope);
	
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourseScope.delete(curricularCourseScope);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing CurricularCourseScope");
		}

		ICurricularCourseScope curricularCourseScope2 = null;
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourseScope2 = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(this.curricularCourse, this.curricularSemester, this.branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing CurricularCourseScope");
		}
	
		assertNull(curricularCourseScope2);

		// CurricularCourseScope inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing CurricularCourseScope\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentCurricularCourseScope.delete(new CurricularCourseScope());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing CurricularCourseScope");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllCurricularCourseScopes() {

		ArrayList list = null;

		System.out.println("\n- Test 5 : Read All Existing CurricularCourseScope\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentCurricularCourseScope.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All CurricularCourseScopes");
		}
		assertNotNull(list);
		assertEquals(14, list.size());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private void loadDataFromDB(boolean existing) {

		ICurricularCourse curricularCourse = null;
		ICurricularSemester curricularSemester = null;
		ICurricularYear curricularYear = null;
		IBranch branch = null;

		try {
			persistentSupport.iniciarTransaccao();
			curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(2));
			assertNotNull(curricularYear);
			curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
			curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
			branch = persistentBranch.readBranchByNameAndCode("Inteligencia Artificial", "IA");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Data from DB");
		}

		assertNotNull(curricularCourse);
		assertNotNull(curricularSemester);
		assertNotNull(branch);
			
		if(!existing) {
			curricularSemester = new CurricularSemester(new Integer(1232243), curricularYear);
		}

		this.branch = branch;
		this.curricularCourse = curricularCourse;
		this.curricularSemester = curricularSemester;
	}
}