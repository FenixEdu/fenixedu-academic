package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentInExtraCurricularCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;
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
				(IStudentCurricularPlan) persistentStudentCurricularPlan.readByOID(
				        StudentCurricularPlan.class,studentCurricularPlanId,
					true);
			if (studentCurricularPlan == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			StudentCurricularPlanState newState = new StudentCurricularPlanState(currentState);
			
			IEmployee employee = null;
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
			
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
					IEnrollment enrolment = (IEnrollment) iterator.next();
					if (enrolment.getEnrollmentState().getValue() ==  EnrollmentState.ENROLLED_TYPE
							|| enrolment.getEnrollmentState().getValue() ==  EnrollmentState.TEMPORARILY_ENROLLED_TYPE) {
						persistentEnrolment.simpleLockWrite(enrolment);
						enrolment.setEnrollmentState(EnrollmentState.ANNULED);
					}
				}
			} else {
			   
				while (iterator.hasNext()) {
					IEnrollment enrolment = (IEnrollment) iterator.next();
					
					if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
						if (!(enrolment instanceof EnrolmentInExtraCurricularCourse)) {
						    persistentEnrolment.delete(enrolment);
						    
							IEnrollment auxEnrolment = new EnrolmentInExtraCurricularCourse();	
							persistentEnrolment.simpleLockWrite(auxEnrolment);
						    
							copyEnrollment(enrolment, auxEnrolment);
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, auxEnrolment);							
						} else {
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);						
						}
					} else {
						if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
						    persistentEnrolment.delete(enrolment);

						    IEnrollment auxEnrolment = new Enrolment();
							persistentEnrolment.simpleLockWrite(auxEnrolment);
							
							copyEnrollment(enrolment, auxEnrolment);
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, auxEnrolment);
						} else {
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);						
						}
					}
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

	/**
     * @param enrolment
     * @param auxEnrolment
     * @throws FenixServiceException
     */
    private void copyEnrollment(IEnrollment enrolment, IEnrollment auxEnrolment) throws FenixServiceException
    {
	    auxEnrolment.setIdInternal(enrolment.getIdInternal());
        //auxEnrolment.setCurricularCourseScope(
        //	enrolment.getCurricularCourseScope());
        auxEnrolment.setCurricularCourse(
        	enrolment.getCurricularCourse());
        auxEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
        auxEnrolment.setStudentCurricularPlan(
        	enrolment.getStudentCurricularPlan());
        try {
        	auxEnrolment.setEnrolmentEvaluationType(
        		enrolment.getEnrolmentEvaluationType());
        	auxEnrolment.setEnrollmentState(enrolment.getEnrollmentState());
        	auxEnrolment.setEvaluations(enrolment.getEvaluations());
        } catch (Exception e1) {
        	throw new FenixServiceException(e1);
        }
    }

    /**
     * @param newState
     * @param persistentEnrolment
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    private void changeAnnulled2ActiveIfActivePlan(StudentCurricularPlanState newState, IPersistentEnrollment persistentEnrolment, IEnrollment enrolment) throws ExcepcaoPersistencia
    {
        if (newState.getState().intValue() == StudentCurricularPlanState.ACTIVE) {
            if (enrolment.getEnrollmentState().getValue() ==  EnrollmentState.ANNULED_TYPE) {
        		persistentEnrolment.simpleLockWrite(enrolment);		
        		enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
        	}					    
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