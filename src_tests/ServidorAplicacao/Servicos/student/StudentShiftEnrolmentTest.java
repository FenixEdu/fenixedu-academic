/**
 * Project Sop 
 * 
 * Package ServidorAplicacao.Servicos.student
 * 
 * Created on 18/Dez/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoEnrolmentServiceResult;
import DataBeans.InfoPerson;
import DataBeans.InfoShift;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import Dominio.Frequenta;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoAula;
import Util.TipoCurso;

/**
 * _pessoa1 is a User with permissions to access service StudentShiftEnrolment
 * _UserView1 is userView from _pessoa1<br/>  
 * _aluno1    is  a Student that is enroled in _disciplina1 and is enroled in
 * 			_disciplinaExecucao1	   _turno1 shift
 * 
 *  _disciplinaExecucao1's shift list:
 * 			- _turno1 and _turno2 - type = Teorica
 *          - _shift3 - type = Pratica
 * @author jpvl
 */
public class StudentShiftEnrolmentTest extends TestCaseServicos {
	private InfoShiftEnrolment studentShiftEnrolment;

	private final String serviceName = "StudentShiftEnrolment";

	protected ITurno _shift3; /* max ocupation = 1 */
	
	private IStudent _student2;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() {
		super.setUp();

		try {
			_suportePersistente.iniciarTransaccao();
			
			
			_student2 = new Student(new Integer("41329"),new Integer(1), _pessoa2, new TipoCurso(TipoCurso.LICENCIATURA));
			_alunoPersistente.lockWrite(_student2);
			
			IFrequenta assist = new Frequenta(_aluno1, _disciplinaExecucao1);
			_frequentaPersistente.lockWrite(assist);

			IFrequenta assist2 = new Frequenta(_student2, _disciplinaExecucao1);
			_frequentaPersistente.lockWrite(assist2);
			

			_shift3 =
				new Turno(
					"turno3",
					new TipoAula(TipoAula.PRATICA),
					new Integer(1),
					_disciplinaExecucao1);
			_turnoPersistente.lockWrite(_shift3);
			_suportePersistente.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			try {
				_suportePersistente.cancelarTransaccao();
			} catch (ExcepcaoPersistencia ignored) {}
			fail("setUp failed!");
		}

	}

	/**
	 * Service Tests 
	 * */

	/**
	 * Tests Unauthorized Access.
	 * */
	public void testUnauthorizedShiftEnrolment() {
		Object args[] = { null, null };

		Object result = null;
		try {
			result = _gestor.executar(_userView2, serviceName, args);
			fail("testUnauthorized");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadStudent", result);
		}
	}

	/*
	 *	Tests a new shift enrolment by a student. 
	 */
	public void testNewShiftEnrolment() {
		Object args[] =
			{ getInfoAlunoFromIAluno(_aluno1), getInfoTurnoFromITurno(_shift3)};

		InfoEnrolmentServiceResult result = getServiceResult(args);
		assertEquals(result.getMessageType(),InfoEnrolmentServiceResult.ENROLMENT_SUCESS);
	}

	/*
	 * 
	 */
	public void testExistingShiftEnrolment() {
		Object args[] =
			{ getInfoAlunoFromIAluno(_aluno1), getInfoTurnoFromITurno(_turno2)};
			

		InfoEnrolmentServiceResult result = getServiceResult(args);
		assertEquals(result.getMessageType(),InfoEnrolmentServiceResult.ENROLMENT_SUCESS);			
	}

	public void testShiftEnrollmentIntoNonExistingShift() {
		InfoShift infoShift = getInfoTurnoFromITurno(_shift3);
		infoShift.setNome("non existing shift");
		Object args [] = {getInfoAlunoFromIAluno(_aluno1), infoShift};
		
		InfoEnrolmentServiceResult result = getServiceResult(args);
		
		assertEquals(result.getMessageType(), InfoEnrolmentServiceResult.NON_EXISTING_SHIFT);
		
	}

	public void testShiftEnrollmentByNonExistingStudent() {
		InfoStudent infoStudent = getInfoAlunoFromIAluno(_aluno1);
		infoStudent.setNumber(new Integer(-1));
		Object args [] = {infoStudent, getInfoTurnoFromITurno(_turno1)};

		InfoEnrolmentServiceResult result = getServiceResult(args);
		assertEquals(result.getMessageType(), InfoEnrolmentServiceResult.NON_EXISTING_STUDENT);
	}
	
	public void testEnrolmentIntoShiftFull() {
		InfoStudent infoStudent = getInfoAlunoFromIAluno(_aluno1);
		Object args [] = {infoStudent, getInfoTurnoFromITurno(_shift3)};

		InfoEnrolmentServiceResult result = getServiceResult(args);
		
		
		InfoStudent infoStudent2 = getInfoAlunoFromIAluno(_student2);
		
		Object args2 [] = {infoStudent2, getInfoTurnoFromITurno(_shift3)};

		InfoEnrolmentServiceResult result2 = getServiceResult(args2);
		
		
		assertEquals(result2.getMessageType(), InfoEnrolmentServiceResult.SHIFT_FULL);
				
	}


	/**
	 * Method getServiceResult.
	 * @param args
	 * @return InfoEnrolmentServiceResult
	 */
	private InfoEnrolmentServiceResult getServiceResult(Object[] args) {
		InfoEnrolmentServiceResult result = null;			
		try {
			result =
				(InfoEnrolmentServiceResult) _gestor.executar(
					_userView,
					serviceName,
					args);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			fail("Executing service!");
		}
		return result;
	}

	/**
	 * Method getInfoTurnoFromITurno.
	 * @param _shift3
	 */
	private InfoShift getInfoTurnoFromITurno(ITurno shift) {
		InfoShift infoShift = new InfoShift(shift.getNome(),null, null, null);
		return infoShift;

	}

	private InfoStudent getInfoAlunoFromIAluno(IStudent student) {
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setNumeroDocumentoIdentificacao(student.getPerson().getNumeroDocumentoIdentificacao());
		infoPerson.setTipoDocumentoIdentificacao(student.getPerson().getTipoDocumentoIdentificacao());
		infoPerson.setNome(student.getPerson().getNome());
		infoPerson.setEmail(student.getPerson().getEmail());
		infoPerson.setUsername(student.getPerson().getUsername());
		infoPerson.setPassword(student.getPerson().getPassword());

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(student.getNumber());
		infoStudent.setState(student.getState());
		infoStudent.setDegreeType(student.getDegreeType());
		infoStudent.setInfoPerson(infoPerson);
/*		try {
				
			BeanUtils.copyProperties(infoStudent, _aluno1);
			IPessoa person = _aluno1.getPerson();
			System.out.println((person == null));
			InfoPerson infoPerson = new InfoPerson();
			BeanUtils.copyProperties(infoPerson, person);
			infoStudent.setInfoPerson(infoPerson);
} catch (Exception e) {
			e.printStackTrace(System.out);
		}
*/		
		return infoStudent;
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * Constructor for StudentShiftEnrolmentTest.
	 * @param testName
	 */
	public StudentShiftEnrolmentTest(String testName) {
		super(testName);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentShiftEnrolmentTest.class);
		return suite;
	}

}
