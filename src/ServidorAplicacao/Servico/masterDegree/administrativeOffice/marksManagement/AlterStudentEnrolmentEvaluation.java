package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.EnrolmentEvaluation;
import Dominio.Funcionario;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.TipoCurso;

/**
 * @author Angela
 * 04/07/2003
 * 
 */
public class AlterStudentEnrolmentEvaluation implements IServico {

	private static AlterStudentEnrolmentEvaluation servico = new AlterStudentEnrolmentEvaluation();

	/**
	 * The singleton access method of this class.
	 **/
	public static AlterStudentEnrolmentEvaluation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private AlterStudentEnrolmentEvaluation() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "AlterStudentEnrolmentEvaluation";
	}
	public List run(Integer curricularCourseCode, Integer enrolmentEvaluationCode, InfoEnrolmentEvaluation infoEnrolmentEvaluation, Integer teacherNumber,  IUserView userView)
		throws FenixServiceException {
			
	List infoEvaluationsWithError = null;
	try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

		//			responsible teacher
		ITeacher teacher = persistentTeacher.readTeacherByNumber(teacherNumber);
		if (teacher == null) {
			throw new NonExistingServiceException();
		}
		//			employee
		IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
		Funcionario employee = readEmployee(person);

		//			curricular Course Scope
		ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
		curricularCourseScope.setIdInternal(curricularCourseCode);
		curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);
		
		infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);
		
		
		infoEvaluationsWithError = new ArrayList();
		Calendar calendario = Calendar.getInstance();

			if (!isValidEvaluation(infoEnrolmentEvaluation)) {
				infoEvaluationsWithError.add(infoEnrolmentEvaluation);
			} else {
//				read enrolment
				IEnrolmentEvaluation iEnrolmentEvaluation = new EnrolmentEvaluation();

				iEnrolmentEvaluation.setIdInternal(enrolmentEvaluationCode);
			    iEnrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation.readByOId(iEnrolmentEvaluation,false);

				IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
				enrolmentEvaluation = Cloner.copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(infoEnrolmentEvaluation);
				enrolmentEvaluation.setEnrolment(iEnrolmentEvaluation.getEnrolment());

				if (infoEnrolmentEvaluation.getExamDate() != null) {
					enrolmentEvaluation.setExamDate(infoEnrolmentEvaluation.getExamDate());
				} else {
					enrolmentEvaluation.setExamDate(calendario.getTime());
				}
				enrolmentEvaluation.setGrade(infoEnrolmentEvaluation.getGrade());
				EnrolmentEvaluationState enrolmentEvaluationState = EnrolmentEvaluationState.FINAL_OBJ;
				enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
				enrolmentEvaluation.setGradeAvailableDate(calendario.getTime());
				enrolmentEvaluation.setEnrolmentEvaluationType(infoEnrolmentEvaluation.getEnrolmentEvaluationType());
				enrolmentEvaluation.setEnrolmentEvaluationState(enrolmentEvaluationState);
				enrolmentEvaluation.setEmployee(employee);
				enrolmentEvaluation.setWhen(calendario.getTime());
				enrolmentEvaluation.setObservation(infoEnrolmentEvaluation.getObservation());
				enrolmentEvaluation.setCheckSum("");
				persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);
				
			}
		
	} catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();
		FenixServiceException newEx = new FenixServiceException("");
		newEx.fillInStackTrace();
		throw newEx;
	}

	return infoEvaluationsWithError;
}

private Funcionario readEmployee(IPessoa person) {
	Funcionario employee = null;
	SuportePersistente spJDBC = SuportePersistente.getInstance();
	IFuncionarioPersistente persistentEmployee = spJDBC.iFuncionarioPersistente();

	try {
		spJDBC.iniciarTransaccao();

		try {
			employee = persistentEmployee.lerFuncionarioPorPessoa(person.getIdInternal().intValue());
System.out.println("no servio  employee" + employee.getCodigoInterno());

		} catch (Exception e) {
			spJDBC.cancelarTransaccao();
			e.printStackTrace();
			return employee;
		}

		spJDBC.confirmarTransaccao();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		return employee;
	}
}

private InfoEnrolmentEvaluation completeEnrolmentEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation)
	throws FenixServiceException {
	try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();

		//			Student
		IStudent student = new Student();
		student = (IStudent) persistentStudent.readStudentByNumberAndDegreeType(infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber(), new TipoCurso(TipoCurso.MESTRADO));
		infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().setInfoStudent(
						Cloner.copyIStudent2InfoStudent(student));
		
						
		return infoEnrolmentEvaluation;
	} catch (ExcepcaoPersistencia e) {
		throw new FenixServiceException(e);
	}
}

private boolean isValidEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
	IStudentCurricularPlan studentCurricularPlan = null;

	try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IStudentCurricularPlanPersistente curricularPlanPersistente = sp.getIStudentCurricularPlanPersistente();

		studentCurricularPlan =
			curricularPlanPersistente.readActiveStudentCurricularPlan(
				infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber(),
				infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getDegreeType());
System.out.println("curricular plan student" + studentCurricularPlan);

	} catch (Exception e) {
		e.printStackTrace();
	}

	IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

	// test marks by execution course: strategy 
	IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
	IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
		degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
	if (infoEnrolmentEvaluation.getGrade() == null || infoEnrolmentEvaluation.getGrade().length() == 0) {
		return false;
	} else {
		return degreeCurricularPlanStrategy.checkMark(infoEnrolmentEvaluation.getGrade());
	}
}

}
