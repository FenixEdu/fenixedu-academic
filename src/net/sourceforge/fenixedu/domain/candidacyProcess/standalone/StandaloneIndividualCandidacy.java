package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.StandaloneIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.joda.time.LocalDate;

public class StandaloneIndividualCandidacy extends StandaloneIndividualCandidacy_Base {

    private StandaloneIndividualCandidacy() {
	super();
    }

    StandaloneIndividualCandidacy(final StandaloneIndividualCandidacyProcess process,
	    final StandaloneIndividualCandidacyProcessBean bean) {

	this();
	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate());
	init(person, process, bean.getCandidacyDate());
	addSelectedCurricularCourses(bean.getCurricularCourses());
	createDebt(person);
    }

    private void addSelectedCurricularCourses(final List<CurricularCourse> curricularCourses) {
	getCurricularCourses().addAll(curricularCourses);
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
	super.checkParameters(person, process, candidacyDate);

	if (person.hasValidStandaloneIndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.StandaloneIndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}
    }

    private void createDebt(final Person person) {
	new StandaloneIndividualCandidacyEvent(this, person);
    }

    public void editCandidacyInformation(final LocalDate candidacyDate, final List<CurricularCourse> curricularCourses) {
	super.checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);
	setCandidacyDate(candidacyDate);
	getCurricularCourses().clear();
	addSelectedCurricularCourses(curricularCourses);
    }

    public void editCandidacyResult(final StandaloneIndividualCandidacyResultBean bean) {
	checkParameters(bean);

	if (isCandidacyResultStateValid(bean.getState())) {
	    setState(bean.getState());
	}
    }

    private void checkParameters(final StandaloneIndividualCandidacyResultBean bean) {
	if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
	    throw new DomainException("error.StandaloneIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
	}
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (!degreeCurricularPlan.isEmpty()) {
	    throw new DomainException("error.StandaloneIndividualCandidacy.dcp.must.be.empty");
	}

	if (hasActiveRegistration(degreeCurricularPlan)) {
	    final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
	    setRegistration(registration);
	    enrolInCurricularCourses(registration);
	    return registration;
	}

	return createRegistration(getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    @Override
    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
	    final CycleType cycleType, final Ingression ingression) {

	final Registration registration = new Registration(person, degreeCurricularPlan);
	registration.setEntryPhase(EntryPhase.FIRST_PHASE_OBJ);
	registration.setIngression(ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval().getExecutionYear());

	setRegistration(registration);

	person.addPersonRoleByRoleType(RoleType.PERSON);
	person.addPersonRoleByRoleType(RoleType.STUDENT);

	enrolInCurricularCourses(registration);

	return registration;
    }

    private void enrolInCurricularCourses(final Registration registration) {
	final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
	    studentCurricularPlan.createNoCourseGroupCurriculumGroupEnrolment(curricularCourse, getCandidacyExecutionInterval(),
		    NoCourseGroupCurriculumGroupType.STANDALONE, AccessControl.getPerson());
	}
    }

    @Override
    protected ExecutionSemester getCandidacyExecutionInterval() {
	return (ExecutionSemester) super.getCandidacyExecutionInterval();
    }

}
