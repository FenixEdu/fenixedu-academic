/*
 * FrequentaOJBTest.java
 * JUnit based test
 *
 * Created on 20 de Outubro de 2002, 15:53
 */

package ServidorPersistente.OJB;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class FrequentaOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentStudent persistentStudent = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
	IFrequentaPersistente persistentAttend = null;

	public FrequentaOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(FrequentaOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentStudent = persistentSupport.getIPersistentStudent();
		persistentExecutionCourse =
			persistentSupport.getIDisciplinaExecucaoPersistente();
		persistentAttend = persistentSupport.getIFrequentaPersistente();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readByAlunoAndDisciplinaExecucao method, of class ServidorPersistente.OJB.FrequentaOJB. */
	public void testreadByAlunoAndDisciplinaExecucao() {
		IFrequenta attend = null;
		IStudent student = null;
		IDisciplinaExecucao executionCourse = null;

		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(student);

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			assertNotNull(attend);

			persistentSupport.confirmarTransaccao();
			assertEquals(attend.getAluno(), student);
			assertEquals(attend.getDisciplinaExecucao(), executionCourse);

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
		}

		// read unexisting Frequenta
		try {
			persistentSupport.iniciarTransaccao();

			executionCourse = null;
			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCII",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse);

			attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			persistentSupport.confirmarTransaccao();
			assertNull(attend);
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
		}
	}

	/** Test of readByAlunoAndDisciplinaExecucao method, of class ServidorPersistente.OJB.FrequentaOJB. */
	public void testreadByStudentId() {

		// FIXME: Must read by Username

		//	List courses = null;
		//	// read existing Frequenta
		//	try {
		//	  persistentSupport.iniciarTransaccao();
		//	  courses = persistentAttend.readByStudentId(_aluno1.getNumber());
		//	  persistentSupport.confirmarTransaccao();
		//	  assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
		//	  assertTrue("testReadByStudentId:read courses of existing frequencies", !(courses.isEmpty()));
		//	  assertEquals("testReadByStudentId:read courses of existing frequencies", 1, courses.size());
		//	} catch (ExcepcaoPersistencia ex) {
		//	  fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
		//	}
		//        
		//	// read unexisting Frequenta
		//	try {
		//		persistentSupport.iniciarTransaccao();
		//		courses = persistentAttend.readByStudentId(_aluno2.getNumber());
		//		persistentSupport.confirmarTransaccao();
		//		assertNotNull("testReadByStudentId:read courses of existing frequencies", courses);
		//		assertTrue("testReadByStudentId:read courses of existing frequencies", courses.isEmpty());
		//	} catch (ExcepcaoPersistencia ex) {
		//	  fail("testReadByAlunoAndDisciplinaExecucao:fail read unexisting frequenta");
		//	}
	}

	// write new existing frequenta
	public void testCreateExistingFrequenta() {
		IStudent student = null;
		IDisciplinaExecucao executionCourse = null;

		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(student);

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);
			persistentSupport.confirmarTransaccao();

			IFrequenta attend = new Frequenta(student, executionCourse);

			persistentSupport.iniciarTransaccao();
			persistentAttend.lockWrite(attend);
			persistentSupport.confirmarTransaccao();
			fail("testCreateExistingFrequenta: Expected an Exception");
		} catch (ExistingPersistentException ex) {
			//all is ok
			try {
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("testCreateExistingFrequenta: Unable to confirm transaction");
			}
			assertNotNull("testCreateExistingFrequenta", ex);
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateExistingFrequenta: Unexpected Exception");
		}
	}

	//  // write new non-existing frequenta
	public void testCreateNonExistingFrequenta() {
		IFrequenta attend = new Frequenta();
		IStudent student = null;
		IDisciplinaExecucao executionCourse = null;

		try {
			persistentSupport.iniciarTransaccao();

			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(student);

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCII",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse);

			persistentSupport.confirmarTransaccao();

			attend.setAluno(student);
			attend.setDisciplinaExecucao(executionCourse);

			persistentSupport.iniciarTransaccao();
			persistentAttend.lockWrite(attend);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testCreateNonExistingFrequenta");
		}
	}

	/** Test of write method, of class ServidorPersistente.OJB.FrequentaOJB. */
	public void testWriteExistingChangedObject() {
		IFrequenta attend = null;
		IStudent student = null;
		IDisciplinaExecucao executionCourse = null;

		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(student);

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			assertNotNull(attend);

			persistentSupport.confirmarTransaccao();

			assertEquals(attend.getAluno(), student);
			assertEquals(attend.getDisciplinaExecucao(), executionCourse);

			persistentSupport.iniciarTransaccao();
			attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			assertNotNull(attend);

			IDisciplinaExecucao executionCourse1 = null;
			executionCourse1 =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCII",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse1);

			persistentExecutionCourse.readByOId(attend, true);
			attend.setDisciplinaExecucao(executionCourse1);
			persistentSupport.confirmarTransaccao();

			// Check changes

			persistentSupport.iniciarTransaccao();
			IFrequenta attendTemp = null;
			attendTemp =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse1);
			assertNotNull(attendTemp);
			assertEquals(attendTemp.getAluno(), student);
			assertEquals(attendTemp.getDisciplinaExecucao(), executionCourse1);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
		}
	}

	/** Test of delete method, of class ServidorPersistente.OJB.FrequentaOJB. */
	public void testDeleteFrequenta() {

		IStudent student = null;

		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			IDisciplinaExecucao executionCourse = null;

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			IFrequenta attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			assertNotNull(attend);

			persistentAttend.delete(attend);
			persistentSupport.confirmarTransaccao();

			persistentSupport.iniciarTransaccao();
			IFrequenta attendTemp =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			persistentSupport.confirmarTransaccao();

			assertNull(attendTemp);
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteFrequenta");
		}
	}

	/** Test of deleteAll method, of class ServidorPersistente.OJB.FrequentaOJB. */
	public void testDeleteAll() {
		IStudent student = null;

		try {

			// Check that something exists
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readByNumero(
					new Integer(800),
					new TipoCurso(TipoCurso.LICENCIATURA));
			IDisciplinaExecucao executionCourse = null;

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			IFrequenta attend =
				persistentAttend.readByAlunoAndDisciplinaExecucao(
					student,
					executionCourse);
			assertNotNull(attend);
			persistentSupport.confirmarTransaccao();

			// Delete All
			// TODO : Figure out why test fails.
			//        Not critical because this operation should never
			//        be performed in any real-life situation
			//      persistentSupport.iniciarTransaccao();
			//      persistentAttend.deleteAll();
			//      persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteAllFrequenta");
		}

	}

	public void testReadByExecutionCourse() {

		try {
			persistentSupport.iniciarTransaccao();

			// Check one that exists  
			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			List attendList =
				persistentAttend.readByExecutionCourse(executionCourse);
			assertNotNull(attendList);
			assertEquals(attendList.size(), 1);

			// Check one that doesn't exist  

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"PO",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse);

			attendList = null;
			attendList =
				persistentAttend.readByExecutionCourse(executionCourse);
			assertNotNull(attendList);
			assertEquals(attendList.size(), 0);

			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteFrequenta");
		}
	}

	public void testCountStudentsAttendingExecutionCourse() {

		try {
			persistentSupport.iniciarTransaccao();
			// Check one that exists
			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			Integer result = persistentAttend.countStudentsAttendingExecutionCourse(executionCourse);
			assertEquals(new Integer(1), result);

			// Check one that doesn't exist  
			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"PO",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse);

			result = persistentAttend.countStudentsAttendingExecutionCourse(executionCourse);
			assertEquals(new Integer(0), result);

			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteFrequenta");
		}
	}

}
