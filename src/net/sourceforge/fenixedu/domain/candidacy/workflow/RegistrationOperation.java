package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.StudentState;

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
	getDegreeCandidacy().getPerson().addPersonRoleByRoleType(RoleType.PERSON);
	getDegreeCandidacy().getPerson().addPersonRoleByRoleType(RoleType.STUDENT);

	/*
	boolean needsCandidateRole = false;
	for (final Candidacy candidacy : getDegreeCandidacy().getPerson().getCandidaciesSet()) {
	    if (candidacy.equals(getDegreeCandidacy())) {
		continue;
	    }

	    if (!candidacy.isConcluded()) {
		needsCandidateRole = true;
		break;
	    }
	}

	if (!needsCandidateRole) {
	    getDegreeCandidacy().getPerson().removeRoleByType(RoleType.CANDIDATE);
	}
	*/
    }

    protected void associateShiftsFor(final Registration registration) {

	if (getExecutionYear().hasShiftDistribution()) {

	    final List<Registration> registrations = getExecutionDegree()
		    .getRegistrationsForDegreeCandidacies();
	    Collections.sort(registrations, Registration.NUMBER_COMPARATOR);

	    for (final Shift shift : getExecutionDegree().getShiftsFromShiftDistributionBasedOn(
		    registrations.indexOf(registration))) {
		shift.addStudents(registration);
	    }
	}
    }

    private ExecutionDegree getExecutionDegree() {
	return getDegreeCandidacy().getExecutionDegree();
    }

    protected ExecutionYear getExecutionYear() {
	return getExecutionDegree().getExecutionYear();
    }

    protected void enrolStudentInCurricularCourses(final ExecutionDegree executionDegree,
	    final Registration registration) {
	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	final StudentCurricularPlan studentCurricularPlan = StudentCurricularPlan
		.createBolonhaStudentCurricularPlan(registration, executionDegree
			.getDegreeCurricularPlan(), StudentCurricularPlanState.ACTIVE,
			new YearMonthDay(), executionPeriod);

	studentCurricularPlan.createFirstTimeStudentEnrolmentsFor(executionPeriod, getCurrentUsername());
    }

    private String getCurrentUsername() {
	if (AccessControl.getUserView() != null) {
	    return AccessControl.getUserView().getPerson().getUsername();
	}
	return getDegreeCandidacy().getPerson().getUsername();
    }

    private ExecutionPeriod getExecutionPeriod() {
	return ExecutionPeriod.readBySemesterAndExecutionYear(1, getExecutionYear().getYear());
    }

    protected Registration createRegistration() {
	final DegreeCandidacy degreeCandidacy = getDegreeCandidacy();
	final Registration registration = new Registration(getDegreeCandidacy().getPerson(), Student
		.generateStudentNumber(), StudentKind.readByStudentType(StudentType.NORMAL),
		new StudentState(StudentState.INSCRITO), Boolean.valueOf(false), Boolean.valueOf(false),
		DegreeType.BOLONHA_DEGREE, degreeCandidacy);

	registration.getStudent().setPersonalDataAuthorizationForCurrentExecutionYear(
		getDegreeCandidacy().getStudentPersonalDataAuthorizationChoice());

	if (getDegreeCandidacy().getApplyForResidence()) {
	    registration.getStudent().setResidenceCandidacyForCurrentExecutionYear(
		    getDegreeCandidacy().getNotesAboutResidenceAppliance());
	}

	return registration;
    }

    private DegreeCandidacy getDegreeCandidacy() {
	return ((DegreeCandidacy) getCandidacy());
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