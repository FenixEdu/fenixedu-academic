/*
 * ExamOJBTest.java
 * JUnit based test
 *
 * Created on 2003/03/29
 */

package ServidorPersistente.OJB;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.Season;

public class ExamExecutionCourseOJBTest extends TestCaseOJB {

	SuportePersistenteOJB ps = null;

	public ExamExecutionCourseOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ExamExecutionCourseOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();

		try {
			ps = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {
		try {
			ps.iniciarTransaccao();

			List examExecutionCourseList = ps.getIPersistentExamExecutionCourse().readAll();
			assertNotNull("testReadAll: result was null", examExecutionCourseList);
			assertEquals("testReadAll: expected a diferent number of results", 13, examExecutionCourseList.size());

			ps.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("testReadAll: unexpected exception: " + e);
		}
	}

	public void testReadByExamAndExecutionCourse() {
		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2003);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 13);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		IDisciplinaExecucao executionCourse = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = null;
		IExam exam = null;
		IExamExecutionCourse examExecutionCourse = null;

		try {
			ps.iniciarTransaccao();
			executionYear =	ps.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			executionPeriod =ps.getIPersistentExecutionPeriod().readByNameAndExecutionYear("2º Semestre", executionYear);
			executionCourse = ps.getIDisciplinaExecucaoPersistente().readByExecutionCourseInitialsAndExecutionPeriod("RCI", executionPeriod);
			exam = ps.getIPersistentExam().readBy(beginning.getTime(), beginning, executionCourse);
			// Make sure test data set is ok
			assertNotNull("testReadByExamExecutionCourse: test data (executionCourse) have been altered!!!",executionCourse);
			assertNotNull("testReadByExamExecutionCourse: test data (exans) have been altered!!!", exam);

			// Read Existing
			examExecutionCourse = ps.getIPersistentExamExecutionCourse().readBy(exam, executionCourse);
			assertNotNull("testReadByExamAndExecutionCourse: expected a result",examExecutionCourse);

			// Prepare for next test
			ps.confirmarTransaccao();
			exam.setSeason(new Season(Season.SEASON2));
			ps.iniciarTransaccao();
			executionCourse = ps.getIDisciplinaExecucaoPersistente().readByExecutionCourseInitialsAndExecutionPeriod("EP",executionPeriod);
			// Make sure test data set is ok
			assertNotNull("testReadByExamExecutionCourse: test data (executionCourse) have been altered!!!",executionCourse);

			// Read Non-Existing
			examExecutionCourse =ps.getIPersistentExamExecutionCourse().readBy(exam, executionCourse);
			assertNull("testReadByExamAndExecutionCourse: expected no result",	examExecutionCourse);

			ps.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("testReadByDayAndBeginningAndExecutionCourse: unexpected exception: " + e);
		}
	}

}
