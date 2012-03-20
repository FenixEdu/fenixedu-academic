package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SecondCycleIndividualCandidacy extends SecondCycleIndividualCandidacy_Base {

    private SecondCycleIndividualCandidacy() {
	super();
    }

    SecondCycleIndividualCandidacy(final SecondCycleIndividualCandidacyProcess process,
	    final SecondCycleIndividualCandidacyProcessBean bean) {
	this();

	Person person = init(bean, process);

	getSelectedDegrees().addAll(bean.getSelectedDegreeList());

	setProfessionalStatus(bean.getProfessionalStatus());
	setOtherEducation(bean.getOtherEducation());

	createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());

	editFormerIstStudentNumber(bean);

	/*
	 * 06/04/2009 - The candidacy may not be associated with a person. In
	 * this case we will not create an Event
	 */
	if (bean.getInternalPersonCandidacy()) {
	    createDebt(person);
	}

	if (getSelectedDegrees().isEmpty()) {
	    throw new DomainException("This shouldnt happen");
	}
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	SecondCycleIndividualCandidacyProcess secondCycleIndividualCandidacyProcess = (SecondCycleIndividualCandidacyProcess) process;
	SecondCycleIndividualCandidacyProcessBean secondCandidacyProcessBean = (SecondCycleIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = secondCandidacyProcessBean
		.getPrecedentDegreeInformation();

	checkParameters(person, secondCycleIndividualCandidacyProcess, candidacyDate,
		secondCandidacyProcessBean.getSelectedDegreeList(), precedentDegreeInformationBean);

    }

    private void checkParameters(final Person person, final SecondCycleIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Set<Degree> degrees,
	    final PrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not
	 * be associated to a person
	 * 
	 * 
	 * if(person.hasValidSecondCycleIndividualCandidacy(process.
	 * getCandidacyExecutionInterval())) { throw newDomainException(
	 * "error.SecondCycleIndividualCandidacy.person.already.has.candidacy",
	 * process .getCandidacyExecutionInterval().getName()); }
	 */

	if (degrees.isEmpty()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degrees.selection");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }
    @Override
    protected void createDebt(final Person person) {
	new SecondCycleIndividualCandidacyEvent(this, person);
    }

    @Override
    public SecondCycleIndividualCandidacyProcess getCandidacyProcess() {
	return (SecondCycleIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final LocalDate candidacyDate, final Set<Degree> selectedDegrees,
	    final PrecedentDegreeInformationBean precedentDegreeInformation, final String professionalStatus,
	    final String otherEducation) {

	checkParameters(candidacyDate, selectedDegrees, precedentDegreeInformation);
	setCandidacyDate(candidacyDate);

	putSelectedDegrees(selectedDegrees);

	setProfessionalStatus(professionalStatus);
	setOtherEducation(otherEducation);
    }

    private void putSelectedDegrees(final Set<Degree> selectedDegrees) {
	while (!getSelectedDegrees().isEmpty()) {
	    removeSelectedDegrees(getSelectedDegrees().iterator().next());
	}

	getSelectedDegrees().addAll(selectedDegrees);

	if (getSelectedDegrees().isEmpty()) {
	    throw new DomainException("this shouldnt happen");
	}
    }

    void editSelectedDegrees(final Set<Degree> selectedDegreeList) {
	while (!getSelectedDegrees().isEmpty()) {
	    getSelectedDegrees().remove(getSelectedDegrees().iterator().next());
	}

	getSelectedDegrees().addAll(selectedDegreeList);

	if (getSelectedDegrees().isEmpty()) {
	    throw new DomainException("this shouldnt happen");
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Set<Degree> selectedDegrees,
	    final PrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegrees == null || selectedDegrees.isEmpty()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}

	if (isCandidacyInternal()) {
	    if (personHasOneOfDegrees(getPersonalDetails().getPerson(), selectedDegrees)) {
		throw new DomainException("error.SecondCycleIndividualCandidacy.existing.degree");
	    }
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
	    throw new DomainException("error.IndividualCandidacy.person.with.registration",
		    degreeCurricularPlan.getPresentationName());
	}

	if (hasRegistration(degreeCurricularPlan)) {
	    final Registration registration = getRegistration(degreeCurricularPlan);
	    setRegistration(registration);
	    return registration;
	}

	getPersonalDetails().ensurePersonInternalization();
	return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
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

    void editFormerIstStudentNumber(SecondCycleIndividualCandidacyProcessBean bean) {
	this.setFormerStudentNumber(bean.getIstStudentNumber());
    }

    @Override
    public void exportValues(StringBuilder result) {
	super.exportValues(result);

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());
	final ResourceBundle candidateBundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	Formatter formatter = new Formatter(result);

	formatter.format("%s: %s\n", candidateBundle.getString("label.process.id"), getCandidacyProcess().getProcessCode());
	PrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess().getPrecedentDegreeInformation();
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.previous.degree"),
		precedentDegreeInformation.getDegreeDesignation());
	formatter.format("%s: %s\n", bundle.getString("label.conclusionDate"), precedentDegreeInformation.getConclusionDate());
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.institution"),
		precedentDegreeInformation.getInstitution().getName());
	formatter.format("%s: %s\n", bundle.getString("label.conclusionGrade"), precedentDegreeInformation.getConclusionGrade());
	formatter.format("\n");
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.professionalStatus"),
		StringUtils.isEmpty(getProfessionalStatus()) ? StringUtils.EMPTY : getProfessionalStatus());
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.otherEducation"),
		StringUtils.isEmpty(getOtherEducation()) ? StringUtils.EMPTY : getOtherEducation());
	formatter.format("%s: %d\n", bundle.getString("label.SecondCycleIndividualCandidacy.professionalExperience"),
		getProfessionalExperience() != null ? getProfessionalExperience() : 0);
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.affinity"),
		getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
	formatter.format("%s: %d\n", bundle.getString("label.SecondCycleIndividualCandidacy.degreeNature"),
		getDegreeNature() != null ? getDegreeNature() : 0);
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.candidacyGrade"),
		getCandidacyGrade() != null ? getCandidacyGrade() : BigDecimal.ZERO);
	formatter
		.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.interviewGrade"), getInterviewGrade());
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.seriesCandidacyGrade"),
		getSeriesCandidacyGrade() != null ? getSeriesCandidacyGrade() : BigDecimal.ZERO);

    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    @Override
    public boolean hasSelectedDegree() {
	throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    @Override
    public void setSelectedDegree(Degree selectedDegree) {
	throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    @Override
    public void removeSelectedDegree() {
	throw new DomainException("error.second.cycle.individual.candidacy.relation.with.degree.obsolete");
    }

    public boolean isSecondCycle() {
	return true;
    }

}
