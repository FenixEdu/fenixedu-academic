package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentInExtraCurricularCourse;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author João Mota
 * 15/Out/2003
 */

public class EditPosGradStudentCurricularPlanStateAndCredits implements IServico {

	private static EditPosGradStudentCurricularPlanStateAndCredits servico =
		new EditPosGradStudentCurricularPlanStateAndCredits();

	public static EditPosGradStudentCurricularPlanStateAndCredits getService() {
		return servico;
	}

	private EditPosGradStudentCurricularPlanStateAndCredits() {
	}

	public final String getNome() {
		return "EditPosGradStudentCurricularPlanStateAndCredits";
	}

	public void run(
		IUserView userView,
		Integer studentCurricularPlanId,
		String currentState,
		Double credits,
		String startDate,
		List extraCurricularCourses,
		String observations)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) persistentStudentCurricularPlan.readByOId(
					new StudentCurricularPlan(studentCurricularPlanId),
					true);
			if (studentCurricularPlan == null) {
				throw new InvalidArgumentsServiceException();
			}
			StudentCurricularPlanState newState = new StudentCurricularPlanState(currentState);
			IEmployee employee = null;
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			if (person == null) {
				throw new InvalidArgumentsServiceException();
			}
			employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
			if (employee == null) {
				throw new InvalidArgumentsServiceException();
			}

			studentCurricularPlan.setStartDate(stringDateToCalendar(startDate).getTime());
			studentCurricularPlan.setCurrentState(newState);
			studentCurricularPlan.setEmployee(employee);
			studentCurricularPlan.setGivenCredits(credits);
			studentCurricularPlan.setObservations(observations);
			List enrollments = studentCurricularPlan.getEnrolments();
			Iterator iterator = enrollments.iterator();
			if (newState.getState().intValue() == StudentCurricularPlanState.INACTIVE) {
				while (iterator.hasNext()) {
					IEnrolment enrolment = (IEnrolment) iterator.next();
					if (enrolment.getEnrolmentState().getValue() != EnrolmentState.APROVED_TYPE) {
						enrolment.setEnrolmentState(EnrolmentState.ANNULED);
					}

				}
			} else {

				while (iterator.hasNext()) {
					IEnrolment enrolment = (IEnrolment) iterator.next();
					IEnrolment auxEnrolment = null;
					if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
						if (!(enrolment instanceof EnrolmentInExtraCurricularCourse)) {
							auxEnrolment = new EnrolmentInExtraCurricularCourse();
							auxEnrolment.setIdInternal(enrolment.getIdInternal());
							//auxEnrolment.setCurricularCourseScope(
							//	enrolment.getCurricularCourseScope());
							auxEnrolment.setCurricularCourse(
								enrolment.getCurricularCourse());								
							auxEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
							auxEnrolment.setStudentCurricularPlan(
								enrolment.getStudentCurricularPlan());
							persistentEnrolment.simpleLockWrite(auxEnrolment);
							try {

								auxEnrolment.setEnrolmentEvaluationType(
									enrolment.getEnrolmentEvaluationType());
								auxEnrolment.setEnrolmentState(enrolment.getEnrolmentState());
								auxEnrolment.setEvaluations(enrolment.getEvaluations());

							} catch (Exception e1) {
								throw new FenixServiceException(e1);
							}
						}
					} else {
						if (enrolment instanceof EnrolmentInExtraCurricularCourse) {

							auxEnrolment = new Enrolment();
							auxEnrolment.setIdInternal(enrolment.getIdInternal());
							//auxEnrolment.setCurricularCourseScope(
							//	enrolment.getCurricularCourseScope());
							auxEnrolment.setCurricularCourse(
								enrolment.getCurricularCourse());
							auxEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
							auxEnrolment.setStudentCurricularPlan(
								enrolment.getStudentCurricularPlan());
							persistentEnrolment.simpleLockWrite(auxEnrolment);
							try {
								auxEnrolment.setEnrolmentEvaluationType(
									enrolment.getEnrolmentEvaluationType());
								auxEnrolment.setEnrolmentState(enrolment.getEnrolmentState());
								auxEnrolment.setEvaluations(enrolment.getEvaluations());
							} catch (Exception e1) {
								throw new FenixServiceException(e1);
							}

						}

					}
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

	private Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
		Calendar calendar = Calendar.getInstance();
		String[] aux = startDate.split("/");
		calendar.set(Calendar.DAY_OF_MONTH, (new Integer(aux[0])).intValue());
		calendar.set(Calendar.MONTH, (new Integer(aux[1])).intValue() - 1);
		calendar.set(Calendar.YEAR, (new Integer(aux[2])).intValue());
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

}