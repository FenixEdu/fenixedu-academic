package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;

/*
 * Created on 23/Abr/2003
 *
 * 
 */

/**
 * @author jmota
 *
 * 
 */
public class EvaluationMethodOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentEvaluationMethod persistentEvaluation = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null;

	/**
		 * @param testName
		 */
	public EvaluationMethodOJBTest(String testName) {
		super(testName);

	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EvaluationMethodOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		System.out.println("Setup");
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentEvaluation = persistentSupport.getIPersistentEvaluationMethod();
		persistentExecutionPeriod =
			persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionYear =
			persistentSupport.getIPersistentExecutionYear();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {
		System.out.println("1 - testReadAll()");
		//					read all evaluation
		try {
			persistentSupport.iniciarTransaccao();
			List evaluationList = persistentEvaluation.readAll();
			Iterator iter = evaluationList.iterator();
			while (iter.hasNext()) {
				System.out.println("evaluation= " + (IEvaluationMethod) iter.next());
			}

			assertEquals(2, evaluationList.size());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Evaluations");
		}

	}

	public void testDeleteAll() {
		System.out.println("2 - DeleteAll()");
		//						delete all evaluation
		try {
			persistentSupport.iniciarTransaccao();
			persistentEvaluation.deleteAll();
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed deleting all Existing Evaluations");
		}
		//		Read Again
		try {
			persistentSupport.iniciarTransaccao();
			List evaluationList = persistentEvaluation.readAll();
	

			assertEquals(0, evaluationList.size());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Evaluations");
		}

	}

//	public void testLockWrite() {
//		System.out.println("3 - testLockWrite()");
//		IExecutionYear executionYear;
//		IExecutionPeriod executionPeriod;
//		IDisciplinaExecucao executionCourse;
//		IEvaluationMethod evaluation;
//		IEvaluationMethod evaluationFromDB;
//
//		try {
//			System.out.println("3.1 - write unexisting evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//
//			evaluation = new EvaluationMethod(executionCourse, "blablabla");
//			persistentEvaluation.lockWrite(evaluation);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed writing unexisting evaluation");
//		}
//		try {
//			System.out.println("3.2 - read written evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//			evaluationFromDB =
//				persistentEvaluation.readByExecutionCourse(executionCourse);
//			assertNotNull(
//				"failed reading written evaluation",
//				evaluationFromDB);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getExecutionCourse(),
//				executionCourse);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getEvaluationElements(),
//				"blablabla");
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed reading written evaluation");
//		}
//
//		try {
//			System.out.println("3.3 - write existing evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//
//			evaluation = new EvaluationMethod(executionCourse, "blablabla");
//			persistentEvaluation.lockWrite(evaluation);
//			persistentSupport.confirmarTransaccao();
//			fail("    -> Failed  writting existing evaluation");
//		} catch (ExcepcaoPersistencia ex) {
//
//		}
//		try {
//			System.out.println("3.4 - update existing evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//
//			evaluation =
//				persistentEvaluation.readByExecutionCourse(executionCourse);
//			evaluation.setEvaluationElements("xpto");
//			persistentEvaluation.lockWrite(evaluation);
//			persistentSupport.confirmarTransaccao();
//
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed  updating existing evaluation");
//
//		}
//		try {
//			System.out.println("3.5 - read written evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//			evaluationFromDB =
//				persistentEvaluation.readByExecutionCourse(executionCourse);
//			assertNotNull(
//				"failed reading written evaluation",
//				evaluationFromDB);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getExecutionCourse(),
//				executionCourse);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getEvaluationElements(),
//				"xpto");
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed reading written evaluation");
//		}
//	}

	public void testDelete() {
		System.out.println("4 - testDelete()");
		IExecutionYear executionYear;
		IExecutionPeriod executionPeriod;
		IDisciplinaExecucao executionCourse;
		IEvaluationMethod evaluation;
		IEvaluationMethod evaluationFromDB;
		try {
			System.out.println("4.1 - delete existing evaluation");
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull("failed reading ExecutionYear", executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);

			evaluation =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			assertNotNull("failed reading existing evaluation", evaluation);
			persistentEvaluation.delete(evaluation);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed  deleting existing evaluation");

		}
		try {
			System.out.println("4.2 - read deleted evaluation");
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull("failed reading ExecutionYear", executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);
			evaluationFromDB =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			assertNull("failed reading written evaluation", evaluationFromDB);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed reading deleted evaluation");
		}

	}

//	public void testReadByExecutionCourse() {
//		System.out.println("5 - testReadByExecutionCourse()");
//		IExecutionYear executionYear;
//		IExecutionPeriod executionPeriod;
//		IDisciplinaExecucao executionCourse;
//		IEvaluationMethod evaluation;
//		IEvaluationMethod evaluationFromDB;
//		try {
//			System.out.println("5.1 - read unexisting evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"PO",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//			evaluationFromDB =
//				persistentEvaluation.readByExecutionCourse(executionCourse);
//			assertNull(
//				"failed reading unexisting evaluation",
//				evaluationFromDB);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed reading unexisting evaluation");
//		}
//		try {
//			System.out.println("5.2 - read existing evaluation");
//			persistentSupport.iniciarTransaccao();
//			executionYear =
//				persistentExecutionYear.readExecutionYearByName("2002/2003");
//			assertNotNull("failed reading ExecutionYear", executionYear);
//			executionPeriod =
//				persistentExecutionPeriod.readByNameAndExecutionYear(
//					"2º Semestre",
//					executionYear);
//			assertNotNull("failed reading  ExecutionPeriod", executionPeriod);
//			executionCourse =
//				persistentExecutionCourse
//					.readByExecutionCourseInitialsAndExecutionPeriod(
//					"TFCI",
//					executionPeriod);
//			assertNotNull("failed reading executionCourse", executionCourse);
//			evaluationFromDB =
//				persistentEvaluation.readByExecutionCourse(executionCourse);
//			assertNotNull(
//				"failed reading existing evaluation",
//				evaluationFromDB);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getExecutionCourse(),
//				executionCourse);
//			assertEquals(
//				"failed reading written evaluation",
//				evaluationFromDB.getEvaluationElements(),
//				"bla");
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("    -> Failed reading existing evaluation");
//		}
//	}

}
