/*
 * ReadShiftEnrolmentTest.java JUnit based test
 *
 * Created on January 13th, 2002, 17:42
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCourseExecutionAndListOfTypeLessonAndInfoShift;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.TurmaTurno;
import Dominio.Turno;
import Dominio.TurnoAluno;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.StudentCurricularPlanState;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

public class ReadOtherCoursesWithShiftsTest extends TestCaseServicos {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;
	
    public ReadOtherCoursesWithShiftsTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadOtherCoursesWithShiftsTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
	IExecutionYear executionYear = new ExecutionYear();
	executionYear.setYear("2002/03");
	IExecutionPeriod executionPeriod = new ExecutionPeriod();
	executionPeriod.setExecutionYear(executionYear);
	executionPeriod.setName("2º Semestre");
    

    IPessoa person = new Pessoa();
    person.setNome("Marvin");
    person.setUsername("lepc");
	person.setNumeroDocumentoIdentificacao("010101010101");
	person.setCodigoFiscal("010101010101");
	person.setTipoDocumentoIdentificacao(
		new TipoDocumentoIdentificacao(
			TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));	
    IStudent student = new Student(new Integer(45498), new Integer(567), person, new TipoCurso(TipoCurso.LICENCIATURA));

    IDisciplinaExecucao discipline1 =
    	new DisciplinaExecucao(
    		"Trabalho Final de Curso I",
    		"TFCI",
    		"Program1",
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		executionPeriod);
    IDisciplinaExecucao discipline2 =
    	new DisciplinaExecucao(
    		"Trabalho Final de Curso II",
    		"TFCII",
    		"Program2",
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		executionPeriod);
    IDisciplinaExecucao discipline3 =
    	new DisciplinaExecucao(
    		"Engenharia da Programação",
    		"EP",
    		"Program3",
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		executionPeriod);
    IDisciplinaExecucao discipline4 =
    	new DisciplinaExecucao(
    		"Aprendizagem",
    		"APR",
    		"Program4",
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		new Double(1),
    		executionPeriod);
	IDisciplinaExecucao discipline5 =
		new DisciplinaExecucao(
			"Programação por Objectos",
			"PO",
			"Program5",
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1),
			executionPeriod);
	IDisciplinaExecucao discipline6 =
		new DisciplinaExecucao(
			"Base de Dados",
			"BD",
			"Program6",
			new Double(1),
			new Double(1),
			new Double(1),
			new Double(1),
			executionPeriod);

    IFrequenta attend1 = new Frequenta(student, discipline1);
	IFrequenta attend2 = new Frequenta(student, discipline2);
	IFrequenta attend3 = new Frequenta(student, discipline3);
	IFrequenta attend4 = new Frequenta(student, discipline4);
	
	ITurno shift1 =
		new Turno(
			"turno_ep_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(100),
			discipline3);
	ITurno shift2 =
		new Turno(
			"turno_ep_laboratorio1",
			new TipoAula(TipoAula.LABORATORIAL),
			new Integer(50),
			discipline3);
	ITurno shift3 =
		new Turno(
			"turno_ep_laboratorio2",
			new TipoAula(TipoAula.LABORATORIAL),
			new Integer(50),
			discipline3);
	ITurno shift4 =
		new Turno(
			"turno_ep_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline3);
	ITurno shift5 =
		new Turno(
			"turno_ep_pratica2",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline4);
	ITurno shift6 =
		new Turno(
			"turno_apr_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(25),
			discipline4);
	ITurno shift7 =
		new Turno(
			"turno_apr_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(25),
			discipline4);
	ITurno shift8 =
		new Turno(
			"turno_po_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline5);
	ITurno shift9 =
		new Turno(
			"turno_po_pratica2",
			new TipoAula(TipoAula.PRATICA),
			new Integer(50),
			discipline5);
	ITurno shift10 =
		new Turno(
			"turno_bd_teorico1",
			new TipoAula(TipoAula.TEORICA),
			new Integer(25),
			discipline6);
	ITurno shift11 =
		new Turno(
			"turno_bd_pratica1",
			new TipoAula(TipoAula.PRATICA),
			new Integer(25),
			discipline6);

	ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
	ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);

	ITurmaTurno classShift1 = new TurmaTurno();
	classShift1.setTurma(_turma1);
	classShift1.setTurno(shift1);
	ITurmaTurno classShift2 = new TurmaTurno();
	classShift2.setTurma(_turma1);
	classShift2.setTurno(shift2);
	ITurmaTurno classShift3 = new TurmaTurno();
	classShift3.setTurma(_turma1);
	classShift3.setTurno(shift3);
	ITurmaTurno classShift4 = new TurmaTurno();
	classShift4.setTurma(_turma1);
	classShift4.setTurno(shift4);
	ITurmaTurno classShift5 = new TurmaTurno();
	classShift5.setTurma(_turma1);
	classShift5.setTurno(shift5);
	ITurmaTurno classShift6 = new TurmaTurno();
	classShift6.setTurma(_turma1);
	classShift6.setTurno(shift6);
	ITurmaTurno classShift7 = new TurmaTurno();
	classShift7.setTurma(_turma1);
	classShift7.setTurno(shift7);
	ITurmaTurno classShift8 = new TurmaTurno();
	classShift8.setTurma(_turma1);
	classShift8.setTurno(shift8);
	ITurmaTurno classShift9 = new TurmaTurno();
	classShift9.setTurma(_turma1);
	classShift9.setTurno(shift9);
	ITurmaTurno classShift10 = new TurmaTurno();
	classShift10.setTurma(_turma1);
	classShift10.setTurno(shift10);
	ITurmaTurno classShift11 = new TurmaTurno();
	classShift11.setTurma(_turma1);
	classShift11.setTurno(shift11);


	Calendar someDate = Calendar.getInstance();
	someDate.set(2002, Calendar.NOVEMBER, 17);

	IPlanoCurricularCurso pCC = new PlanoCurricularCurso();
	pCC.setName("nome");
	
	pCC.setCurso(_cursoExecucao1.getCurso());

	IStudentCurricularPlan sCP = new StudentCurricularPlan();
	sCP.setStudent(student);
	sCP.setStartDate(someDate.getTime());
	sCP.setCurrentState(new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
	sCP.setCourseCurricularPlan(pCC);

	try {
		_suportePersistente.iniciarTransaccao();
		_frequentaPersistente.lockWrite(attend1);
		_frequentaPersistente.lockWrite(attend2);
		_frequentaPersistente.lockWrite(attend3);
		_frequentaPersistente.lockWrite(attend4);
		_turnoPersistente.lockWrite(shift1);
		_turnoPersistente.lockWrite(shift2);
		_turnoPersistente.lockWrite(shift3);
		_turnoPersistente.lockWrite(shift4);
		_turnoPersistente.lockWrite(shift5);
		_turnoPersistente.lockWrite(shift6);
		_turnoPersistente.lockWrite(shift7);
		_turnoPersistente.lockWrite(shift8);
		_turnoPersistente.lockWrite(shift9);
		_turnoPersistente.lockWrite(shift10);
		_turnoPersistente.lockWrite(shift11);
		_turmaTurnoPersistente.lockWrite(classShift1);
		_turmaTurnoPersistente.lockWrite(classShift2);
		_turmaTurnoPersistente.lockWrite(classShift3);
		_turmaTurnoPersistente.lockWrite(classShift4);
		_turmaTurnoPersistente.lockWrite(classShift5);
		_turmaTurnoPersistente.lockWrite(classShift6);
		_turmaTurnoPersistente.lockWrite(classShift7);
		_turmaTurnoPersistente.lockWrite(classShift8);
		_turmaTurnoPersistente.lockWrite(classShift9);
		_turmaTurnoPersistente.lockWrite(classShift10);
		_turmaTurnoPersistente.lockWrite(classShift11);				
		_turnoAlunoPersistente.lockWrite(shiftStudent1);
		_turnoAlunoPersistente.lockWrite(shiftStudent2);
		_persistentStudentCurricularPlan.lockWrite(sCP);
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia excepcao) {
		fail("Exception when setUp");
	}

	infoPerson = new InfoPerson();
	infoPerson.setNome(person.getNome());
	infoPerson.setUsername(person.getUsername());
	infoStudent = new InfoStudent(student.getNumber(), student.getState(), infoPerson, new TipoCurso(TipoCurso.LICENCIATURA));

	Object argsReadShiftEnrolment[] = new Object[1];
	argsReadShiftEnrolment[0] = infoStudent;
	try {
		infoShiftEnrolment =
			(InfoShiftEnrolment) _gestor.executar(
				_userView,
				"ReadShiftEnrolment",
				argsReadShiftEnrolment);
	} catch (Exception ex) {
		fail("Exception when setup.");
	}

  }
    
  protected void tearDown() {
    //super.tearDown();
  }

  // read shift enrolment by unauthorized user
  public void testUnauthorizedReadOtherCoursesWithShiftsTest() {
    Object argsReadOtherCoursesWithShiftsTest[] = new Object[1];
	argsReadOtherCoursesWithShiftsTest[0] = infoStudent;
    //argsReadOtherCoursesWithShiftsTest[1] = infoShiftEnrolment;

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ReadOtherCoursesWithShifts", argsReadOtherCoursesWithShiftsTest);
        fail("testUnauthorizedReadOtherCoursesWithShiftsTest");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadOtherCoursesWithShiftsTest", result);
      }
  }

  // read shift enrolment by authorized user
  public void testAuthorizedReadOtherCoursesWithShiftsTest() {
	Object argsReadOtherCoursesWithShiftsTest[] = new Object[1];
	argsReadOtherCoursesWithShiftsTest[0] = infoStudent;
	//argsReadOtherCoursesWithShiftsTest[1] = infoShiftEnrolment;
    
	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadOtherCoursesWithShifts", argsReadOtherCoursesWithShiftsTest);
		assertNotNull("testAuthorizedReadOtherCoursesWithShiftsTest: result is null", result);
		List iCWS = (List) result;
		assertTrue("testAuthorizedReadOtherCoursesWithShiftsTest: result is empty", !(iCWS.isEmpty()));
		assertEquals(
			"testAuthorizedReadOtherCoursesWithShiftsTest: result contains an unexpected number of objects",
			3,
			iCWS.size());
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift pair1 =
			(InfoCourseExecutionAndListOfTypeLessonAndInfoShift) iCWS
				.get(1);
//				.get(0);
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift pair2 =
			(InfoCourseExecutionAndListOfTypeLessonAndInfoShift) iCWS
				.get(0);
//				.get(1);				
		InfoCourseExecutionAndListOfTypeLessonAndInfoShift pair3 =
			(InfoCourseExecutionAndListOfTypeLessonAndInfoShift) iCWS
				.get(2);
				
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest", "Base de Dados",
			         ((InfoExecutionCourse) pair1.getInfoExecutionCourse()).getNome());
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest", "Programação por Objectos",
					 ((InfoExecutionCourse) pair2.getInfoExecutionCourse()).getNome());
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest", "Trabalho Final Curso",
					 ((InfoExecutionCourse) pair3.getInfoExecutionCourse()).getNome());
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest: size pair 1", 2,
					 ((List) pair1.getTypeLessonsAndInfoShifts()).size());
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest: size pair 2", 1,
					 ((List) pair2.getTypeLessonsAndInfoShifts()).size());
		assertEquals("testAuthorizedReadOtherCoursesWithShiftsTest: size pair 3", 1,
					 ((List) pair3.getTypeLessonsAndInfoShifts()).size());
	  } catch (Exception ex) {
	  	ex.printStackTrace(System.out);
		fail("testAuthorizedReadOtherCoursesWithShiftsTest: unexpected exception : " + ex);
	  }
  }

}
