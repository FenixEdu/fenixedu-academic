package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeCandidacyForGraduatedPersonEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPerson extends DegreeCandidacyForGraduatedPerson_Base {

    private DegreeCandidacyForGraduatedPerson() {
	super();
    }

    DegreeCandidacyForGraduatedPerson(final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {

	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);
	createDebt();
    }

    private void checkParameters(final Person person, final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (person.hasValidDegreeCandidacyForGraduatedPerson(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	if (selectedDegree == null || personHasDegree(person, selectedDegree)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degrees");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

    private boolean personHasDegree(final Person person, final Degree selectedDegree) {
	return person.hasStudent() ? person.getStudent().hasRegistrationFor(selectedDegree) : false;
    }

    private void createDebt() {
	new DegreeCandidacyForGraduatedPersonEvent(this, getPerson());
    }

    @Override
    public DegreeCandidacyForGraduatedPersonIndividualProcess getCandidacyProcess() {
	return (DegreeCandidacyForGraduatedPersonIndividualProcess) super.getCandidacyProcess();
    }

    public void editCandidacyInformation(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyDate(bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());
	
	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(bean.getPrecedentDegreeInformation());
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);
	if (selectedDegree == null || personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degrees");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

}
