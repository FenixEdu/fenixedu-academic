package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.YearMonthDay;

public class SecondCycleIndividualCandidacy extends SecondCycleIndividualCandidacy_Base {

    private SecondCycleIndividualCandidacy() {
	super();
    }

    SecondCycleIndividualCandidacy(final SecondCycleIndividualCandidacyProcess process,
	    final SecondCycleIndividualCandidacyProcessBean bean) {
	this();

	final Person person = getPersonFromBean(bean);
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyProcess(process);
	setPerson(person);
	setSelectedDegree(bean.getSelectedDegree());
	setProfessionalStatus(bean.getProfessionalStatus());
	setOtherEducation(bean.getOtherEducation());
	setState(IndividualCandidacyState.STAND_BY);
	setCandidacyDate(bean.getCandidacyDate());

	createPrecendentDegreeInformation(person, bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final SecondCycleIndividualCandidacyProcess process,
	    final YearMonthDay candidacyDate, final Degree degree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	if (person == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.person");
	}
	if (process == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.process");
	}
	if (candidacyDate == null || !process.hasOpenCandidacyPeriod(candidacyDate.toDateTimeAtMidnight())) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.candidacyDate", process.getCandidacyStart()
		    .toString(DateFormatUtil.DEFAULT_DATE_FORMAT), process.getCandidacyEnd().toString(
		    DateFormatUtil.DEFAULT_DATE_FORMAT));
	}
	if (person.hasValidSecondCycleIndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}
	if (degree == null || personHasDegree(person, degree)) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degrees");
	}
	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    private boolean personHasDegree(final Person person, final Degree degree) {
	return person.hasStudent() ? person.getStudent().hasRegistrationFor(degree) : false;
    }

    private Person getPersonFromBean(final SecondCycleIndividualCandidacyProcessBean bean) {
	if (bean.getPersonBean().hasPerson()) {
	    return bean.getPersonBean().getPerson().edit(bean.getPersonBean());
	} else {
	    return new Person(bean.getPersonBean());
	}
    }

    private void createPrecendentDegreeInformation(final Person person,
	    final SecondCycleIndividualCandidacyProcessBean processBean) {

	final CandidacyPrecedentDegreeInformationBean bean = processBean.getPrecedentDegreeInformation();
	if (processBean.isExternalPrecedentDegreeType()) {
	    new ExternalPrecedentDegreeInformation(this, bean.getDegreeDesignation(), bean.getConclusionDate(), bean
		    .getInstitution(), bean.getConclusionGrade());
	} else {
	    final StudentCurricularPlan studentCurricularPlan = processBean.getPrecedentStudentCurricularPlan();
	    if (studentCurricularPlan == null) {
		throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	    }
	    if (studentCurricularPlan.isBolonhaDegree()) {
		new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan, CycleType.FIRST_CYCLE);
	    } else {
		new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan);
	    }
	}
    }

    private void createDebt(final Person person) {
	new SecondCycleIndividualCandidacyEvent(this, person);
    }

    public void cancel(final Person person) {
	checkRulesToCancel();
	setState(IndividualCandidacyState.CANCELLED);
	setResponsible(person.getUsername());
	getEvent().cancel("SecondCycleIndividualCandidacy.canceled");
    }

    private void checkRulesToCancel() {
	if (hasAnyPayment()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    Person getResponsiblePerson() {
	return Person.readPersonByUsername(getResponsible());
    }

    @Override
    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
	return (SecondCycleIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final YearMonthDay candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation, final String professionalStatus,
	    final String otherEducation) {

	checkParameters(candidacyDate, selectedDegree, precedentDegreeInformation);
	setCandidacyDate(candidacyDate);
	setSelectedDegree(selectedDegree);
	setProfessionalStatus(professionalStatus);
	setOtherEducation(otherEducation);
	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(precedentDegreeInformation);
	}
    }

    private void checkParameters(final YearMonthDay candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	if (candidacyDate == null || !getCandidacyProcess().hasOpenCandidacyPeriod(candidacyDate.toDateTimeAtMidnight())) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.candidacyDate", getCandidacyProcess()
		    .getCandidacyStart().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getCandidacyProcess().getCandidacyEnd()
		    .toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	}
	if (selectedDegree == null || personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}
	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    void editCandidacyResult(final SecondCycleIndividualCandidacyResultBean bean) {
	checkEditParameters(bean);
	setProfessionalExperience(bean.getProfessionalExperience());
	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setCandidacyGrade(bean.getGrade());
	setInterviewGrade(bean.getInterviewGrade());
	setSeriesCandidacyGrade(bean.getSeriesGrade());
	setState(bean.getState());
	setNotes(bean.getNotes());
    }

    private void checkEditParameters(final SecondCycleIndividualCandidacyResultBean bean) {
	if (bean.getProfessionalExperience() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.profissionalExperience");
	}
	if (bean.getAffinity() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.affinity");
	}
	if (bean.getDegreeNature() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degreeNature");
	}
	if (bean.getGrade() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.grade");
	}
	if (bean.getInterviewGrade() == null || bean.getInterviewGrade().length() == 0) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.interview.value");
	}
	if (bean.getSeriesGrade() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.series.grade");
	}
	if (bean.getState() == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.state");
	}
    }
}
