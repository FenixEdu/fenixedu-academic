package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DegreeEnrolmentInfo;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDegreeEnrolmentInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentDegreeEnrolmentInfo;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class DegreeEnrolmentInfoOJBTest extends TestCaseOJB {

	private IPersistentExecutionPeriod executionPeriodDAO;
	SuportePersistenteOJB persistentSupport = null;
	IPersistentDegreeEnrolmentInfo persistentDegreeEnrolmentInfo = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	ICursoPersistente persistentDegree = null;

	public DegreeEnrolmentInfoOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class DegreeEnrolmentInfoOJB.");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class DegreeEnrolmentInfoOJB.");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DegreeEnrolmentInfoOJBTest.class);
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
		persistentDegreeEnrolmentInfo = persistentSupport.getIPersistentDegreeEnrolmentInfo();
		persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentDegree = persistentSupport.getICursoPersistente();
	}

	protected void tearDown() {
		super.tearDown();
	}

// -------------------------------------------------------------------------------------------------------------------------

	public void testWriteDegreeEnrolmentInfo() {

		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;

		// DegreeEnrolmentInfo ja existente
		System.out.println("- Test 1.1 : Write Existing DegreeEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("LEIC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		IDegreeEnrolmentInfo degreeEnrolmentInfo = new DegreeEnrolmentInfo();
		degreeEnrolmentInfo.setDegreeCurricularPlan(degreeCurricularPlan);
		degreeEnrolmentInfo.setDegreeDuration(new Integer(5));
		degreeEnrolmentInfo.setMinimalYearForOptionalCourses(new Integer(3));
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.lockWrite(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing DegreeEnrolmentInfo");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing DegreeEnrolmentInfo");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing DegreeEnrolmentInfo");
		}

		// DegreeEnrolmentInfo inexistente
		System.out.println(".- Test 1.2 : Write Non Existing DegreeEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano2", degree);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}
		assertNotNull(degree);
		assertNotNull(degreeCurricularPlan);

		degreeEnrolmentInfo = new DegreeEnrolmentInfo();
		degreeEnrolmentInfo.setDegreeCurricularPlan(degreeCurricularPlan);
		degreeEnrolmentInfo.setDegreeDuration(new Integer(5));
		degreeEnrolmentInfo.setMinimalYearForOptionalCourses(new Integer(3));
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.lockWrite(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing DegreeEnrolmentInfo");
		}

		IDegreeEnrolmentInfo degreeEnrolmentInfo2 = null;
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo2 = persistentDegreeEnrolmentInfo.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing DegreeEnrolmentInfo Just Writen Before");
		}
		assertNotNull(degreeEnrolmentInfo2);
		assertEquals("DegreeEnrolmentInfos not equal!", degreeEnrolmentInfo, degreeEnrolmentInfo2);
	}

// -------------------------------------------------------------------------------------------------------------------------
/*
	public void testDeleteAllDegreeEnrolmentInfos() {

		System.out.println("\n- Test 2 : Delete All DegreeEnrolmentInfos");
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Delete All DegreeEnrolmentInfos");
		}

		List result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentDegreeEnrolmentInfo.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All DegreeEnrolmentInfos");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadDegreeEnrolmentInfo() {

		IDegreeEnrolmentInfo degreeEnrolmentInfo = null;

		ICurricularCourse curricularCourse = null;
		IStudentCurricularPlan studentCurricularPlan = null;

		System.out.println("\n- Test 3.1 : Read Existing DegreeEnrolmentInfo\n");

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse =
				persistentCurricularCourse.readCurricularCourseByNameAndCode(
					"Trabalho Final de Curso I",
					"TFCI");
			studentCurricularPlan =
				persistentStudentCurricularPlan
					.readActiveStudentCurricularPlan(
					new Integer(45498),
					new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}

		assertNotNull(curricularCourse);
		assertNotNull(studentCurricularPlan);

		// DegreeEnrolmentInfo ja existente
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo =
				persistentDegreeEnrolmentInfo
					.readDegreeEnrolmentInfoByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing DegreeEnrolmentInfo");
		}
		assertNotNull(degreeEnrolmentInfo);
		assertTrue(degreeEnrolmentInfo.getCurricularCourse().equals(curricularCourse));
		assertTrue(
			degreeEnrolmentInfo.getStudentCurricularPlan().equals(studentCurricularPlan));

		// DegreeEnrolmentInfo inexistente
		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse =
				persistentCurricularCourse.readCurricularCourseByNameAndCode(
					"Trabalho Final de Curso II",
					"TFCII");
			studentCurricularPlan =
				persistentStudentCurricularPlan
					.readActiveStudentCurricularPlan(
					new Integer(600),
					new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}

		assertNotNull(curricularCourse);
		assertNotNull(studentCurricularPlan);

		degreeEnrolmentInfo = null;
		System.out.println("\n- Test 3.2 : Read Non Existing DegreeEnrolmentInfo");
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo =
				persistentDegreeEnrolmentInfo
					.readDegreeEnrolmentInfoByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing DegreeEnrolmentInfo");
		}
		assertNull(degreeEnrolmentInfo);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteDegreeEnrolmentInfo() {

		IDegreeEnrolmentInfo degreeEnrolmentInfo = null;
		ICurricularCourse curricularCourse = null;
		IStudentCurricularPlan studentCurricularPlan = null;

		try {
			persistentSupport.iniciarTransaccao();
			curricularCourse =
				persistentCurricularCourse.readCurricularCourseByNameAndCode(
					"Trabalho Final de Curso I",
					"TFCI");
			studentCurricularPlan =
				persistentStudentCurricularPlan
					.readActiveStudentCurricularPlan(
					new Integer(45498),
					new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}

		assertNotNull(curricularCourse);
		assertNotNull(studentCurricularPlan);

		// DegreeEnrolmentInfo ja existente
		System.out.println("\n- Test 4.1 : Delete Existing DegreeEnrolmentInfo\n");
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfo =
				persistentDegreeEnrolmentInfo
					.readDegreeEnrolmentInfoByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing DegreeEnrolmentInfo To Delete");
		}
		assertNotNull(degreeEnrolmentInfo);

		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.delete(degreeEnrolmentInfo);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing DegreeEnrolmentInfo");
		}

		IDegreeEnrolmentInfo enr2 = null;
		try {
			persistentSupport.iniciarTransaccao();
			enr2 =
				persistentDegreeEnrolmentInfo
					.readDegreeEnrolmentInfoByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted DegreeEnrolmentInfo");
		}
		assertNull(enr2);

		// DegreeEnrolmentInfo inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing DegreeEnrolmentInfo\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentDegreeEnrolmentInfo.delete(new DegreeEnrolmentInfo());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing DegreeEnrolmentInfo");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllDegreeEnrolmentInfos() {

		List list = null;

		System.out.println("\n- Test 5 : Read All Existing DegreeEnrolmentInfo\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentDegreeEnrolmentInfo.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All DegreeEnrolmentInfos");
		}
		assertNotNull(list);
		assertEquals(6, list.size());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadDegreeEnrolmentInfoByStudentCurricularPlanAndDegreeEnrolmentInfoState() {

		List list = null;

		IStudentCurricularPlan studentCurricularPlan = null;

		System.out.println(
			"\n- Test 6 : Read Existing DegreeEnrolmentInfo By StudentCurricularPlan And DegreeEnrolmentInfoState\n");

		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlan =
				persistentStudentCurricularPlan
					.readActiveStudentCurricularPlan(
					new Integer(45498),
					new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading StudentCurricularPlan");
		}

		assertNotNull(studentCurricularPlan);

		// DegreeEnrolmentInfo ja existente
		try {
			persistentSupport.iniciarTransaccao();
			list =
				persistentDegreeEnrolmentInfo
					.readDegreeEnrolmentInfosByStudentCurricularPlanAndDegreeEnrolmentInfoState(
					studentCurricularPlan,
					new DegreeEnrolmentInfoState(DegreeEnrolmentInfoState.APROVED));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing DegreeEnrolmentInfo");
		}
		assertNotNull(list);
		assertEquals(1,list.size());
	}

	public void testReadAllByStudentCurricularPlan() {
		IStudentCurricularPlan studentCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();
			studentCurricularPlan =
				persistentStudentCurricularPlan
					.readActiveStudentCurricularPlan(
					new Integer(600),
					new TipoCurso(TipoCurso.LICENCIATURA));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading CurricularCourse & StudentCurricularPlan");
		}
		assertNotNull(
			"Can't find 4598 student curricular plan!",
			studentCurricularPlan);
		List degreeEnrolmentInfos = null;
		try {
			persistentSupport.iniciarTransaccao();
			degreeEnrolmentInfos =
				persistentDegreeEnrolmentInfo.readAllByStudentCurricularPlan(
					studentCurricularPlan);
			persistentDegreeEnrolmentInfo.readAllByStudentCurricularPlan(
				studentCurricularPlan);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("Reading all degreeEnrolmentInfos by student!");
		}
		assertNotNull("DegreeEnrolmentInfos must be not null!",degreeEnrolmentInfos);
		assertEquals(1, degreeEnrolmentInfos.size());
	}
*/
}