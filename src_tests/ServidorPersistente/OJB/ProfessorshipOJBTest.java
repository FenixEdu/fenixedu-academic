/*
 * Created on 26/Mar/2003
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
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;

/**
 * @author jmota
 *
 */
public class ProfessorshipOJBTest extends TestCaseOJB {
	private SuportePersistenteOJB persistentSupport = null;
	private IDisciplinaExecucao executionCourse = null;
	private IDisciplinaExecucao executionCourseWithoutProfessorships = null;
	private IExecutionPeriod executionPeriod = null;
	private IExecutionYear executionYear = null;
	private ITeacher teacher = null;
	private ITeacher teacherWithoutProfessorship = null;
	private IPersistentProfessorship persistentProfessorship = null;
	private IPersistentTeacher persistentTeacher = null;
	private IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	private IPersistentExecutionPeriod persistentExecutionPeriod = null;
	private IPersistentExecutionYear persistentExecutionYear = null;
	/**
	 * @param testName
	 */
	public ProfessorshipOJBTest(String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ProfessorshipOJBTest.class);
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
		persistentProfessorship =
			persistentSupport.getIPersistentProfessorship();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testDelete() {

		//read existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(2));
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
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testDelete: failed reading professorship",
				professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Test delete professorship: failed in the test setup");
		}
		//delete professorship
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testDelete: failed reading professorship",
				professorship);
			persistentProfessorship.delete(professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed deleting");
		}
		//read again
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNull(
				"testDelete: failed deleting professorship",
				professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed reading deleted professorship");
		}
	}
	public void testReadByTeacherAndExecutionCourse() {
		//		setup
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(2));
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading teacher",
				teacher);
			teacherWithoutProfessorship =
				persistentTeacher.readTeacherByNumber(new Integer(4));
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading teacher",
				teacherWithoutProfessorship);
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
		//read existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);
			assertNotNull(
				"testReadByTeacherAndExecutionCourse: failed reading professorship",
				professorship);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByTeacherAndExecutionCourse: failed reading professorship. ExecutionCourse not equal!",
				professorship.getExecutionCourse(),
				executionCourse);
			assertEquals(
				"testReadByTeacherAndExecutionCourse: failed reading professorship. Teacher not equal!",
				professorship.getTeacher(),
				teacher);
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacherAndExecutionCourse: failed reading");
		}
		//read non Existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			IProfessorship professorship =
				persistentProfessorship.readByTeacherAndExecutionCourse(
					teacherWithoutProfessorship,
					executionCourse);
			assertNull(
				"testReadByTeacherAndExecutionCourse: failed reading non existing professorship"
					+ professorship,
				professorship);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacherAndExecutionCourse: failed reading non existing professorship");
		}

	}

	public void testReadByTeacher() {
		//		setup
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer(2));
			assertNotNull("testReadByTeacher: failed reading teacher", teacher);
			teacherWithoutProfessorship =
				persistentTeacher.readTeacherByNumber(new Integer(4));
			assertNotNull(
				"testReadByTeacher: failed reading teacher",
				teacherWithoutProfessorship);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("testReadByTeacher: failed in the test setup");
		}
		//read existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			List professorships =
				persistentProfessorship.readByTeacher(teacher);
			assertNotNull(
				"testReadByTeacher: failed reading professorship",
				professorships);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByTeacher: failed reading professorship.List size diferent",
				professorships.size(),
				1);
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacher: failed reading");
		}
		//read non Existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			List professorships =
				persistentProfessorship.readByTeacher(
					teacherWithoutProfessorship);
			assertEquals(
				"testReadByTeacher: failed reading non existing professorship"
					+ professorships,
				professorships.size(),
				0);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByTeacher: failed reading non existing  professorship");
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
			executionCourseWithoutProfessorships =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"PO",
					executionPeriod);
			assertNotNull(
				"testReadByExecutionCourse: failed reading executionCourse",
				executionCourseWithoutProfessorships);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("testReadByExecutionCourse: failed in the test setup");
		}
		//read existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			List professorships =
				persistentProfessorship.readByExecutionCourse(executionCourse);
			assertNotNull(
				"testReadByExecutionCourse: failed reading professorship",
				professorships);
			persistentSupport.confirmarTransaccao();
			assertEquals(
				"testReadByExecutionCourse: failed reading professorship.List size diferent",
				2, professorships.size());
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByExecutionCourse: failed reading");
		}
		//read non Existing professorship
		try {
			persistentSupport.iniciarTransaccao();
			List professorships =
				persistentProfessorship.readByExecutionCourse(
					executionCourseWithoutProfessorships);
			assertEquals(
				"testReadByExecutionCourse: failed reading non existing professorship"
					+ professorships,
				professorships.size(),
				0);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadByExecutionCourse: failed reading non existing  professorship");
		}
	}

	public void testReadAll() {

		//read all professorships
		try {
			persistentSupport.iniciarTransaccao();
			List professorships = persistentProfessorship.readAll();
			assertNotNull(
				"testReadAll: failed reading all professorships",
				professorships);
			assertEquals(
				"testReadAll: failed reading all professorships",
				4,
				professorships.size());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("testReadAll: failed deleting");
		}
		
	}

	public void testDeleteAll() {

		//delete all professorships
		try {
			persistentSupport.iniciarTransaccao();
			persistentProfessorship.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed deleting");
		}
		//read again
		try {
			persistentSupport.iniciarTransaccao();
			List professorships = persistentProfessorship.readAll();
			assertEquals(
				"testDelete: failed deleting professorships",
				0,
				professorships.size());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Test delete professorship: failed reading deleted professorship");
		}
	}
}
