package ServidorAplicacao
	.Servico
	.masterDegree
	.administrativeOffice
	.student
	.studentCurricularPlan;

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
import Util.StudentCurricularPlanState;

/**
 * @author João Mota
 * 15/Out/2003
 */

public class EditPosGradStudentCurricularPlanStateAndCredits
	implements IServico {

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
		List extraCurricularCourses)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			IStudentCurricularPlan studentCurricularPlan =
				(
					IStudentCurricularPlan) persistentStudentCurricularPlan
						.readByOId(
					new StudentCurricularPlan(studentCurricularPlanId),
					true);
			if (studentCurricularPlan == null) {
				throw new InvalidArgumentsServiceException();
			}
			StudentCurricularPlanState newState =
				new StudentCurricularPlanState(currentState);
			IEmployee employee = null;
			IPersistentEmployee persistentEmployee =
				sp.getIPersistentEmployee();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentEnrolment persistentEnrolment =
				sp.getIPersistentEnrolment();
			IPessoa person =
				persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			if (person == null) {
				throw new InvalidArgumentsServiceException();
			}
			employee =
				persistentEmployee.readByPerson(
					person.getIdInternal().intValue());
			if (employee == null) {
				throw new InvalidArgumentsServiceException();
			}
			studentCurricularPlan.setCurrentState(newState);
			studentCurricularPlan.setEmployee(employee);
			studentCurricularPlan.setGivenCredits(credits);
			List enrollments = studentCurricularPlan.getEnrolments();
			Iterator iterator = enrollments.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				IEnrolment auxEnrolment = null;
				if (extraCurricularCourses
					.contains(enrolment.getIdInternal())) {
					if (!(enrolment
						instanceof EnrolmentInExtraCurricularCourse)) {
						System.out.println("normal->extra");

						auxEnrolment = new EnrolmentInExtraCurricularCourse();
						auxEnrolment.setIdInternal(enrolment.getIdInternal());
						auxEnrolment.setCurricularCourseScope(
							enrolment.getCurricularCourseScope());
						auxEnrolment.setExecutionPeriod(
							enrolment.getExecutionPeriod());
						auxEnrolment.setStudentCurricularPlan(
							enrolment.getStudentCurricularPlan());
						persistentEnrolment.simpleLockWrite(auxEnrolment);
						try {

							auxEnrolment.setEnrolmentEvaluationType(
								enrolment.getEnrolmentEvaluationType());
							auxEnrolment.setEnrolmentState(
								enrolment.getEnrolmentState());
							auxEnrolment.setEvaluations(
								enrolment.getEvaluations());

						} catch (Exception e1) {
							throw new FenixServiceException(e1);
						}
					}
				} else {
					if (enrolment
						instanceof EnrolmentInExtraCurricularCourse) {

						auxEnrolment = new Enrolment();
						auxEnrolment.setIdInternal(enrolment.getIdInternal());
						auxEnrolment.setCurricularCourseScope(
							enrolment.getCurricularCourseScope());
						auxEnrolment.setExecutionPeriod(
							enrolment.getExecutionPeriod());
						auxEnrolment.setStudentCurricularPlan(
							enrolment.getStudentCurricularPlan());
						persistentEnrolment.simpleLockWrite(auxEnrolment);
						try {
							auxEnrolment.setEnrolmentEvaluationType(
							enrolment.getEnrolmentEvaluationType());
						auxEnrolment.setEnrolmentState(
							enrolment.getEnrolmentState());
						auxEnrolment.setEvaluations(
							enrolment.getEvaluations());
						} catch (Exception e1) {
							throw new FenixServiceException(e1);
						}

					}

				}
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}