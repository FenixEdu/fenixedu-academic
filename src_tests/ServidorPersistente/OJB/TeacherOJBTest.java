package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;

/**
 * @author Ivo Brandão
 */
public class TeacherOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentTeacher persistentTeacher = null;

	public TeacherOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TeacherOJBTest.class);

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
		persistentTeacher = persistentSupport.getIPersistentTeacher();
	}

	protected void tearDown() {
		super.tearDown();
	}

	//	public void testLockWrite() {
	//		ITeacher teacher = null;
	//
	//		// write non existing
	//		IPessoa person =
	//			new Pessoa(
	//				new TipoDocumentoIdentificacao(
	//					TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE),
	//				"eusoufixe");
	//
	//		teacher = new Teacher(person, new Integer("22"));
	//
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			persistentTeacher.lockWrite(teacher);
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testLockWrite: write non Existing");
	//		}
	//
	//		ITeacher teacherRead = null;
	//
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacherRead =
	//				persistentTeacher.readTeacherByNumber(
	//					teacher.getTeacherNumber());
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testEscreverPais: unexpected exception reading");
	//		}
	//		assertNotNull(teacherRead);
	//		assertEquals(
	//			teacherRead.getTeacherNumber(),
	//			teacher.getTeacherNumber());
	//		assertEquals(
	//			teacherRead.getResponsibleForExecutionCourses(),
	//			teacher.getResponsibleForExecutionCourses());
	//		assertEquals(
	//			teacherRead.getProfessorShipsExecutionCourses(),
	//			teacher.getProfessorShipsExecutionCourses());
	//
	//	}
	//
	//	public void testDeleteAllTeachers() {
	//
	//		//read all existing
	//		List result = null;
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result = persistentTeacher.readAll();
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex) {
	//			fail("testDeleteAllTeachers: readAll");
	//		}
	//		assertNotNull(result);
	//		assertEquals(result.isEmpty(), false);
	//
	//		//erase all existing        
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			persistentTeacher.deleteAll();
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex2) {
	//			fail("testDeleteAllTeachers: deleteAll");
	//		}
	//
	//		//read all existing again
	//		result = null;
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result = persistentTeacher.readAll();
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex) {
	//			fail("testDeleteAllTeachers: readAll");
	//		}
	//		assertNotNull(result);
	//		assertTrue(result.isEmpty());
	//	}
	//
	//	public void testReadTeacherByUsername() {
	//		ITeacher teacher = null;
	//
	//		//read existing
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByUsername("user");
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadTeacherByUsername: confirmarTransaccao");
	//		}
	//		assertNotNull(teacher);
	//		assertTrue(teacher.getTeacherNumber().equals(new Integer("1")));
	//		assertEquals(teacher.getResponsibleForExecutionCourses().size(), 3);
	//		assertEquals(teacher.getProfessorShipsExecutionCourses().size(), 3);
	//
	//		//read unexisting
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByUsername("teacher2");
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadTeacherByUsername: confirmarTransaccao");
	//		}
	//		assertNull(teacher);
	//	}
	//
	//	public void testReadTeacherByNumber() {
	//		ITeacher teacher = null;
	//
	//		//read existing
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadTeacherByNumber: confirmarTransaccao");
	//		}
	//		assertNotNull(teacher);
	//		assertTrue(teacher.getTeacherNumber().equals(new Integer("1")));
	//		assertEquals(teacher.getResponsibleForExecutionCourses().size(), 3);
	//		assertEquals(teacher.getProfessorShipsExecutionCourses().size(), 3);
	//
	//		//read unexisting
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByNumber(new Integer("52"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadTeacherByNumber: confirmarTransaccao");
	//		}
	//		assertNull(teacher);
	//	}
	//
	//	public void testDeleteTeacher() {
	//
	//		ITeacher teacher = null;
	//
	//		//read existing        
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex) {
	//			fail("testDeleteTeacher: readTeacherByNumber existing");
	//		}
	//		assertNotNull(teacher);
	//
	//		//erase it
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			persistentTeacher.delete(teacher);
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex2) {
	//			fail("testDeleteTeacher: delete");
	//		}
	//
	//		//read it again
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex) {
	//			fail("testDeleteTeacher: readTeacherByNumber unexisting");
	//		}
	//		assertNull(teacher);
	//
	//	}
	//
	//	public void testReadAllTeachers() {
	//		List list = null;
	//
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			list = persistentTeacher.readAll();
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia ex2) {
	//			fail("testReadAllTeachers: readAll");
	//		}
	//		assertNotNull(list);
	//	}
	//
	//	public void testReadResponsableForExecutionCoursesByTeacherNumber() {
	//		List result = null;
	//
	//		//read existing
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result =
	//				persistentTeacher.readResponsibleForExecutionCoursesByNumber(
	//					new Integer("1"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadResponsableForExecutionCoursesByTeacherNumber: readResponsableForExecutionCourses");
	//		}
	//		assertNotNull(result);
	//		assertEquals(result.size(), 3);
	//
	//		//read unexisting
	//		result = null;
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result =
	//				persistentTeacher.readResponsibleForExecutionCoursesByNumber(
	//					new Integer("222"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadResponsableForExecutionCoursesByTeacherNumber: readResponsableForExecutionCourses");
	//		}
	//		assertNotNull(result);
	//		assertEquals(result.isEmpty(), true);
	//	}
	//
	//	public void testReadProfessorShipsExecutionCoursesByTeacherNumber() {
	//		List result = null;
	//
	//		//read existing
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result =
	//				persistentTeacher.readProfessorShipsExecutionCoursesByNumber(
	//					new Integer("1"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadProfessorShipsExecutionCoursesByUsername: readProfessorShipsExecutionCourses");
	//		}
	//		assertNotNull(result);
	//		assertEquals(result.size(), 3);
	//
	//		//read unexisting
	//		result = null;
	//		try {
	//			persistentSupport.iniciarTransaccao();
	//			result =
	//				persistentTeacher.readProfessorShipsExecutionCoursesByNumber(
	//					new Integer("5"));
	//			persistentSupport.confirmarTransaccao();
	//		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
	//			fail("testReadProfessorShipsExecutionCoursesByUsername: readProfessorShipsExecutionCourses");
	//		}
	//		assertNotNull(result);
	//		assertEquals(result.isEmpty(), true);
	//	}
	public void testReadTeacherByExecutionCourseProfessorship() {
		List result = null;
		IDisciplinaExecucao executionCourse = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = null;
		//setup
		try {
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("failed in the test setup");
		}

		//read existing
		try {
			persistentSupport.iniciarTransaccao();
			result =
				persistentTeacher.readTeacherByExecutionCourseProfessorship(
					executionCourse);
			persistentSupport.confirmarTransaccao();
		
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByExecutionCourseProfessorship: ReadTeacherByExecutionCourseProfessorship");
		}
		assertNotNull(result);
		assertEquals(result.size(), 3);

		//read unexisting
		result = null;
		//		setup
		try {
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"PO",
					executionPeriod);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("failed in the test setup");
		}

		try {
			persistentSupport.iniciarTransaccao();
			result =
				persistentTeacher.readTeacherByExecutionCourseProfessorship(
					executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByExecutionCourseProfessorship: ReadTeacherByExecutionCourseProfessorship");
		}
		assertNotNull(result);
		assertEquals(result.isEmpty(), true);

	}

	public void testReadTeacherByExecutionCourseResponsibility() {
		List result = null;
		IDisciplinaExecucao executionCourse = null;
		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = null;
		//setup
		try {
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("failed in the test setup");
		}

		//read existing
		try {
			persistentSupport.iniciarTransaccao();
			result =
				persistentTeacher.readTeacherByExecutionCourseResponsibility(
					executionCourse);
			persistentSupport.confirmarTransaccao();
			Iterator iter = result.iterator();
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByExecutionCourseResponsability readTeacherByExecutionCourseResponsibility");
		}
		assertNotNull(result);
		assertEquals(result.size(), 1);

		//read unexisting
		result = null;
		//		setup
		try {
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSupport.getIDisciplinaExecucaoPersistente();
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				persistentSupport.getIPersistentExecutionPeriod();
			persistentSupport.iniciarTransaccao();
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			executionPeriod =
				persistentExecutionPeriod.readByNameAndExecutionYear(
					"2º Semestre",
					executionYear);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					"PO",
					executionPeriod);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("failed in the test setup");
		}

		try {
			persistentSupport.iniciarTransaccao();
			result =
				persistentTeacher.readTeacherByExecutionCourseResponsibility(
					executionCourse);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testreadTeacherByExecutionCourseResponsibility: readTeacherByExecutionCourseResponsibility");
		}
		assertNotNull(result);
		assertEquals(result.isEmpty(), true);
	}
}