package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Evaluation;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.TestCaseOJB;

/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EvaluationOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IPersistentEvaluation persistentEvaluation = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	IPersistentExecutionYear persistentExecutionYear = null;

	/**
		 * @param testName
		 */
	public EvaluationOJBTest(String testName) {
		super(testName);

	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EvaluationOJBTest.class);
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
		persistentEvaluation = persistentSupport.getIPersistentEvaluation();
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
				System.out.println("evaluation= " + (IEvaluation) iter.next());
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
			Iterator iter = evaluationList.iterator();

			assertEquals(0, evaluationList.size());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing Evaluations");
		}

	}

	public void testLockWrite() {
		System.out.println("3 - testLockWrite()");
		IExecutionYear executionYear;
		IExecutionPeriod executionPeriod;
		IDisciplinaExecucao executionCourse;
		IEvaluation evaluation;
		IEvaluation evaluationFromDB;

		try {
			System.out.println("3.1 - write unexisting evaluation");
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
					"PO",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);

			evaluation = new Evaluation(executionCourse, "blablabla");
			persistentEvaluation.lockWrite(evaluation);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed writing unexisting evaluation");
		}
		try {
			System.out.println("3.2 - read written evaluation");
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
					"PO",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);
			evaluationFromDB =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			assertNotNull(
				"failed reading written evaluation",
				evaluationFromDB);
			assertEquals(
				"failed reading written evaluation",
				evaluationFromDB.getExecutionCourse(),
				executionCourse);
			assertEquals(
				"failed reading written evaluation",
				evaluationFromDB.getEvaluationElements(),
				"blablabla");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed reading written evaluation");
		}

		try {
			System.out.println("3.3 - write existing evaluation");
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
					"PO",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);

			evaluation = new Evaluation(executionCourse, "blablabla");
			persistentEvaluation.lockWrite(evaluation);
			persistentSupport.confirmarTransaccao();
			fail("    -> Failed  writting existing evaluation");
		} catch (ExcepcaoPersistencia ex) {

		}
		try {
			System.out.println("3.4 - update existing evaluation");
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
					"PO",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);

			evaluation =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			evaluation.setEvaluationElements("xpto");
			persistentEvaluation.lockWrite(evaluation);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed  updating existing evaluation");

		}
		try {
			System.out.println("3.5 - read written evaluation");
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
					"PO",
					executionPeriod);
			assertNotNull("failed reading executionCourse", executionCourse);
			evaluationFromDB =
				persistentEvaluation.readByExecutionCourse(executionCourse);
			assertNotNull(
				"failed reading written evaluation",
				evaluationFromDB);
			assertEquals(
				"failed reading written evaluation",
				evaluationFromDB.getExecutionCourse(),
				executionCourse);
			assertEquals(
				"failed reading written evaluation",
				evaluationFromDB.getEvaluationElements(),
				"xpto");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed reading written evaluation");
		}
	}

}
