package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.Equivalence;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEquivalence;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEquivalence;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.EquivalenceType;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class EquivalenceOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentEnrolment persistentEnrolment = null;
	IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	IPersistentCurricularCourse persistentCurricularCourse = null;
	IPersistentEquivalence persistentEquivalence = null;
	IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
	IPersistentCurricularYear persistentCurricularYear = null;
	IPersistentCurricularSemester persistentCurricularSemester = null;
	
	IEnrolment enrolment = null;
	IEnrolment equivalentEnrolment = null;

	public EquivalenceOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class EquivalenceOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class EquivalenceOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EquivalenceOJBTest.class);
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
		persistentEnrolment = persistentSupport.getIPersistentEnrolment();
		persistentEquivalence = persistentSupport.getIPersistentEquivalence();
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentCurricularCourseScope = persistentSupport.getIPersistentCurricularCourseScope();
		persistentCurricularYear = persistentSupport.getIPersistentCurricularYear();
		persistentCurricularSemester = persistentSupport.getIPersistentCurricularSemester();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadByEnrolmentAndEquivalentEnrolment(){
		loadEnrolments(true);
		IEquivalence equivalence = null;
		try {
			persistentSupport.iniciarTransaccao();
			equivalence = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			fail("Unexpected exception!");
		}
		assertNotNull("Equivalence is null!",equivalence);
	}


	public void testWriteEquivalence() {

		System.out.println("\n- Test 1.1 : Write Existing Equivalence\n");

		// Equivalence ja existente
		this.loadEnrolments(true);
		
		IEquivalence equivalence = new Equivalence(this.enrolment, this.equivalentEnrolment, new EquivalenceType(EquivalenceType.EQUIVALENT_COURSE));

		try {
			persistentSupport.iniciarTransaccao();
			persistentEquivalence.lockWrite(equivalence);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing Equivalence");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing Equivalence");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing Equivalence");
		}

		// Equivalence inexistente
		this.loadEnrolments(false);
		equivalence = new Equivalence(this.enrolment, this.equivalentEnrolment, new EquivalenceType(EquivalenceType.EQUIVALENT_COURSE));

		System.out.println("\n- Test 1.2 : Write Non Existing Equivalence\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentEquivalence.lockWrite(equivalence);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing Equivalence");
		}

		IEquivalence equivalence2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			equivalence2 = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading New Existing Equivalence Just Writen Before");
		}

		assertNotNull(equivalence2);
		assertTrue(equivalence2.getEnrolment().equals(this.enrolment));
		assertTrue(equivalence2.getEquivalentEnrolment().equals(this.equivalentEnrolment));
		assertTrue(equivalence2.getEquivalenceType().equals(new EquivalenceType(EquivalenceType.EQUIVALENT_COURSE)));
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllEquivalences() {

		System.out.println("\n- Test 2 : Delete All Equivalences");
		try {
			persistentSupport.iniciarTransaccao();
			persistentEquivalence.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Delete All Equivalences");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentEquivalence.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All Equivalences");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadEquivalence() {

		System.out.println("\n- Test 3.1 : Read Existing Equivalence\n");

		// Equivalence ja existente
		this.loadEnrolments(true);
		IEquivalence equivalence = null;

		try {
			persistentSupport.iniciarTransaccao();
			equivalence = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing Equivalence");
		}
		assertNotNull(equivalence);
		assertTrue(equivalence.getEnrolment().equals(this.enrolment));
		assertTrue(equivalence.getEquivalentEnrolment().equals(this.equivalentEnrolment));
		assertTrue(equivalence.getEquivalenceType().equals(new EquivalenceType(EquivalenceType.EQUIVALENT_COURSE)));

		// Equivalence inexistente
		System.out.println("\n- Test 3.2 : Read Non Existing Equivalence");

		this.loadEnrolments(false);

		try {
			persistentSupport.iniciarTransaccao();
			equivalence = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing Equivalence");
		}
		assertNull(equivalence);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteEquivalence() {

		// Equivalence ja existente
		System.out.println("\n- Test 4.1 : Delete Existing Equivalence\n");
		this.loadEnrolments(true);
		IEquivalence equivalence = null;

		try {
			persistentSupport.iniciarTransaccao();
			equivalence = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing Equivalence To Delete");
		}
		assertNotNull(equivalence);

		try {
			persistentSupport.iniciarTransaccao();
			persistentEquivalence.delete(equivalence);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing Equivalence");
		}

		try {
			persistentSupport.iniciarTransaccao();
			equivalence = persistentEquivalence.readEquivalenceByEnrolmentAndEquivalentEnrolment(this.enrolment, this.equivalentEnrolment);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted Equivalence");
		}
		assertNull(equivalence);

		// Equivalence inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing Equivalence\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentEquivalence.delete(new Equivalence());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing Equivalence");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllEquivalences() {

		ArrayList list = null;

		System.out.println("\n- Test 5 : Read All Existing Equivalence\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentEquivalence.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All Equivalences");
		}
		assertNotNull(list);
		assertEquals(list.size(), 1);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private void loadEnrolments(boolean exists) {

		ICurricularCourse curricularCourse = null;
		IStudentCurricularPlan studentCurricularPlan = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurricularYear curricularYear = null;
		ICurricularSemester curricularSemester = null;
		ICurricularCourseScope curricularCourseScope = null;
		IBranch branch = null;

		if(exists) {// Enrolment ja existente
			try {
				persistentSupport.iniciarTransaccao();

				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
				degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
				branch = studentCurricularPlan.getBranch();
				ICurricularCourse curricularCourseCriteria = new CurricularCourse();
				curricularCourseCriteria.setName("Cadeira a Equivaler");
				curricularCourseCriteria.setCode("CAE");
//				curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria);
				curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
				curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
				curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);

				assertNotNull(studentCurricularPlan);
				assertNotNull(degreeCurricularPlan);
				assertNotNull(branch);
				assertNotNull(curricularCourse);
				assertNotNull(curricularYear);
				assertNotNull(curricularSemester);
				assertNotNull(curricularCourseScope);
								
				this.enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(studentCurricularPlan, curricularCourseScope);
				assertNotNull(enrolment);

				ICurricularCourse curricularCourseCriteria2 = new CurricularCourse();
				curricularCourseCriteria.setName("Cadeira Equivalente");
				curricularCourseCriteria.setCode("CE");
//				curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria2);
				curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);

				assertNotNull(studentCurricularPlan);
				assertNotNull(curricularCourse);
				assertNotNull(curricularCourseScope);

				this.equivalentEnrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(studentCurricularPlan, curricularCourseScope);
				assertNotNull(equivalentEnrolment);

				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Loading Enrolments from DB.");
			}
		} else {
			try {
				persistentSupport.iniciarTransaccao();
				
				IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
				IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
				
				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
				degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
				branch = studentCurricularPlan.getBranch();
				ICurricularCourse curricularCourseCriteria = new CurricularCourse();
				curricularCourseCriteria.setName("Cadeira Que Não Tem Enrolment 1");
				curricularCourseCriteria.setCode("CNE1");
				curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
//				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria);
				curricularYear = persistentCurricularYear.readCurricularYearByYear(new Integer(1));
				curricularSemester = persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(new Integer(1), curricularYear);
				curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);

				assertNotNull(studentCurricularPlan);
				assertNotNull(degreeCurricularPlan);
				assertNotNull(branch);
				assertNotNull(curricularCourse);
				assertNotNull(curricularYear);
				assertNotNull(curricularSemester);
				assertNotNull(curricularCourseScope);

				this.enrolment = new Enrolment(studentCurricularPlan, curricularCourseScope, EnrolmentState.APROVED_OBJ);
				this.enrolment.setExecutionPeriod(executionPeriod);
				this.enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);

				ICurricularCourse curricularCourseCriteria2 = new CurricularCourse();
				curricularCourseCriteria.setName("Cadeira Equivalente");
				curricularCourseCriteria.setCode("CE");
//				curricularCourseCriteria.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseCriteria2);
				curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);

				assertNotNull(studentCurricularPlan);
				assertNotNull(curricularCourse);
				assertNotNull(curricularCourseScope);

				this.equivalentEnrolment = new Enrolment(studentCurricularPlan, curricularCourseScope, EnrolmentState.APROVED_OBJ);
				this.equivalentEnrolment.setExecutionPeriod(executionPeriod);
				this.enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);

				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Loading Enrolments from DB.");
			}
		}
	}

}