package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

public class SecondCycleIndividualCandidacy extends SecondCycleIndividualCandidacy_Base {

    private SecondCycleIndividualCandidacy() {
	super();
    }

    SecondCycleIndividualCandidacy(final SecondCycleIndividualCandidacyProcess process,
	    final SecondCycleIndividualCandidacyProcessBean bean) {
	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());

	setSelectedDegree(bean.getSelectedDegree());
	setProfessionalStatus(bean.getProfessionalStatus());
	setOtherEducation(bean.getOtherEducation());

	createPrecedentDegreeInformation(bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final SecondCycleIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree degree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (person.hasValidSecondCycleIndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	if (degree == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected void createInstitutionPrecedentDegreeInformation(StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.isBolonhaDegree()) {
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan, CycleType.FIRST_CYCLE);
	} else {
	    new InstitutionPrecedentDegreeInformation(this, studentCurricularPlan);
	}
    }

    private void createDebt(final Person person) {
	new SecondCycleIndividualCandidacyEvent(this, person);
    }

    @Override
    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
	return (SecondCycleIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final LocalDate candidacyDate, final Degree selectedDegree,
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

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
		    getCandidacyExecutionInterval()).getContent());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    void editCandidacyResult(final SecondCycleIndividualCandidacyResultBean bean) {

	setProfessionalExperience(bean.getProfessionalExperience());
	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setCandidacyGrade(bean.getGrade());
	setInterviewGrade(bean.getInterviewGrade());
	setSeriesCandidacyGrade(bean.getSeriesGrade());
	setNotes(bean.getNotes());

	if (bean.getState() == null) {
	    setState(IndividualCandidacyState.STAND_BY);
	    removeRegistration();
	} else {
	    setState(bean.getState());
	}
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (hasRegistration(degreeCurricularPlan)) {
	    final Registration registration = getRegistration(degreeCurricularPlan);
	    setRegistration(registration);
	    return registration;
	}

	return createRegistration(getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    private boolean hasRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	return hasStudent() && getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Registration getRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	final List<Registration> registrations = getStudent().getRegistrationsFor(degreeCurricularPlan);
	Collections.sort(registrations, Registration.COMPARATOR_BY_START_DATE);

	Registration result = null;
	for (final Registration registration : registrations) {
	    if (result == null || registration.hasAnyActiveState(getCandidacyExecutionInterval())) {
		result = registration;
	    }
	}
	return result;
    }

    @Override
    protected Registration createRegistration(final Person person, final DegreeCurricularPlan degreeCurricularPlan,
	    final CycleType cycleType, final Ingression ingression) {
	final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval());
	return registration;
    }

}
