package net.sourceforge.fenixedu.domain.student;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.MDCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.Equivalence;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExtraCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.OptionalDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.domain.studentCurriculum.TemporarySubstitution;

import org.joda.time.DateTime;

public class SeparationCyclesManagement {

    private static final List<DegreeType> ACCEPTED_DEGREE_TYPES = Arrays.asList(DegreeType.BOLONHA_DEGREE,
	    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

    public SeparationCyclesManagement() {
    }

    public Registration separateSecondCycle(final StudentCurricularPlan studentCurricularPlan) {
	checkIfCanSeparateSecondCycle(studentCurricularPlan);
	return createNewSecondCycle(studentCurricularPlan);
    }

    protected void checkIfCanSeparateSecondCycle(final StudentCurricularPlan studentCurricularPlan) {
	if (!studentCurricularPlan.isBolonhaDegree()) {
	    throw new DomainException("error.AffinityCyclesManagement.not.bolonha.degree");
	}

	if (!studentCurricularPlan.isActive() && !studentCurricularPlan.getRegistration().isConcluded()) {
	    throw new DomainException("error.AffinityCyclesManagement.not.active.or.concluded", studentCurricularPlan.getName());
	}

	if (studentCurricularPlan.isConclusionProcessed()) {
	    throw new DomainException("error.AffinityCyclesManagement.conclusion.processed");
	}

	if (!ACCEPTED_DEGREE_TYPES.contains(studentCurricularPlan.getDegreeType())) {
	    throw new DomainException("error.AffinityCyclesManagement.invalid.degreeType");
	}

	final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
	if (secondCycle == null || !secondCycle.isExternal()) {
	    throw new DomainException("error.AffinityCyclesManagement.invalid.secondCycle");
	}

	final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
	if (firstCycle == null || !firstCycle.isConcluded()) {
	    throw new DomainException("error.AffinityCyclesManagement.invalid.firstCycle");
	}

	if (studentAlreadyHasNewRegistration(studentCurricularPlan)) {
	    final DegreeCurricularPlan degreeCurricularPlan = secondCycle.getDegreeCurricularPlanOfDegreeModule();
	    throw new DomainException("error.AffinityCyclesManagement.already.has.registration", degreeCurricularPlan.getName());
	}
    }

    private boolean studentAlreadyHasNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
	final Student student = studentCurricularPlan.getRegistration().getStudent();
	return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
    }

    protected Registration createNewSecondCycle(final StudentCurricularPlan oldStudentCurricularPlan) {
	final Student student = oldStudentCurricularPlan.getRegistration().getStudent();
	final CycleCurriculumGroup oldSecondCycle = oldStudentCurricularPlan.getSecondCycle();
	final DegreeCurricularPlan degreeCurricularPlan = oldSecondCycle.getDegreeCurricularPlanOfDegreeModule();

	final Registration newRegistration = createRegistration(student, oldStudentCurricularPlan);
	final StudentCurricularPlan newStudentCurricularPlan = createStudentCurricularPlan(newRegistration, degreeCurricularPlan);
	final CycleCurriculumGroup newSecondCycle = newStudentCurricularPlan.getSecondCycle();

	copyCycleCurriculumGroupsInformation(oldSecondCycle, newSecondCycle);
	moveExtraCurriculumGroupInformation(oldStudentCurricularPlan, newStudentCurricularPlan);
	tryRemoveOldSecondCycle(oldSecondCycle);
	markOldRegistrationWithConcludedState(oldStudentCurricularPlan);

	return newRegistration;
    }

    private Registration createRegistration(final Student student, final StudentCurricularPlan sourceStudentCurricularPlan) {

	final CycleCurriculumGroup oldSecondCycle = sourceStudentCurricularPlan.getSecondCycle();
	Registration registration = student.getRegistrationFor(oldSecondCycle.getDegreeCurricularPlanOfDegreeModule());

	if (registration != null) {
	    return registration;
	}

	registration = new Registration(student.getPerson(), student.getNumber());
	registration.setDegree(oldSecondCycle.getDegreeCurricularPlanOfDegreeModule().getDegree());
	registration.setStudentCandidacy(createMDCandidacy(student, oldSecondCycle));
	registration.setStartDate(getExecutionPeriod().getBeginDateYearMonthDay());
	registration.getActiveState().setStateDate(getExecutionPeriod().getBeginDateYearMonthDay());
	registration.setSourceRegistration(sourceStudentCurricularPlan.getRegistration());
	registration.getActiveState().setResponsiblePerson(null);
	registration.setRegistrationAgreement(RegistrationAgreement.NORMAL);

	return registration;
    }

    private MDCandidacy createMDCandidacy(final Student student, final CycleCurriculumGroup oldSecondCycle) {
	final DegreeCurricularPlan degreeCurricularPlan = oldSecondCycle.getDegreeCurricularPlanOfDegreeModule();
	return new MDCandidacy(student.getPerson(), degreeCurricularPlan.getExecutionDegreeByYear(getExecutionYear()));
    }

    private StudentCurricularPlan createStudentCurricularPlan(final Registration registration,
	    final DegreeCurricularPlan degreeCurricularPlan) {

	StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(degreeCurricularPlan);
	if (studentCurricularPlan != null) {
	    return studentCurricularPlan;
	}

	studentCurricularPlan = new StudentCurricularPlan(registration, degreeCurricularPlan, registration.getStartDate());
	new RootCurriculumGroup(studentCurricularPlan, degreeCurricularPlan.getRoot(), null);
	// set ingression after create studentcurricularPlan
	registration.setIngression(Ingression.DA1C);

	return studentCurricularPlan;
    }

    private void copyCycleCurriculumGroupsInformation(final CycleCurriculumGroup oldSecondCycle,
	    final CycleCurriculumGroup newSecondCycle) {
	for (final CurriculumModule curriculumModule : oldSecondCycle.getCurriculumModulesSet()) {
	    if (curriculumModule.isLeaf()) {
		copyCurricumLineInformation((CurriculumLine) curriculumModule, newSecondCycle);
	    } else {
		copyCurriculumGroupsInformation((CurriculumGroup) curriculumModule, newSecondCycle);
	    }
	}
    }

    private void copyCurriculumGroupsInformation(final CurriculumGroup source, final CurriculumGroup parent) {
	final CurriculumGroup destination;
	if (parent.hasChildDegreeModule(source.getDegreeModule())) {
	    destination = (CurriculumGroup) parent.getChildCurriculumModule(source.getDegreeModule());
	} else {
	    destination = new CurriculumGroup(parent, source.getDegreeModule());
	}

	for (final CurriculumModule curriculumModule : source.getCurriculumModulesSet()) {
	    if (curriculumModule.isLeaf()) {
		copyCurricumLineInformation((CurriculumLine) curriculumModule, destination);
	    } else {
		copyCurriculumGroupsInformation((CurriculumGroup) curriculumModule, destination);
	    }
	}
    }

    private void copyCurricumLineInformation(final CurriculumLine curriculumLine, final CurriculumGroup parent) {
	if (curriculumLine.isEnrolment()) {
	    final Enrolment enrolment = (Enrolment) curriculumLine;
	    if (enrolment.getExecutionPeriod().isAfterOrEquals(getExecutionPeriod())) {
		moveEnrolment(enrolment, parent);
	    } else if (enrolment.isApproved()) {
		createSubstitutionForEnrolment((Enrolment) curriculumLine, parent);
	    }
	} else if (curriculumLine.isDismissal()) {
	    createDismissal((Dismissal) curriculumLine, parent);
	} else {
	    throw new DomainException("error.unknown.curriculumLine");
	}
    }

    private void moveEnrolment(final Enrolment enrolment, final CurriculumGroup parent) {
	final CurriculumModule child = parent.getChildCurriculumModule(enrolment.getDegreeModule());
	if (child != null && child.isEnrolment()) {
	    throw new DomainException("error.AffinityCyclesManagement.enrolment.should.not.exist");
	}

	final Registration registration = parent.getStudentCurricularPlan().getRegistration();
	enrolment.setCurriculumGroup(parent);

	for (final Attends attends : enrolment.getAttends()) {
	    attends.setRegistration(registration);
	}
    }

    private void createSubstitutionForEnrolment(final Enrolment enrolment, final CurriculumGroup parent) {
	if (parent.hasChildDegreeModule(enrolment.getDegreeModule())) {
	    return;
	}

	final Substitution substitution = new Substitution();
	substitution.setStudentCurricularPlan(parent.getStudentCurricularPlan());
	substitution.setExecutionPeriod(getExecutionPeriod());
	EnrolmentWrapper.create(substitution, enrolment);

	if (enrolment.isOptional()) {
	    final OptionalEnrolment optional = (OptionalEnrolment) enrolment;
	    new OptionalDismissal(substitution, parent, optional.getOptionalCurricularCourse(), optional.getEctsCredits());
	} else {
	    new Dismissal(substitution, parent, enrolment.getCurricularCourse());
	}
    }

    private void createDismissal(final Dismissal dismissal, final CurriculumGroup parent) {
	if (curriculumGroupHasSimilarDismissal(parent, dismissal)) {
	    return;
	}

	final Credits credits = dismissal.getCredits();

	final Credits newCredits;
	if (credits.isTemporary()) {
	    newCredits = new TemporarySubstitution();

	} else if (credits.isSubstitution()) {
	    newCredits = new Substitution();

	} else if (credits.isEquivalence()) {
	    final Equivalence equivalence = (Equivalence) credits;
	    final Equivalence newEquivalence = new Equivalence();
	    equivalence.setGrade(equivalence.getGrade());
	    newCredits = newEquivalence;

	} else {
	    newCredits = new Credits();
	}

	newCredits.setStudentCurricularPlan(parent.getStudentCurricularPlan());
	newCredits.setExecutionPeriod(getExecutionPeriod());
	newCredits.setGivenCredits(credits.getGivenCredits());

	for (final IEnrolment enrolment : credits.getIEnrolments()) {
	    EnrolmentWrapper.create(newCredits, enrolment);
	}

	if (dismissal.hasCurricularCourse()) {
	    if (dismissal instanceof OptionalDismissal) {
		final OptionalDismissal optionalDismissal = (OptionalDismissal) dismissal;
		new OptionalDismissal(newCredits, parent, (OptionalCurricularCourse) optionalDismissal.getCurricularCourse(),
			optionalDismissal.getEctsCredits());
	    } else {
		new Dismissal(newCredits, parent, dismissal.getCurricularCourse());
	    }
	} else if (dismissal.isCreditsDismissal()) {
	    final CreditsDismissal creditsDismissal = (CreditsDismissal) dismissal;
	    new CreditsDismissal(newCredits, parent, creditsDismissal.getNoEnrolCurricularCourses());
	} else {
	    throw new DomainException("error.unknown.dismissal.type");
	}
    }

    private boolean curriculumGroupHasSimilarDismissal(final CurriculumGroup curriculumGroup, final Dismissal dismissal) {
	for (final Dismissal each : curriculumGroup.getChildDismissals()) {
	    if (each.isSimilar(dismissal)) {
		return true;
	    }
	}
	return false;
    }

    private void moveExtraCurriculumGroupInformation(final StudentCurricularPlan oldStudentCurricularPlan,
	    final StudentCurricularPlan newStudentCurricularPlan) {

	final ExtraCurriculumGroup oldExtraCurriculumGroup = oldStudentCurricularPlan.getExtraCurriculumGroup();
	if (oldExtraCurriculumGroup != null) {
	    final ExtraCurriculumGroup newExtraCurriculumGroup = newStudentCurricularPlan.getExtraCurriculumGroup();
	    if (newExtraCurriculumGroup == null) {
		throw new DomainException("error.invalid.newExtraCurriculumGroup");
	    }
	    for (final CurriculumModule curriculumModule : oldExtraCurriculumGroup.getCurriculumModulesSet()) {
		curriculumModule.setCurriculumGroup(newExtraCurriculumGroup);
	    }

	    for (final CurriculumLine curriculumLine : newExtraCurriculumGroup.getAllCurriculumLines()) {
		if (curriculumLine.isDismissal()) {
		    final Dismissal dismissal = (Dismissal) curriculumLine;
		    dismissal.getCredits().setStudentCurricularPlan(newStudentCurricularPlan);
		}
	    }
	}
    }

    private void tryRemoveOldSecondCycle(final CycleCurriculumGroup oldSecondCycle) {
	if (canRemoveOldSecondCycle(oldSecondCycle)) {
	    deleteCurriculumModules(oldSecondCycle);
	}
    }

    protected void deleteCurriculumModules(final CurriculumModule curriculumModule) {
	if (curriculumModule == null) {
	    return;
	}
	if (!curriculumModule.isLeaf()) {
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
	    for (; curriculumGroup.hasAnyCurriculumModules();) {
		deleteCurriculumModules(curriculumGroup.getCurriculumModules().get(0));
	    }
	    curriculumGroup.delete();
	} else if (curriculumModule.isDismissal()) {
	    curriculumModule.delete();
	} else {
	    throw new DomainException("error.can.only.remove.groups.and.dismissals");
	}
    }

    private boolean canRemoveOldSecondCycle(final CycleCurriculumGroup oldSecondCycle) {
	for (final CurriculumLine curriculumLine : oldSecondCycle.getAllCurriculumLines()) {
	    if (curriculumLine.isEnrolment()) {
		return false;
	    } else if (!curriculumLine.isDismissal()) {
		throw new DomainException("error.unknown.curriculum.line");
	    }
	}
	return true;
    }

    private void markOldRegistrationWithConcludedState(final StudentCurricularPlan oldStudentCurricularPlan) {
	if (oldStudentCurricularPlan.getRegistration().hasState(RegistrationStateType.CONCLUDED)) {
	    return;
	}
	final RegistrationState state = RegistrationState.createState(oldStudentCurricularPlan.getRegistration(), null,
		new DateTime(), RegistrationStateType.CONCLUDED);
	state.setResponsiblePerson(null);
    }

    protected ExecutionPeriod getExecutionPeriod() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    private ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }
}
