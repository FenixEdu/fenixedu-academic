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
import Dominio.Mark;
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
			mark =  persistentMark.readBy(((IExam) examList.get(0)),attend);
			assertNotNull(mark);					
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
		  fail("readByExamAttend:fail read non existing mark");
		}
	
	}
	

	/** Test of delete method, of class ServidorPersistente.OJB.MarkOJB. */
	public void testDeleteMark() {
		System.out.println("test-3");
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
			mark =  persistentMark.readBy(((IExam) examList.get(0)),attend);
			assertNotNull(mark);	
			persistentMark.delete(mark);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testDeleteFrequenta");
		}
	}

	/** Test of Write method, of class ServidorPersistente.OJB.MarkOJB. */
	public void testLockWrite() {
		System.out.println("test-4");
		IFrequenta attend = null;
		IStudent student = null;
		IExam exam = null;
		IMark mark = null;
		ISala sala = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		List examList = null;
		try{
		
		persistentSupport.iniciarTransaccao();
		sala = persistentSala.readByName("Ga3");
		assertNotNull(persistentSala);
		executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
		assertNotNull(executionPeriod);	
		examList = (List) persistentExam.readBy(sala, executionPeriod);
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
		}catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testLockWrite: write non Existing");
		}
		mark =  new Mark(new Integer(0));
		mark.setAttend(attend);
		mark.setExam((IExam)examList.get(0));
		mark.setMark("16");
		mark.setPublishedMark("");
		try {
			persistentSupport.iniciarTransaccao();
			persistentMark.lockWrite(mark);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testLockWrite: write an invalid mark");
		}

		IMark markRead = null;

		try {
			persistentSupport.iniciarTransaccao();
			markRead = persistentMark.readBy((IExam) examList.get(0), attend);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testEscreverNota: unexpected exception reading");
		}
		assertNotNull(markRead);
		assertEquals(markRead.getMark(),mark.getMark());
		//assertEquals(markRead.getPublishedMark(),mark.getPublishedMark());
		assertEquals(markRead.getAttend(),mark.getAttend());
		assertEquals(markRead.getExam(),mark.getExam());
		
	
		}


}
