/*
 * ReadShiftLessonsTest.java JUnit based test
 *
 * Created on January 4th, 2003, 23:35
 */

package ServidorAplicacao.Servicos.student;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import Dominio.Aula;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ITurnoAula;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.Turno;
import Dominio.TurnoAluno;
import Dominio.TurnoAula;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

public class ReadShiftLessonsTest extends TestCaseServicos {

	private InfoPerson infoPerson = null;
	private InfoStudent infoStudent = null;
	
    public ReadShiftLessonsTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(ReadShiftLessonsTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
    
    IPessoa person = new Pessoa();
    person.setNome("Marvin");
    person.setUsername("lepc");
	person.setNumeroDocumentoIdentificacao("010101010101");
	person.setCodigoFiscal("010101010101");
	person.setTipoDocumentoIdentificacao(
		new TipoDocumentoIdentificacao(
			TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));	
    IStudent student = new Student(new Integer(45498), new Integer(567), person, new TipoCurso(TipoCurso.LICENCIATURA));

	IExecutionYear executionYear = new ExecutionYear();
	executionYear.setYear("2002/2003");
	IExecutionPeriod executionPeriod = new ExecutionPeriod();
	executionPeriod.setExecutionYear(executionYear);
	executionPeriod.setName("2º Semestre");


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
			"Engenharia da Programacao",
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

	ITurnoAluno shiftStudent1 = new TurnoAluno(shift6, student);
	ITurnoAluno shiftStudent2 = new TurnoAluno(shift7, student);

	IAula lesson1 =
		new Aula(
			new DiaSemana(DiaSemana.QUARTA_FEIRA),
			_inicio,
			_fim,
			_tipoAula,
			_sala1,
			discipline4);
	IAula lesson2 =
		new Aula(
			new DiaSemana(DiaSemana.QUINTA_FEIRA),
			_inicio,
			_fim,
			new TipoAula(TipoAula.PRATICA),
			_sala1,
			discipline4);

	ITurnoAula shiftLesson1 = new TurnoAula(shift6, lesson1);
	ITurnoAula shiftLesson2 = new TurnoAula(shift7, lesson2);

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
		_turnoAlunoPersistente.lockWrite(shiftStudent1);
		_turnoAlunoPersistente.lockWrite(shiftStudent2);
		_turnoAulaPersistente.lockWrite(shiftLesson1);
		_turnoAulaPersistente.lockWrite(shiftLesson2);
		_suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia excepcao) {
		fail("Exception when setUp");
	}

	infoPerson = new InfoPerson();
	infoPerson.setNome(person.getNome());
	infoPerson.setUsername(person.getUsername());
	infoStudent = new InfoStudent(student.getNumber(), student.getState(), infoPerson, new TipoCurso(TipoCurso.LICENCIATURA));	
  }
    
  protected void tearDown() {
    //super.tearDown();
  }

  // read shift lessons by unauthorized user
  public void testUnauthorizedReadShiftLessons() {
    Object argsReadShiftLessons[] = new Object[1];
    argsReadShiftLessons[0] = new InfoShift("turno_apr_teorico1", null, null, null);

    Object result = null; 
      try {
        result = _gestor.executar(_userView2, "ReadShiftLessons", argsReadShiftLessons);
        fail("testUnauthorizedReadShiftLessons");
      } catch (Exception ex) {
        assertNull("testUnauthorizedReadShiftLessons", result);
      }
  }

  // read shift lessons by authorized user
  public void testAuthorizedReadShiftLessons() {
	Object argsReadShiftLessons[] = new Object[1];
	argsReadShiftLessons[0] = new InfoShift("turno_apr_teorico1", null, null, null);
    
	Object result = null; 
	  try {
		result = _gestor.executar(_userView, "ReadShiftLessons", argsReadShiftLessons);
		assertNotNull("testAuthorizedReadShiftLessons", result);
		assertEquals("testAuthorizedReadShiftLessons", ((List)result).size(), 1);
		List infoLessons = (List) result;
	  } catch (Exception ex) {
		fail("testAuthorizedReadShiftLessons" + ex);
	  }
  }

}
