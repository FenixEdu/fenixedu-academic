package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEmployee;
import Dominio.IEnrolment;
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
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;
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
	public List run(
		Integer curricularCourseCode,
		Integer enrolmentEvaluationCode,
		InfoEnrolmentEvaluation infoEnrolmentEvaluation,
		Integer teacherNumber,
		IUserView userView)
		throws FenixServiceException {

		List infoEvaluationsWithError = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();

			//			responsible teacher
			ITeacher teacher = persistentTeacher.readTeacherByNumber(teacherNumber);
			if (teacher == null) {
				throw new NonExistingServiceException();
			}
			//		employee
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			IEmployee employee = readEmployee(person);

			//			curricular Course Scope
			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

			infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);

			infoEvaluationsWithError = new ArrayList();
			Calendar calendario = Calendar.getInstance();

			if (!isValidEvaluation(infoEnrolmentEvaluation)) {
				infoEvaluationsWithError.add(infoEnrolmentEvaluation);
			} else {
		
			
				// read enrolmentEvaluation
				IEnrolmentEvaluation iEnrolmentEvaluation = new EnrolmentEvaluation();

				iEnrolmentEvaluation.setIdInternal(enrolmentEvaluationCode);
				iEnrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation.readByOId(iEnrolmentEvaluation, false);

				IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
				enrolmentEvaluation = Cloner.copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(infoEnrolmentEvaluation);

				//check for an alteration
				if (!enrolmentEvaluation.getGrade().equals(iEnrolmentEvaluation.getGrade())) {
					try {
						iEnrolmentEvaluation.getEnrolment().setEnrolmentState(EnrolmentState.APROVED);
					} catch (NumberFormatException e) {
						if (enrolmentEvaluation.getGrade().equals("RE"))
							iEnrolmentEvaluation.getEnrolment().setEnrolmentState(EnrolmentState.NOT_APROVED);		
						if (enrolmentEvaluation.getGrade().equals("NA"))
							iEnrolmentEvaluation.getEnrolment().setEnrolmentState(EnrolmentState.NOT_EVALUATED);
					}
					IEnrolment enrolment = new Enrolment();
					enrolment.setCurricularCourseScope(iEnrolmentEvaluation.getEnrolment().getCurricularCourseScope());
					enrolment.setEnrolmentEvaluationType(iEnrolmentEvaluation.getEnrolment().getEnrolmentEvaluationType());
					enrolment.setExecutionPeriod(iEnrolmentEvaluation.getEnrolment().getExecutionPeriod());
					enrolment.setStudentCurricularPlan(iEnrolmentEvaluation.getEnrolment().getStudentCurricularPlan());
					enrolment.setIdInternal(iEnrolmentEvaluation.getEnrolment().getIdInternal());
					enrolment.setEnrolmentState(iEnrolmentEvaluation.getEnrolment().getEnrolmentState());
					System.out.println("verificar se há ou nap alteracao---- " + enrolment.getEnrolmentState());
					persistentEnrolment.lockWrite(enrolment);
					iEnrolmentEvaluation.setEnrolment(enrolment);

				}		

				enrolmentEvaluation.setEnrolment(iEnrolmentEvaluation.getEnrolment());
				enrolmentEvaluation.setWhen(calendario.getTime());
				persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);
				if (infoEnrolmentEvaluation.getExamDate() != null) {
					enrolmentEvaluation.setExamDate(infoEnrolmentEvaluation.getExamDate());
				} else {
					enrolmentEvaluation.setExamDate(calendario.getTime());
				}
				if (infoEnrolmentEvaluation.getGradeAvailableDate() != null) {
					enrolmentEvaluation.setGradeAvailableDate(infoEnrolmentEvaluation.getGradeAvailableDate());
				} else {
					enrolmentEvaluation.setGradeAvailableDate(calendario.getTime());
				}
				enrolmentEvaluation.setGrade(infoEnrolmentEvaluation.getGrade());
				EnrolmentEvaluationState enrolmentEvaluationState = EnrolmentEvaluationState.FINAL_OBJ;
				enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
				//			enrolmentEvaluation.setGradeAvailableDate(calendario.getTime());
				enrolmentEvaluation.setEnrolmentEvaluationType(infoEnrolmentEvaluation.getEnrolmentEvaluationType());
				enrolmentEvaluation.setEnrolmentEvaluationState(enrolmentEvaluationState);
				enrolmentEvaluation.setEmployee(employee);
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

	private IEmployee readEmployee(IPessoa person) {
		IEmployee employee = null;
		IPersistentEmployee persistentEmployee;
		try {
			persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
			employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		return employee;
	}

	private InfoEnrolmentEvaluation completeEnrolmentEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			//			Student
			IStudent student = new Student();
			student =
				(IStudent) persistentStudent.readStudentByNumberAndDegreeType(
					infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber(),
					new TipoCurso(TipoCurso.MESTRADO));
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
			return degreeCurricularPlanStrategy.checkMark(infoEnrolmentEvaluation.getGrade().toUpperCase());
		}
	}

}
