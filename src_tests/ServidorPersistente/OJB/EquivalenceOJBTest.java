package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Enrolment;
import Dominio.Equivalence;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEquivalence;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEquivalence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
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
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

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

		if(exists) {// Enrolment ja existente
			try {
				persistentSupport.iniciarTransaccao();
				curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso I", "TFCI");
				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
				assertNotNull(curricularCourse);
				assertNotNull(studentCurricularPlan);
				this.enrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourse(studentCurricularPlan, curricularCourse);

				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
				assertNotNull(studentCurricularPlan);
				this.equivalentEnrolment = persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourse(studentCurricularPlan, curricularCourse);

				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Loading Enrolments from DB.");
			}
		} else {
			try {
				persistentSupport.iniciarTransaccao();
				curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode("Trabalho Final de Curso II", "TFCII");
				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
				assertNotNull(curricularCourse);
				assertNotNull(studentCurricularPlan);
				this.enrolment = new Enrolment(studentCurricularPlan, curricularCourse, new EnrolmentState(EnrolmentState.APROVED));

				studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
				assertNotNull(studentCurricularPlan);
				this.equivalentEnrolment = new Enrolment(studentCurricularPlan, curricularCourse, new EnrolmentState(EnrolmentState.APROVED));

				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("Loading Enrolments from DB.");
			}
		}
	}

}