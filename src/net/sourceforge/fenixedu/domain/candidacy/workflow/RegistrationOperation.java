package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

public class RegistrationOperation extends CandidacyOperation {

    public RegistrationOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
	super(roleTypes, candidacy);
    }

    @Override
    protected void internalExecute() {
	final ExecutionDegree executionDegree = getExecutionDegree();
	final Registration registration = createRegistration();
	enrolStudentInCurricularCourses(executionDegree, registration);
	associateShiftsFor(registration);
	changePersonRoles();
    }

    private void changePersonRoles() {
	getStudentCandidacy().getPerson().addPersonRoleByRoleType(RoleType.STUDENT);
    }

    protected void associateShiftsFor(final Registration registration) {

	if (getExecutionYear().hasShiftDistribution()) {
            for (final ShiftDistributionEntry shiftEntry : getExecutionDegree().getNextFreeShiftDistributions()) {
                shiftEntry.setDistributed(Boolean.TRUE);
                shiftEntry.getShift().addStudents(registration);
                correctExecutionCourseIfNecessary(registration, shiftEntry.getShift());
            }
        }
    }

    private void correctExecutionCourseIfNecessary(Registration registration, Shift shift) {

	final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
	final ExecutionCourse finalExecutionCourse = shift.getExecutionCourse();

	for (final CurricularCourse curricularCourse : finalExecutionCourse.getAssociatedCurricularCoursesSet()) {

	    final Enrolment enrolment = studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,
		    ExecutionSemester.readActualExecutionPeriod());
	    if (enrolment != null) {

		final Attends attends = enrolment.getAttendsFor(ExecutionSemester.readActualExecutionPeriod());
		if (attends != null && !attends.isFor(finalExecutionCourse)) {
		    attends.setDisciplinaExecucao(finalExecutionCourse);
		}
		break;
	    }
	}
    }

    private ExecutionDegree getExecutionDegree() {
	return getStudentCandidacy().getExecutionDegree();
    }

    protected ExecutionYear getExecutionYear() {
	return getExecutionDegree().getExecutionYear();
    }

    protected void enrolStudentInCurricularCourses(final ExecutionDegree executionDegree, final Registration registration) {
	final ExecutionSemester executionSemester = getExecutionPeriod();
	final StudentCurricularPlan studentCurricularPlan = StudentCurricularPlan.createBolonhaStudentCurricularPlan(
		registration, executionDegree.getDegreeCurricularPlan(), new YearMonthDay(), executionSemester);

	studentCurricularPlan.createFirstTimeStudentEnrolmentsFor(executionSemester, getCurrentUsername());
    }

    private String getCurrentUsername() {
	if (AccessControl.getUserView() != null) {
	    return AccessControl.getPerson().getUsername();
	}
	return getStudentCandidacy().getPerson().getUsername();
    }

    private ExecutionSemester getExecutionPeriod() {
	return getExecutionYear().readExecutionPeriodForSemester(1);
    }

    protected Registration createRegistration() {
	final Registration registration = new Registration(getStudentCandidacy().getPerson(), getStudentCandidacy());

	registration.getStudent().setPersonalDataAuthorizationForCurrentExecutionYear(
		getStudentCandidacy().getStudentPersonalDataAuthorizationChoice());

	if (getStudentCandidacy().getApplyForResidence()) {
	    registration.getStudent().setResidenceCandidacyForCurrentExecutionYear(
		    getStudentCandidacy().getNotesAboutResidenceAppliance());
	}

	return registration;
    }

    private StudentCandidacy getStudentCandidacy() {
	return ((StudentCandidacy) getCandidacy());
    }

    @Override
    public CandidacyOperationType getType() {
	return CandidacyOperationType.REGISTRATION;
    }

    @Override
    public boolean isInput() {
	return false;
    }

    @Override
    public boolean isAuthorized(Person person) {
	if (getCandidacy().getPerson().hasRole(RoleType.PERSON)) {
	    return person.hasRole(RoleType.EMPLOYEE);
	} else {
	    return super.isAuthorized(person);
	}
    }

}