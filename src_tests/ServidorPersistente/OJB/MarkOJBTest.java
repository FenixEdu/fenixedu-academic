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
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISala;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISalaPersistente;
import Util.TipoCurso;

public class MarkOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentStudent persistentStudent = null;
	IPersistentExam persistentExam = null;
	IFrequentaPersistente persistentAttend = null;
	IPersistentMark persistentMark = null;
	IPersistentExecutionPeriod persistentExecutionPeriod = null;
	ISalaPersistente persistentSala = null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse = null;

	public MarkOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
		
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MarkOJBTest.class);
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
		persistentExam = persistentSupport.getIPersistentExam();
		persistentAttend = persistentSupport.getIFrequentaPersistente();
		persistentSala = persistentSupport.getISalaPersistente();
		persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		persistentMark = persistentSupport.getIPersistentMark();
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();

	}

	protected void tearDown() {
		super.tearDown();
	}

	/** Test of readByExam method, of class ServidorPersistente.OJB.MarkOJB. */
	public void testreadByExam() {
		System.out.println("test-1");
		IFrequenta attend = null;
		IStudent student = null;
		IExam exam = null;
		IMark mark = null;
		ISala sala = null;
		IExecutionPeriod executionPeriod = null;

		try {
			persistentSupport.iniciarTransaccao();
			sala = persistentSala.readByName("Ga3");
			assertNotNull(persistentSala);
			executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
			assertNotNull(executionPeriod);	
			List examList = (List) persistentExam.readBy(sala, executionPeriod);
			assertTrue(examList.size()== 1 );
			List markList =  persistentMark.readBy(((IExam) examList.get(0)));
			assertTrue(markList.size()== 2 );			
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByExam:fail read non existing mark");
		}

		// read unexisting Mark
		try {
			persistentSupport.iniciarTransaccao();
			sala = persistentSala.readByName("Ga1");
			assertNotNull(persistentSala);
			executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
			assertNotNull(executionPeriod);	
			List examList = (List) persistentExam.readBy(sala, executionPeriod);
			assertTrue(examList.size()== 1 );
			List markList =  persistentMark.readBy(((IExam) examList.get(0)));
			assertTrue(markList.size()== 0 );			
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByExam:fail read existing Mark");
		}
	}	
		
	/** Test of readByExamAttend method, of class ServidorPersistente.OJB.MarkOJB. */
	public void testreadByExamAttend() {
		System.out.println("test-2");
		IFrequenta attend = null;
		IStudent student = null;
		IExam exam = null;
		IMark mark = null;
		ISala sala = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		
		try {
		//	read existing Exam
			persistentSupport.iniciarTransaccao();
			sala = persistentSala.readByName("Ga3");
			assertNotNull(persistentSala);
			executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
			assertNotNull(executionPeriod);	
			List examList = (List) persistentExam.readBy(sala, executionPeriod);
			assertTrue(examList.size()== 1 );
			// read an existing Attend
			student =persistentStudent.readByNumero(
							new Integer(600),
							new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(student);
			// read execution course
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCII",
					"2002/2003",
					"MEEC");
			assertNotNull(executionCourse);
			attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,executionCourse);
			assertNotNull(attend);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
		  fail("readByExamAttend:fail read non existing mark");
		}
	
	}
//
//	// write new existing frequenta
//	public void testCreateExistingFrequenta() {
//		IStudent student = null;
//		IDisciplinaExecucao executionCourse = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			student =
//				persistentStudent.readByNumero(
//					new Integer(800),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			assertNotNull(student);
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//			persistentSupport.confirmarTransaccao();
//
//			IFrequenta attend = new Frequenta(student, executionCourse);
//
//			persistentSupport.iniciarTransaccao();
//			persistentAttend.lockWrite(attend);
//			persistentSupport.confirmarTransaccao();
//			fail("testCreateExistingFrequenta: Expected an Exception");
//		} catch (ExistingPersistentException ex) {
//			//all is ok
//			try {
//				persistentSupport.confirmarTransaccao();
//			} catch (ExcepcaoPersistencia e) {
//				fail("testCreateExistingFrequenta: Unable to confirm transaction");
//			}
//			assertNotNull("testCreateExistingFrequenta", ex);
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testCreateExistingFrequenta: Unexpected Exception");
//		}
//	}
//
//	//  // write new non-existing frequenta
//	public void testCreateNonExistingFrequenta() {
//		IFrequenta attend = new Frequenta();
//		IStudent student = null;
//		IDisciplinaExecucao executionCourse = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//
//			student =
//				persistentStudent.readByNumero(
//					new Integer(800),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			assertNotNull(student);
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCII",
//					"2002/2003",
//					"MEEC");
//			assertNotNull(executionCourse);
//
//			persistentSupport.confirmarTransaccao();
//
//			attend.setAluno(student);
//			attend.setDisciplinaExecucao(executionCourse);
//
//			persistentSupport.iniciarTransaccao();
//			persistentAttend.lockWrite(attend);
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testCreateNonExistingFrequenta");
//		}
//	}
//
//	/** Test of write method, of class ServidorPersistente.OJB.FrequentaOJB. */
//	public void testWriteExistingChangedObject() {
//		IFrequenta attend = null;
//		IStudent student = null;
//		IDisciplinaExecucao executionCourse = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			student =
//				persistentStudent.readByNumero(
//					new Integer(800),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			assertNotNull(student);
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//
//			attend =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse);
//			assertNotNull(attend);
//
//			persistentSupport.confirmarTransaccao();
//
//			assertEquals(attend.getAluno(), student);
//			assertEquals(attend.getDisciplinaExecucao(), executionCourse);
//
//			persistentSupport.iniciarTransaccao();
//			attend =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse);
//			assertNotNull(attend);
//
//			IDisciplinaExecucao executionCourse1 = null;
//			executionCourse1 =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCII",
//					"2002/2003",
//					"MEEC");
//			assertNotNull(executionCourse1);
//
//			persistentExecutionCourse.readByOId(attend, true);
//			attend.setDisciplinaExecucao(executionCourse1);
//			persistentSupport.confirmarTransaccao();
//
//			// Check changes
//
//			persistentSupport.iniciarTransaccao();
//			IFrequenta attendTemp = null;
//			attendTemp =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse1);
//			assertNotNull(attendTemp);
//			assertEquals(attendTemp.getAluno(), student);
//			assertEquals(attendTemp.getDisciplinaExecucao(), executionCourse1);
//
//			persistentSupport.confirmarTransaccao();
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testReadByAlunoAndDisciplinaExecucao:fail read existing frequenta");
//		}
//	}
//
//	/** Test of delete method, of class ServidorPersistente.OJB.FrequentaOJB. */
//	public void testDeleteFrequenta() {
//
//		IStudent student = null;
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			student =
//				persistentStudent.readByNumero(
//					new Integer(800),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			IDisciplinaExecucao executionCourse = null;
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//
//			IFrequenta attend =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse);
//			assertNotNull(attend);
//
//			persistentAttend.delete(attend);
//			persistentSupport.confirmarTransaccao();
//
//			persistentSupport.iniciarTransaccao();
//			IFrequenta attendTemp =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse);
//			persistentSupport.confirmarTransaccao();
//
//			assertNull(attendTemp);
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testDeleteFrequenta");
//		}
//	}
//
//	/** Test of deleteAll method, of class ServidorPersistente.OJB.FrequentaOJB. */
//	public void testDeleteAll() {
//		IStudent student = null;
//
//		try {
//
//			// Check that something exists
//			persistentSupport.iniciarTransaccao();
//			student =
//				persistentStudent.readByNumero(
//					new Integer(800),
//					new TipoCurso(TipoCurso.LICENCIATURA));
//			IDisciplinaExecucao executionCourse = null;
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//
//			IFrequenta attend =
//				persistentAttend.readByAlunoAndDisciplinaExecucao(
//					student,
//					executionCourse);
//			assertNotNull(attend);
//			persistentSupport.confirmarTransaccao();
//
//			// Delete All
//			// TODO : Figure out why test fails.
//			//        Not critical because this operation should never
//			//        be performed in any real-life situation
//			//      persistentSupport.iniciarTransaccao();
//			//      persistentAttend.deleteAll();
//			//      persistentSupport.confirmarTransaccao();
//
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testDeleteAllFrequenta");
//		}
//
//	}
//
//	public void testReadByExecutionCourse() {
//
//		try {
//			persistentSupport.iniciarTransaccao();
//
//			// Check one that exists  
//			IDisciplinaExecucao executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//
//			List attendList =
//				persistentAttend.readByExecutionCourse(executionCourse);
//			assertNotNull(attendList);
//			assertEquals(attendList.size(), 1);
//
//			// Check one that doesn't exist  
//
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"PO",
//					"2002/2003",
//					"MEEC");
//			assertNotNull(executionCourse);
//
//			attendList = null;
//			attendList =
//				persistentAttend.readByExecutionCourse(executionCourse);
//			assertNotNull(attendList);
//			assertEquals(attendList.size(), 0);
//
//			persistentSupport.confirmarTransaccao();
//
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testDeleteFrequenta");
//		}
//	}
//
//	public void testCountStudentsAttendingExecutionCourse() {
//
//		try {
//			persistentSupport.iniciarTransaccao();
//			// Check one that exists
//			IDisciplinaExecucao executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"TFCI",
//					"2002/2003",
//					"LEIC");
//			assertNotNull(executionCourse);
//
//			Integer result = persistentAttend.countStudentsAttendingExecutionCourse(executionCourse);
//			assertEquals(new Integer(1), result);
//
//			// Check one that doesn't exist  
//			executionCourse =
//				persistentExecutionCourse
//					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
//					"PO",
//					"2002/2003",
//					"MEEC");
//			assertNotNull(executionCourse);
//
//			result = persistentAttend.countStudentsAttendingExecutionCourse(executionCourse);
//			assertEquals(new Integer(0), result);
//
//			persistentSupport.confirmarTransaccao();
//
//		} catch (ExcepcaoPersistencia ex) {
//			fail("testDeleteFrequenta");
//		}
//	}

}
