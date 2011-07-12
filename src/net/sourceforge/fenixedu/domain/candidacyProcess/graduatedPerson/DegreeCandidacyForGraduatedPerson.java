package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeCandidacyForGraduatedPersonEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeCandidacyForGraduatedPerson extends DegreeCandidacyForGraduatedPerson_Base {

    private DegreeCandidacyForGraduatedPerson() {
	super();
    }

    DegreeCandidacyForGraduatedPerson(final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	this();

	Person person = init(bean, process);
	setSelectedDegree(bean.getSelectedDegree());
	createPrecedentDegreeInformation(bean);

	createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());

	/*
	 * 06/04/2009 - The candidacy may not be associated with a person. In this case we will not create an Event
	 */
	if (bean.getInternalPersonCandidacy()) {
	    createDebt(person);
	}
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	DegreeCandidacyForGraduatedPersonIndividualProcess degreeCandidacyProcess = (DegreeCandidacyForGraduatedPersonIndividualProcess) process;
	DegreeCandidacyForGraduatedPersonIndividualProcessBean degreeCandidacyBean = (DegreeCandidacyForGraduatedPersonIndividualProcessBean) bean;

	LocalDate candidacyDate = degreeCandidacyBean.getCandidacyDate();
	Degree selectedDegree = degreeCandidacyBean.getSelectedDegree();
	CandidacyPrecedentDegreeInformationBean precedentDegreeInformation = degreeCandidacyBean.getPrecedentDegreeInformation();

	checkParameters(person, degreeCandidacyProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	/*
	 * 31/03/2009 - The candidacy will not be associated with a Person if it is submited externally (not in administrative office)
	 * 
	 * if (person == null) { throw new DomainException("error.IndividualCandidacy.invalid.person"); }
	 * 
	 * if(person.hasValidDegreeCandidacyForGraduatedPerson(process. getCandidacyExecutionInterval())) { throw newDomainException(
	 * "error.DegreeCandidacyForGraduatedPerson.person.already.has.candidacy" , process
	 * .getCandidacyExecutionInterval().getName()); }
	 */

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degree");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected void createDebt(final Person person) {
	new DegreeCandidacyForGraduatedPersonEvent(this, person);
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

	checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);
	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degree");
	}

	if (hasRegistration() && getRegistration().getDegree() != selectedDegree) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.cannot.change.degree");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

    void editCandidacyResult(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
	checkParameters(bean);

	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setCandidacyGrade(bean.getGrade());

	if (isCandidacyResultStateValid(bean.getState())) {
	    setState(bean.getState());
	}
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
	if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.cannot.change.state.from.accepted.candidacies");
	}
    }

    void editSelectedDegree(final Degree selectedDegree) {
	setSelectedDegree(selectedDegree);
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
	    Ingression ingression) {
	final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval());
	return registration;
    }

    @Override
    public void exportValues(StringBuilder result) {
	super.exportValues(result);

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());
	final ResourceBundle candidateBundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	Formatter formatter = new Formatter(result);

	formatter.format("%s: %s\n", candidateBundle.getString("label.process.id"), getCandidacyProcess().getProcessCode());
	CandidacyPrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess()
		.getCandidacyPrecedentDegreeInformation();
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.previous.degree"),
		precedentDegreeInformation.getDegreeDesignation());
	formatter.format("%s: %s\n", bundle.getString("label.conclusionDate"), precedentDegreeInformation.getConclusionDate());
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.institution"),
		precedentDegreeInformation.getInstitution().getName());
	formatter.format("%s: %s\n", bundle.getString("label.conclusionGrade"), precedentDegreeInformation.getConclusionGrade());
	formatter.format("\n");
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.affinity"),
		getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
	formatter.format("%s: %d\n", bundle.getString("label.SecondCycleIndividualCandidacy.degreeNature"),
		getDegreeNature() != null ? getDegreeNature() : 0);
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.candidacyGrade"),
		getCandidacyGrade() != null ? getCandidacyGrade() : BigDecimal.ZERO);
    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

}
