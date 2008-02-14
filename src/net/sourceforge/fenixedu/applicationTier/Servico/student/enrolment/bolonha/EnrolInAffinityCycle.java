package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.MDCandidacy;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.AffinityCyclesManagement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.DateTime;

public class EnrolInAffinityCycle extends Service {

    /**
         * This method is used when student is enroling second cycle without
         * conclude first cycle
         * 
         */
    public void run(final Person person, final CycleEnrolmentBean cycleBean) {
	final StudentCurricularPlan studentCurricularPlan = cycleBean.getStudentCurricularPlan();
	studentCurricularPlan.enrolInAffinityCycle(cycleBean.getCycleCourseGroupToEnrol(), cycleBean.getExecutionPeriod());
    }

    /**
         * This method is used to create new registrations based on a new cycle.
         * If second cycle belongs to the same DegreeCurricularPlan than we use
         * studentCurricularPlan.enrolInAffinityCycle(cycleCourseGroupToEnrol,
         * executionPeriod). Else we create a new empty registration or we
         * separate the old second cycle that exists in previous
         * StudentCurricularPlan to a new registration
         * 
         * 
         */
    public Registration run(final Person person, final StudentCurricularPlan studentCurricularPlan,
	    final CycleCourseGroup cycleCourseGroupToEnrol, final ExecutionPeriod executionPeriod) throws FenixServiceException {

	// TODO: move to domain
	// TODO: refactor this code, should be more generic

	checkConditionsToEnrol(studentCurricularPlan, executionPeriod);

	final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
	if (secondCycle == null) {

	    if (studentCurricularPlanAllowAffinityCycle(studentCurricularPlan, cycleCourseGroupToEnrol)) {
		studentCurricularPlan.enrolInAffinityCycle(cycleCourseGroupToEnrol, executionPeriod);
		return studentCurricularPlan.getRegistration();

	    } else {

		final Student student = studentCurricularPlan.getRegistration().getStudent();
		if (student.hasRegistrationFor(cycleCourseGroupToEnrol.getParentDegreeCurricularPlan())) {
		    throw new FenixServiceException("error");
		}

		final MDCandidacy candidacy = createMDCandidacy(student, cycleCourseGroupToEnrol, executionPeriod);
		final Registration newRegistration = new Registration(student.getPerson(), cycleCourseGroupToEnrol
			.getParentDegreeCurricularPlan(), candidacy, RegistrationAgreement.NORMAL, cycleCourseGroupToEnrol
			.getCycleType());

		newRegistration.setSourceRegistration(newRegistration);
		newRegistration.getActiveState().setResponsiblePerson(null);
		newRegistration.setIngression(Ingression.DA1C);
		
		markOldRegistrationWithConcludedState(studentCurricularPlan);

		return newRegistration;
	    }

	} else if (secondCycle.isExternal()) {
	    return new AffinityCyclesManagement(studentCurricularPlan).enrol(cycleCourseGroupToEnrol);
	} else {
	    throw new FenixServiceException("error");
	}
    }

    private void markOldRegistrationWithConcludedState(final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.getRegistration().hasState(RegistrationStateType.CONCLUDED)) {
	    return;
	}
	
	final Registration registration = studentCurricularPlan.getRegistration();
	final RegistrationState state = RegistrationState.createState(registration, null, new DateTime(),
		RegistrationStateType.CONCLUDED);
	state.setResponsiblePerson(null);
    }

    private boolean studentCurricularPlanAllowAffinityCycle(final StudentCurricularPlan studentCurricularPlan,
	    final CycleCourseGroup cycleCourseGroupToEnrol) {
	return studentCurricularPlan.getCycleTypes().contains(cycleCourseGroupToEnrol.getCycleType())
		&& studentCurricularPlan.getDegreeCurricularPlan() == cycleCourseGroupToEnrol.getParentDegreeCurricularPlan();
    }

    private MDCandidacy createMDCandidacy(final Student student, final CycleCourseGroup cycleCourseGroupToEnrol,
	    final ExecutionPeriod executionPeriod) {
	return new MDCandidacy(student.getPerson(), cycleCourseGroupToEnrol.getParentDegreeCurricularPlan()
		.getExecutionDegreeByYear(executionPeriod.getExecutionYear()));
    }

    private void checkConditionsToEnrol(final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod)
	    throws FenixServiceException {
	if (isFromSpecialSeason(studentCurricularPlan, executionPeriod)) {
	    if (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriodInCurricularCoursesSpecialSeason() == null) {
		throw new FenixServiceException("error.out.of.enrolment.period");
	    }
	} else {
	    if (!studentCurricularPlan.getDegreeCurricularPlan().hasActualEnrolmentPeriodInCurricularCourses()) {
		throw new FenixServiceException("error.out.of.enrolment.period");
	    }
	}

	if (studentCurricularPlan.getRegistration().getStudent().isAnyTuitionInDebt()) {
	    throw new FenixServiceException("error.message.tuitionNotPayed");
	}
    }

    private boolean isFromSpecialSeason(final StudentCurricularPlan activeStudentCurricularPlan,
	    final ExecutionPeriod executionPeriod) {
	return activeStudentCurricularPlan.hasSpecialSeasonOrHasSpecialSeasonInTransitedStudentCurricularPlan(executionPeriod);
    }
}
