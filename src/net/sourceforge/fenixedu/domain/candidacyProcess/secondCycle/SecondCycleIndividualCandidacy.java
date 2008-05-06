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

public class SecondCycleIndividualCandidacy extends SecondCycleIndividualCandidacy_Base {

    private SecondCycleIndividualCandidacy() {
	super();
    }

    SecondCycleIndividualCandidacy(final SecondCycleIndividualCandidacyProcess process,
	    final SecondCycleIndividualCandidacyProcessBean bean) {
	this();

	final Person person = getPersonFromBean(bean);
	checkParameters(person, process, bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyProcess(process);
	setPerson(person);
	setSelectedDegree(bean.getSelectedDegree());
	setProfessionalStatus(bean.getProfessionalStatus());
	setOtherEducation(bean.getOtherEducation());
	setState(IndividualCandidacyState.STAND_BY);

	createPrecendentDegreeInformation(person, bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final SecondCycleIndividualCandidacyProcess process, final Degree degree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	if (person == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.person");
	}
	if (process == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.process");
	}
	checkParameters(person, degree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final Degree degree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {
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
    }

    private void checkRulesToCancel() {
	if (hasAnyPayment()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    Person getResponsiblePerson() {
	return Person.readPersonByUsername(getResponsible());
    }

    void editCandidacyInformation(final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation, final String professionalStatus,
	    final String otherEducation) {

	checkParameters(getPerson(), selectedDegree, precedentDegreeInformation);
	setSelectedDegree(selectedDegree);
	setProfessionalStatus(professionalStatus);
	setOtherEducation(otherEducation);
	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(precedentDegreeInformation);
	}
    }
}
