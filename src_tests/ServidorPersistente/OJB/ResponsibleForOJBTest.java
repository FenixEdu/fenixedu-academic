/*
 * Created on 28/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;

/**
 * @author jmota
 *
 */
public class ResponsibleForOJBTest extends TestCaseOJB {
	private SuportePersistenteOJB persistentSupport = null;
	private IDisciplinaExecucao executionCourse = null;
	private IDisciplinaExecucao executionCourseWithoutResponsibleFors = null;
	private IExecutionPeriod executionPeriod = null;
	private IExecutionYear executionYear = null;
	private ITeacher teacher = null;
	private ITeacher teacherWithoutResponsibleFor = null;
	private IPersistentResponsibleFor persistentResponsibleFor = null;
	private IPersistentTeacher persistentTeacher = null;
	private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private IPersistentExecutionPeriod persistentExecutionPeriod = null;
	private IPersistentExecutionYear persistentExecutionYear = null;
	/**
	 * @param testName
	 */
	public ResponsibleForOJBTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ResponsibleForOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error while setting up");
		}
		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentExecutionPeriod =
			persistentSupport.getIPersistentExecutionPeriod();
		persistentExecutionYear =
			persistentSupport.getIPersistentExecutionYear();
		persistentTeacher = persistentSupport.getIPersistentTeacher();
		persistentResponsibleFor =
			persistentSupport.getIPersistentResponsibleFor();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testDelete() {

		//read existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(1));
			assertNotNull("testDelete: failed reading teacher", teacher);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(
				"testDelete: failed reading executionYear",
				executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(
				"testDelete: failed reading executionPeriod",
				executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(
				"testDelete: failed reading executionCourse",
				executionCourse);
			IResponsibleFor responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testDelete: failed reading responsibleFor",
				responsibleFor);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Test delete responsibleFor: failed in the test setup");
		}
		//delete responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			IResponsibleFor responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testDelete: failed reading responsibleFor",
				responsibleFor);
			persistentResponsibleFor.delete(responsibleFor);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete responsibleFor: failed deleting");
		}
		//read again
		try {
			persistentSupport.iniciarTransaccao();
			IResponsibleFor responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNull(
				"testDelete: failed deleting responsibleFor",
				responsibleFor);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete responsibleFor: failed reading deleted responsibleFor");
		}
	}
	public void testReadByTeacherAndExecutionCourse() {
		//		setup
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(1));
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading teacher",
				teacher);
			teacherWithoutResponsibleFor =
				persistentTeacher.readTeacherByNumber(new Integer(4));
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading teacher",
				teacherWithoutResponsibleFor);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading executionYear",
				executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading executionPeriod",
				executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading executionCourse",
				executionCourse);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("testReadByTeacherAndExecutionCourse: failed in the test setup");
		}
		//read existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			IResponsibleFor responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading responsibleFor",
				responsibleFor);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByTeacherAndExecutionCourse: failed reading responsibleFor. ExecutionCourse not equal!",
				responsibleFor.getExecutionCourse(),
				executionCourse);
			assertEquals(
				"testReadByTeacherAndExecutionCourse: failed reading responsibleFor. Teacher not equal!",
				responsibleFor.getTeacher(),
				teacher);
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacherAndExecutionCourse: failed reading");
		}
		//read non Existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			IResponsibleFor responsibleFor =
				persistentResponsibleFor.readByTeacherAndExecutionCourse(
					teacherWithoutResponsibleFor,
					executionCourse);
			assertNull(
				"testReadByTeacherAndExecutionCourse: failed reading non existing responsibleFor"
					+ responsibleFor,
				responsibleFor);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacherAndExecutionCourse: failed reading non existing responsibleFor");
		}

	}

	public void testReadByTeacher() {
		//		setup
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(1));
			assertNotNull("testReadByTeacher: failed reading teacher", teacher);
			teacherWithoutResponsibleFor =
				persistentTeacher.readTeacherByNumber(new Integer(4));
			assertNotNull(
				"testReadByTeacher: failed reading teacher",
				teacherWithoutResponsibleFor);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("testReadByTeacher: failed in the test setup");
		}
		//read existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors =
				persistentResponsibleFor.readByTeacher(teacher);
			assertNotNull(
				"testReadByTeacher: failed reading responsibleFor",
				responsibleFors);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByTeacher: failed reading responsibleFor.List size diferent",
				responsibleFors.size(),
				3);
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacher: failed reading");
		}
		//read non Existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors =
				persistentResponsibleFor.readByTeacher(
					teacherWithoutResponsibleFor);
			assertEquals(
				"testReadByTeacher: failed reading non existing responsibleFor"
					+ responsibleFors,
				responsibleFors.size(),
				0);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacher: failed reading non existing  responsibleFor");
		}

	}

	public void testReadByExecutionCourse() {
		//		setup
		try {
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(
				"testReadByExecutionCourse: failed reading executionYear",
				executionYear);
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			assertNotNull(
				"testReadByExecutionCourse: failed reading executionPeriod",
				executionPeriod);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			assertNotNull(
				"testReadByExecutionCourse: failed reading executionCourse",
				executionCourse);
			executionCourseWithoutResponsibleFors =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"PO",
					executionPeriod);
			assertNotNull(
				"testReadByExecutionCourse: failed reading executionCourse",
				executionCourseWithoutResponsibleFors);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("testReadByExecutionCourse: failed in the test setup");
		}
		//read existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors =
				persistentResponsibleFor.readByExecutionCourse(executionCourse);
			assertNotNull(
				"testReadByExecutionCourse: failed reading responsibleFor",
				responsibleFors);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByExecutionCourse: failed reading responsibleFor.List size diferent",
				responsibleFors.size(),
				1);
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByExecutionCourse: failed reading");
		}
		//read non Existing responsibleFor
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors =
				persistentResponsibleFor.readByExecutionCourse(
					executionCourseWithoutResponsibleFors);
			assertEquals(
				"testReadByExecutionCourse: failed reading non existing responsibleFor"
					+ responsibleFors,
				responsibleFors.size(),
				0);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByExecutionCourse: failed reading non existing  responsibleFor");
		}
	}

	public void testReadAll() {

		//read all responsibleFors
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors = persistentResponsibleFor.readAll();
			assertNotNull(
				"testReadAll: failed reading all responsibleFors",
				responsibleFors);
			assertEquals(
				"testReadAll: failed reading all responsibleFors",
				3,
				responsibleFors.size());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadAll: failed deleting");
		}
		
	}

	public void testDeleteAll() {

		//delete all responsibleFors
		try {
			persistentSupport.iniciarTransaccao();
			persistentResponsibleFor.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete responsibleFor: failed deleting");
		}
		//read again
		try {
			persistentSupport.iniciarTransaccao();
			List responsibleFors = persistentResponsibleFor.readAll();
			assertEquals(
				"testDelete: failed deleting responsibleFors",
				0,
				responsibleFors.size());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete responsibleFor: failed reading deleted responsibleFor");
		}
	}
}
