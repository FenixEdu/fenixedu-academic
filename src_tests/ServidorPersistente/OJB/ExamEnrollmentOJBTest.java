/*
 * Created on 22/Mai/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Exam;
import Dominio.ExamEnrollment;
import Dominio.IExam;
import Dominio.IExamEnrollment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamEnrollment;
/**
 * @author tania
 *
 */
public class ExamEnrollmentOJBTest extends TestCaseOJB {

	IPersistentExamEnrollment persistentExamEnrollment = null;

	/**
	 * @param testName
	 */
	public ExamEnrollmentOJBTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ExamEnrollmentOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		System.out.println("Setup");
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentExamEnrollment =
			(IPersistentExamEnrollment) sp.getIPersistentExamEnrollment();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {
		System.out.println("1 - testReadAll()");
		//					read all evaluation
		try {
			sp.iniciarTransaccao();
			List examEnrollmentList = persistentExamEnrollment.readAll();
			Iterator iter = examEnrollmentList.iterator();
			while (iter.hasNext()) {
				System.out.println(
					"examEnrollment = " + (IExamEnrollment) iter.next());
			}
			sp.confirmarTransaccao();
			assertEquals(2, examEnrollmentList.size());
			

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			fail("    -> Failed Reading Existing ExamEnrollments");
		}

	}

	public void testDeleteAll() {
		System.out.println("2 - DeleteAll()");
		//						delete all examEnrollment
		try {
			sp.iniciarTransaccao();
			persistentExamEnrollment.deleteAll();
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed deleting all Existing ExamEnrollments");
		}
		//		Read Again
		try {
			sp.iniciarTransaccao();
			List examEnrollmentList = persistentExamEnrollment.readAll();
			Iterator iter = examEnrollmentList.iterator();

			assertEquals(0, examEnrollmentList.size());
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed Reading Existing ExamEnrollments");
		}

	}

	public void testLockWrite() {
		System.out.println("3 - testLockWrite()");

		IExamEnrollment examEnrollment;
		IExamEnrollment examEnrollmentFromDB;
		IExam exam;

		examEnrollment = null;
		exam = new Exam();

		try {
			System.out.println("3.1 - write unexisting examEnrollment");

			sp.iniciarTransaccao();

			Calendar beginDate = Calendar.getInstance();
			beginDate.set(Calendar.YEAR, 2003);
			beginDate.set(Calendar.MONTH, Calendar.MAY);
			beginDate.set(Calendar.DATE, 01);
			beginDate.set(Calendar.HOUR_OF_DAY, 0);
			beginDate.set(Calendar.MINUTE, 0);
			beginDate.set(Calendar.SECOND, 0);
			beginDate.set(Calendar.MILLISECOND, 0);

			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.YEAR, 2003);
			endDate.set(Calendar.MONTH, Calendar.MAY);
			endDate.set(Calendar.DATE, 31);
			endDate.set(Calendar.HOUR_OF_DAY, 0);
			endDate.set(Calendar.MINUTE, 0);
			endDate.set(Calendar.SECOND, 0);
			endDate.set(Calendar.MILLISECOND, 0);

			exam.setIdInternal(new Integer("10"));

			exam = (IExam) sp.getIPersistentExam().readByOId(exam);

			examEnrollment = new ExamEnrollment();
			examEnrollment.setExam(exam);
			examEnrollment.setBeginDate(beginDate);
			examEnrollment.setEndDate(endDate);
			sp.getIPersistentExamEnrollment().lockWrite(examEnrollment);

			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed writing unexisting examEnrollment");

		}
		try {
			System.out.println("3.2 - read written examEnrollment");
			sp.iniciarTransaccao();

			Calendar beginDate = Calendar.getInstance();
			beginDate.set(Calendar.YEAR, 2003);
			beginDate.set(Calendar.MONTH, Calendar.MAY);
			beginDate.set(Calendar.DATE, 01);
			beginDate.set(Calendar.HOUR_OF_DAY, 0);
			beginDate.set(Calendar.MINUTE, 0);
			beginDate.set(Calendar.SECOND, 0);
			beginDate.set(Calendar.MILLISECOND, 0);

			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.YEAR, 2003);
			endDate.set(Calendar.MONTH, Calendar.MAY);
			endDate.set(Calendar.DATE, 31);
			endDate.set(Calendar.HOUR_OF_DAY, 0);
			endDate.set(Calendar.MINUTE, 0);
			endDate.set(Calendar.SECOND, 0);
			endDate.set(Calendar.MILLISECOND, 0);

			exam.setIdInternal(new Integer("10"));

			exam = (IExam) sp.getIPersistentExam().readByOId(exam);

			examEnrollment = new ExamEnrollment();
			examEnrollment.setExam(exam);
			examEnrollment.setBeginDate(beginDate);
			examEnrollment.setEndDate(endDate);

			examEnrollmentFromDB =
				persistentExamEnrollment.readIExamEnrollmentByExam(exam);

			assertNotNull("failed reading examEnrollment", examEnrollment);

			assertEquals(
				"failed reading written examEnrollment",
			examEnrollmentFromDB,
				examEnrollment);

			assertEquals(
				"failed reading written examEnrollment",
				examEnrollmentFromDB.getBeginDate(),
				examEnrollment.getBeginDate());

			assertEquals(
				"failed reading written examEnrollment",
				examEnrollmentFromDB.getEndDate(),
				examEnrollment.getEndDate());

			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed reading written examEnrollment");
		}

		try {
			System.out.println("3.3 - update existing examEnrollment");
			sp.iniciarTransaccao();

			exam.setIdInternal(new Integer("1"));
			exam = (IExam) sp.getIPersistentExam().readByOId(exam);
			examEnrollment =
				(IExamEnrollment) sp.getIPersistentExamEnrollment().readIExamEnrollmentByExam(exam);

			assertNotNull("failed reading examEnrollment", examEnrollment);

			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.YEAR, 2003);
			endDate.set(Calendar.MONTH, Calendar.MAY);
			endDate.set(Calendar.DATE, 27);
			endDate.set(Calendar.HOUR_OF_DAY, 0);
			endDate.set(Calendar.MINUTE, 0);
			endDate.set(Calendar.SECOND, 0);
			endDate.set(Calendar.MILLISECOND, 0);

			examEnrollment.setEndDate(endDate);

			persistentExamEnrollment.lockWrite(examEnrollment);
			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed  updating existing examEnrollment");

		}
		try {
			Object persistentIExamEnrollment = null;
			System.out.println("3.4 - read written examEnrollment");
			sp.iniciarTransaccao();

			exam.setIdInternal(new Integer("1"));
			examEnrollment =
				(IExamEnrollment) sp.getIPersistentExamEnrollment().readIExamEnrollmentByExam(exam);

			examEnrollmentFromDB =
				persistentExamEnrollment.readIExamEnrollmentByExam(exam);

			assertNotNull(
				"failed reading examEnrollment",
				examEnrollmentFromDB);

			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.YEAR, 2003);
			endDate.set(Calendar.MONTH, Calendar.MAY);
			endDate.set(Calendar.DATE, 27);
			endDate.set(Calendar.HOUR_OF_DAY, 0);
			endDate.set(Calendar.MINUTE, 0);
			endDate.set(Calendar.SECOND, 0);
			endDate.set(Calendar.MILLISECOND, 0);

			assertEquals(
				"failed on unique field while reading update ",
				examEnrollmentFromDB,
				examEnrollment);

			assertEquals(
				"failed on endDate while reading update ",
				examEnrollmentFromDB.getEndDate(),
				endDate);

			sp.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Failed reading written examEnrollment");
		}
	}

}
