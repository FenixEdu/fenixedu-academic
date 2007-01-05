package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditPosGradStudentCurricularPlanStateAndCredits extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanId, String currentState,
	    Double credits, String startDate, List extraCurricularCourses, String observations,
	    Integer branchId, String specialization) throws FenixServiceException, ExcepcaoPersistencia {
	StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanId);
	if (studentCurricularPlan == null) {
	    throw new InvalidArgumentsServiceException();
	}

	StudentCurricularPlanState newState = StudentCurricularPlanState.valueOf(currentState);

	Person person = Person.readPersonByUsername(userView.getUtilizador());
	if (person == null) {
	    throw new InvalidArgumentsServiceException();
	}

	Employee employee = person.getEmployee();
	if (employee == null) {
	    throw new InvalidArgumentsServiceException();
	}

	Branch branch = rootDomainObject.readBranchByOID(branchId);
	if (branch == null) {
	    throw new InvalidArgumentsServiceException();
	}

	studentCurricularPlan.setStartDate(stringDateToCalendar(startDate).getTime());
	studentCurricularPlan.setCurrentState(newState);
	studentCurricularPlan.setEmployee(employee);
	studentCurricularPlan.setGivenCredits(credits);
	studentCurricularPlan.setObservations(observations);
	studentCurricularPlan.setBranch(branch);
	studentCurricularPlan.setSpecialization(Specialization.valueOf(specialization));

	List enrollments = studentCurricularPlan.getEnrolments();
	Iterator iterator = enrollments.iterator();
	if (newState.equals(StudentCurricularPlanState.INACTIVE)) {
	    while (iterator.hasNext()) {
		Enrolment enrolment = (Enrolment) iterator.next();
		if (enrolment.getEnrollmentState() == EnrollmentState.ENROLLED
			|| enrolment.getEnrollmentState() == EnrollmentState.TEMPORARILY_ENROLLED) {
		    enrolment.setEnrollmentState(EnrollmentState.ANNULED);
		}
	    }
	} else {

	    while (iterator.hasNext()) {
		Enrolment enrolment = (Enrolment) iterator.next();
		if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
		    if (!(enrolment.isExtraCurricular())) {

			Enrolment auxEnrolment = new Enrolment(enrolment.getStudentCurricularPlan(),
				enrolment.getCurricularCourse(), enrolment.getExecutionPeriod(),
				enrolment.getEnrolmentCondition(), enrolment.getCreatedBy(), true);

			copyEnrollment(enrolment, auxEnrolment);
			setEnrolmentCreationInformation(userView, auxEnrolment);

			changeAnnulled2ActiveIfActivePlan(newState, auxEnrolment);

			enrolment.delete();
		    } else {
			changeAnnulled2ActiveIfActivePlan(newState, enrolment);
		    }
		} else {
		    if (enrolment.isExtraCurricular()) {

			Enrolment auxEnrolment = new Enrolment(enrolment.getStudentCurricularPlan(),
				enrolment.getCurricularCourse(), enrolment.getExecutionPeriod(),
				enrolment.getEnrolmentCondition(), enrolment.getCreatedBy(), true);

			copyEnrollment(enrolment, auxEnrolment);
			setEnrolmentCreationInformation(userView, auxEnrolment);

			changeAnnulled2ActiveIfActivePlan(newState, auxEnrolment);

			enrolment.delete();
		    } else {
			changeAnnulled2ActiveIfActivePlan(newState, enrolment);
		    }
		}
	    }
	}

    }

    private void setEnrolmentCreationInformation(IUserView userView, Enrolment auxEnrolment) {
	auxEnrolment.setCreationDate(Calendar.getInstance().getTime());
	auxEnrolment.setCreatedBy(userView.getUtilizador());
    }

    /**
         * @param enrolment
         * @param auxEnrolment
         * @throws FenixServiceException
         */
    private void copyEnrollment(Enrolment enrolment, Enrolment auxEnrolment) {

	auxEnrolment.setCreationDate(enrolment.getCreationDate());
	auxEnrolment.setAccumulatedWeight(enrolment.getAccumulatedWeight());
	auxEnrolment.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
	auxEnrolment.setEnrollmentState(enrolment.getEnrollmentState());

	for (final List<EnrolmentEvaluation> evaluations = enrolment.getEvaluations(); !evaluations
		.isEmpty(); auxEnrolment.addEvaluations(evaluations.get(0)))
	    ;
	for (final List<EnrolmentEquivalence> equivalences = enrolment.getEnrolmentEquivalences(); !equivalences
		.isEmpty(); auxEnrolment.addEnrolmentEquivalences(equivalences.get(0)))
	    ;
	for (final List<Attends> attends = enrolment.getAttends(); !attends.isEmpty(); auxEnrolment
		.addAttends(attends.get(0)))
	    ;
	for (final List<CreditsInAnySecundaryArea> credits = enrolment.getCreditsInAnySecundaryAreas(); !credits
		.isEmpty(); auxEnrolment.addCreditsInAnySecundaryAreas(credits.get(0)))
	    ;
	for (final List<CreditsInScientificArea> credits = enrolment.getCreditsInScientificAreas(); !credits
		.isEmpty(); auxEnrolment.addCreditsInScientificAreas(credits.get(0)))
	    ;

    }

    private void changeAnnulled2ActiveIfActivePlan(StudentCurricularPlanState newState,
	    Enrolment enrolment) throws ExcepcaoPersistencia {
	if (newState.equals(StudentCurricularPlanState.ACTIVE)) {
	    if (enrolment.getEnrollmentState() == EnrollmentState.ANNULED) {
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

	return calendar;
    }

}
