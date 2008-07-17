package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

public class DegreeChangeIndividualCandidacy extends DegreeChangeIndividualCandidacy_Base {

    private DegreeChangeIndividualCandidacy() {
	super();
    }

    DegreeChangeIndividualCandidacy(final DegreeChangeIndividualCandidacyProcess process,
	    final DegreeChangeIndividualCandidacyProcessBean bean) {
	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final DegreeChangeIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (person.hasValidDegreeChangeIndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(person, selectedDegree)) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getName());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected ExternalPrecedentDegreeInformation createExternalPrecedentDegreeInformation(
	    final CandidacyPrecedentDegreeInformationBean bean) {
	final ExternalPrecedentDegreeInformation information = super.createExternalPrecedentDegreeInformation(bean);
	information.init(bean.getNumberOfApprovedCurricularCourses(), bean.getGradeSum(), bean.getApprovedEcts(), bean
		.getEnroledEcts());
	return information;
    }

    private void createDebt(final Person person) {
	new DegreeChangeIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeChangeIndividualCandidacyProcess getCandidacyProcess() {
	return (DegreeChangeIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyDate(bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(bean.getPrecedentDegreeInformation());
	    getPrecedentDegreeInformation().editCurricularCoursesInformation(bean.getPrecedentDegreeInformation());
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getName());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

}
