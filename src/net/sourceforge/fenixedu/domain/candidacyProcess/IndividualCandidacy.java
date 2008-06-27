package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

    protected IndividualCandidacy() {
	super();
	super.setWhenCreated(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
	setPerson(person);
	setCandidacyProcess(process);
	setCandidacyDate(candidacyDate);
	setState(IndividualCandidacyState.STAND_BY);
    }

    protected void checkParameters(final Person person, final IndividualCandidacyProcess process, final LocalDate candidacyDate) {
	if (person == null) {
	    throw new DomainException("error.IndividualCandidacy.invalid.person");
	}
	if (process == null) {
	    throw new DomainException("error.IndividualCandidacy.invalid.process");
	}
	if (candidacyDate == null || !process.hasOpenCandidacyPeriod(candidacyDate.toDateTimeAtStartOfDay())) {
	    throw new DomainException("error.IndividualCandidacy.invalid.candidacyDate", process.getCandidacyStart().toString(
		    DateFormatUtil.DEFAULT_DATE_FORMAT), process.getCandidacyEnd().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	}
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.IndividualCandidacy.cannot.modify.when.created");
    }

    public boolean hasAnyPayment() {
	return hasEvent() && getEvent().hasAnyPayments();
    }

    public void editPersonalCandidacyInformation(final PersonBean personBean) {
	getPerson().edit(personBean);
    }

    public void cancel(final Person person) {
	checkRulesToCancel();
	setState(IndividualCandidacyState.CANCELLED);
	setResponsible(person.getUsername());
	if (hasEvent()) {
	    getEvent().cancel("IndividualCandidacy.canceled");
	}
    }

    protected void checkRulesToCancel() {
	if (hasEvent() && hasAnyPayment()) {
	    throw new DomainException("error.IndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    public Person getResponsiblePerson() {
	return Person.readPersonByUsername(getResponsible());
    }

    public boolean isInStandBy() {
	return getState() == IndividualCandidacyState.STAND_BY;
    }

    public boolean isAccepted() {
	return getState() == IndividualCandidacyState.ACCEPTED;
    }

    public boolean isCancelled() {
	return getState() == IndividualCandidacyState.CANCELLED;
    }

    public boolean isRejected() {
	return getState() == IndividualCandidacyState.REJECTED;
    }

    public boolean isDebtPayed() {
	return hasEvent() && getEvent().isClosed();
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return hasCandidacyProcess() && getCandidacyProcess().isFor(executionInterval);
    }

    protected void createPrecedentDegreeInformation(final IndividualCandidacyProcessWithPrecedentDegreeInformationBean processBean) {
	final CandidacyPrecedentDegreeInformationBean bean = processBean.getPrecedentDegreeInformation();
	if (processBean.isExternalPrecedentDegreeType()) {
	    createExternalPrecedentDegreeInformation(bean);
	} else {
	    final StudentCurricularPlan studentCurricularPlan = processBean.getPrecedentStudentCurricularPlan();
	    if (studentCurricularPlan == null) {
		throw new DomainException("error.IndividualCandidacy.invalid.precedentDegreeInformation");
	    }
	    createInstitutionPrecedentDegreeInformation(studentCurricularPlan);
	}
    }

    protected void createExternalPrecedentDegreeInformation(final CandidacyPrecedentDegreeInformationBean bean) {
	new ExternalPrecedentDegreeInformation(this, bean.getDegreeDesignation(), bean.getConclusionDate(),
		bean.getInstitution(), bean.getConclusionGrade());
    }

    protected void createInstitutionPrecedentDegreeInformation(final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.isBolonhaDegree()) {
	    final CycleType cycleType;
	    if (studentCurricularPlan.hasConcludedAnyInternalCycle()) {
		cycleType = studentCurricularPlan.getLastConcludedCycleCurriculumGroup().getCycleType();
	    } else {
		cycleType = studentCurricularPlan.getLastOrderedCycleCurriculumGroup().getCycleType();
	    }
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan, cycleType);
	} else {
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan);
	}
    }

    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (hasRegistration(degreeCurricularPlan)) {
	    final Registration registration = getStudent().getActiveRegistrationFor(degreeCurricularPlan);
	    if (registration.getStartDate().isBefore(getCandidacyDate())) {
		throw new DomainException("error.IndividualCandidacy.person.with.registration.previous.candidacy",
			degreeCurricularPlan.getPresentationName());
	    }
	    setRegistration(registration);
	    return registration;
	}

	final Registration registration = new Registration(getPerson(), degreeCurricularPlan, cycleType);
	registration.setEntryPhase(EntryPhase.FIRST_PHASE_OBJ);
	registration.setIngression(ingression);
	setRegistration(registration);
	return registration;
    }

    private boolean hasRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	return getPerson().hasStudent() && getPerson().getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Student getStudent() {
	return getPerson().hasStudent() ? getPerson().getStudent() : null;
    }
}
