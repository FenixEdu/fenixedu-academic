/*
 * ReadShiftEnrolmentTest.java JUnit based test
 *
 * Created on December 21st, 2002, 21:27
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCourseExecutionAndListOfTypeLessonAndInfoShift;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class ReadShiftEnrolmentTest extends TestCaseServicos {

	private InfoStudent infoStudent = null;
	
    public ReadShiftEnrolmentTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadShiftEnrolmentTest.class);
        
    return suite;
  }
    
    
    
  // FIXME : The service needs lots of corrections  
  protected void setUp() {
//    super.setUp();
//    
//    IPessoa person = new Pessoa();
//    person.setNome("Marvin");
//    person.setUsername("lepc");
//	person.setNumeroDocumentoIdentificacao("010101010101");
//	person.setCodigoFiscal("010101010101");
//	person.setTipoDocumentoIdentificacao(
//		new TipoDocumentoIdentificacao(
//			TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));	
//    IStudent student = new Student(new Integer(45498), new Integer(567), person, new TipoCurso(TipoCurso.LICENCIATURA));
//
//	IExecutionYear executionYear = new ExecutionYear();
//	executionYear.setYear("2002/03");
//	IExecutionPeriod executionPeriod = new ExecutionPeriod();
//	executionPeriod.setExecutionYear(executionYear);
//	executionPeriod.setName("2º Semestre");
//
//
//    IDisciplinaExecucao discipline1 =
//    	new DisciplinaExecucao(
//    		"Trabalho Final de Curso I",
//    		"TFCI",
//    		"Program1",
//
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		executionPeriod);
//    IDisciplinaExecucao discipline2 =
//    	new DisciplinaExecucao(
//    		"Trabalho Final de Curso II",
//    		"TFCII",
//    		"Program2",
//
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		executionPeriod);
//    IDisciplinaExecucao discipline3 =
//    	new DisciplinaExecucao(
//    		"Engenharia da Programacao",
//    		"EP",
//    		"Program3",
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		executionPeriod);
//    IDisciplinaExecucao discipline4 =
//    	new DisciplinaExecucao(
//    		"Aprendizagem",
//    		"APR",
//    		"Program4",
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		new Double(1),
//    		executionPeriod);
//
//    IFrequenta attend1 = new Frequenta(student, discipline1);
//	IFrequenta attend2 = new Frequenta(student, discipline2);
//	IFrequenta attend3 = new Frequenta(student, discipline3);
//	IFrequenta attend4 = new Frequenta(student, discipline4);
//	
//	ITurno shift1 =
//		new Turno(
//			"turno_ep_teorico1",
//			new TipoAula(TipoAula.TEORICA),
//			new Integer(100),
//			discipline3);
//	ITurno shift2 =
//		new Turno(
//			"turno_ep_laboratorio1",
//			new TipoAula(TipoAula.LABORATORIAL),
//			new Integer(50),
//			discipline3);
//	ITurno shift3 =
//		new Turno(
//			"turno_ep_laboratorio2",
//			new TipoAula(TipoAula.LABORATORIAL),
//			new Integer(50),
//			discipline3);
//	ITurno shift4 =
//		new Turno(
//			"turno_ep_pratica1",
//			new TipoAula(TipoAula.PRATICA),
//			new Integer(50),
//			discipline3);
//	ITurno shift5 =
//		new Turno(
//			"turno_ep_pratica2",
//			new TipoAula(TipoAula.PRATICA),
//			new Integer(50),
//			discipline4);
//	ITurno shift6 =
//		new Turno(
//			"turno_apr_teorico1",
//			new TipoAula(TipoAula.TEORICA),
//			new Integer(25),
//			discipline4);
//	ITurno shift7 =
//		new Turno(
//			"turno_apr_pratica1",
//			new TipoAula(TipoAula.PRATICA),
//			new Integer(25),
//			discipline4);
//
//	ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
//	ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);
//
//	ITurmaTurno classShift1 = new TurmaTurno();
//	classShift1.setTurma(_turma1);
//	classShift1.setTurno(shift1);
//	ITurmaTurno classShift2 = new TurmaTurno();
//	classShift2.setTurma(_turma1);
//	classShift2.setTurno(shift2);
//	ITurmaTurno classShift3 = new TurmaTurno();
//	classShift3.setTurma(_turma1);
//	classShift3.setTurno(shift3);
//	ITurmaTurno classShift4 = new TurmaTurno();
//	classShift4.setTurma(_turma1);
//	classShift4.setTurno(shift4);
//	ITurmaTurno classShift5 = new TurmaTurno();
//	classShift5.setTurma(_turma1);
//	classShift5.setTurno(shift5);
//	ITurmaTurno classShift6 = new TurmaTurno();
//	classShift6.setTurma(_turma1);
//	classShift6.setTurno(shift6);
//	ITurmaTurno classShift7 = new TurmaTurno();
//	classShift7.setTurma(_turma1);
//	classShift7.setTurno(shift7);
//	
//	try {
//		_suportePersistente.iniciarTransaccao();
//		_frequentaPersistente.lockWrite(attend1);
//		_frequentaPersistente.lockWrite(attend2);
//		_frequentaPersistente.lockWrite(attend3);
//		_frequentaPersistente.lockWrite(attend4);
//		_turnoPersistente.lockWrite(shift1);
//		_turnoPersistente.lockWrite(shift2);
//		_turnoPersistente.lockWrite(shift3);
//		_turnoPersistente.lockWrite(shift4);
//		_turnoPersistente.lockWrite(shift5);
//		_turnoAlunoPersistente.lockWrite(shiftStudent1);
//		_turnoAlunoPersistente.lockWrite(shiftStudent2);
//		_turmaTurnoPersistente.lockWrite(classShift1);
//		_turmaTurnoPersistente.lockWrite(classShift2);
//		_turmaTurnoPersistente.lockWrite(classShift3);
//		_turmaTurnoPersistente.lockWrite(classShift4);
//		_turmaTurnoPersistente.lockWrite(classShift5);
//		_turmaTurnoPersistente.lockWrite(classShift6);
//		_turmaTurnoPersistente.lockWrite(classShift7);
//		_suportePersistente.confirmarTransaccao();
//	} catch (ExcepcaoPersistencia excepcao) {
//		fail("Exception when setUp");
//	}
//
//	infoPerson = new InfoPerson();
//	infoPerson.setNome(person.getNome());
//	infoPerson.setUsername(person.getUsername());
//	infoStudent = new InfoStudent(student.getNumber(), student.getState(), infoPerson, new TipoCurso(TipoCurso.LICENCIATURA));	
  }
    
  protected void tearDown() {
    //super.tearDown();
  }

  // read shift enrolment by unauthorized user
  public void testUnauthorizedReadShiftEnrolment() {
    Object argsReadShiftEnrolment[] = new Object[1];
    argsReadShiftEnrolment[0] = infoStudent;

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ReadShiftEnrolment", argsReadShiftEnrolment);
        fail("testUnauthorizedReadShiftEnrolment");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadShiftEnrolment", result);
      }
  }

  // read shift enrolment by authorized user
  public void testAuthorizedReadShiftEnrolment() {
	Object argsReadShiftEnrolment[] = new Object[1];
	argsReadShiftEnrolment[0] = infoStudent;
    
	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadShiftEnrolment", argsReadShiftEnrolment);
		assertNotNull("testAuthorizedReadShiftEnrolment", result);
		InfoShiftEnrolment iSE = (InfoShiftEnrolment) result;
		//assertNotNull("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithOutShift());
		assertNotNull("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithShift());
		//assertTrue("testAuthorizedReadShiftEnrolment", !(iSE.getInfoEnrolmentWithOutShift().isEmpty()));
		assertTrue("testAuthorizedReadShiftEnrolment", !(iSE.getInfoEnrolmentWithShift().isEmpty()));
		//assertEquals("testAuthorizedReadShiftEnrolment", 2, iSE.getInfoEnrolmentWithOutShift().size());
		assertEquals("testAuthorizedReadShiftEnrolment", 2, iSE.getInfoEnrolmentWithShift().size());
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift pair1 =
			(InfoCourseExecutionAndListOfTypeLessonAndInfoShift) iSE
				.getInfoEnrolmentWithShift()
				.get(0);
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift pair2 =
			(InfoCourseExecutionAndListOfTypeLessonAndInfoShift) iSE
				.getInfoEnrolmentWithShift()
				.get(1);
		assertEquals("testAuthorizedReadShiftEnrolment", "Engenharia da Programacao",
			         ((InfoExecutionCourse) pair1.getInfoExecutionCourse()).getNome());
		assertEquals("testAuthorizedReadShiftEnrolment", "Aprendizagem",
					 ((InfoExecutionCourse) pair2.getInfoExecutionCourse()).getNome());
		assertEquals("testAuthorizedReadShiftEnrolment", 3,
					 ((List) pair1.getTypeLessonsAndInfoShifts()).size());
		assertEquals("testAuthorizedReadShiftEnrolment", 2,
					 ((List) pair2.getTypeLessonsAndInfoShifts()).size());
	  } catch (Exception ex) {
		fail("testAuthorizedReadShiftEnrolment");
	  }
  }

  // read shift enrolment of student with no enrolments by authorized user
  public void testAuthorizedReadShiftEnrolmentNoFrequemcies() {
//  Object argsReadShiftEnrolment[] = new Object[1];
//  InfoPerson infoPerson = new InfoPerson();
//  infoPerson.setNome(_pessoa1.getNome());
//  infoPerson.setUsername(_pessoa1.getUsername());
//  argsReadShiftEnrolment[0] = new InfoStudent(_aluno1.getNumber(), _aluno1.getState(), infoPerson, new TipoCurso(TipoCurso.LICENCIATURA));
//
//  Object result = null; 
//	try {
//	  result = _gestor.executar(_userView, "ReadShiftEnrolment", argsReadShiftEnrolment);
//	  assertNotNull("testAuthorizedReadShiftEnrolment", result);
//	  InfoShiftEnrolment iSE = (InfoShiftEnrolment) result;
//	  assertNotNull("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithOutShift());
//	  assertNotNull("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithShift());
//	  assertTrue("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithOutShift().isEmpty());
//	  assertTrue("testAuthorizedReadShiftEnrolment", iSE.getInfoEnrolmentWithShift().isEmpty());
//	} catch (Exception ex) {
//	  fail("testAuthorizedReadShiftEnrolment");
//	}
  }

}
