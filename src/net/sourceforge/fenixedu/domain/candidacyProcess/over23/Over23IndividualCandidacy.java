package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.Over23IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.Formation;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Over23IndividualCandidacy extends Over23IndividualCandidacy_Base {

    private Over23IndividualCandidacy() {
	super();
    }

    Over23IndividualCandidacy(final Over23IndividualCandidacyProcess process, final Over23IndividualCandidacyProcessBean bean) {
	this();

	Person person = init(bean, process);

	setDisabilities(bean.getDisabilities());
	setEducation(bean.getEducation());
	setLanguages(bean.getLanguages());
	setLanguagesRead(bean.getLanguagesRead());
	setLanguagesWrite(bean.getLanguagesWrite());
	setLanguagesSpeak(bean.getLanguagesSpeak());

	createDegreeEntries(bean.getSelectedDegrees());

	createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());

	/*
	 * 06/04/2009 - The candidacy may not be associated with a person. In this case we will not create an Event
	 */

	/*
	 * 08/05/2009 - Now all candidacies are external (even made in academic administrative office)
	 * 
	 * TODO Anil : Are other candidacies created as an external
	 */
	if (bean.getInternalPersonCandidacy()) {
	    createDebt(person);
	}
    }

    protected void createFormationEntries(List<FormationBean> formationConcludedBeanList,
	    List<FormationBean> formationNonConcludedBeanList) {
	for (FormationBean formation : formationConcludedBeanList) {
	    this.addFormations(new Formation(this, formation));
	}

	for (FormationBean formation : formationNonConcludedBeanList) {
	    this.addFormations(new Formation(this, formation));
	}
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	Over23IndividualCandidacyProcess over23Process = (Over23IndividualCandidacyProcess) process;
	Over23IndividualCandidacyProcessBean over23ProcessBean = (Over23IndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();
	List<Degree> degrees = over23ProcessBean.getSelectedDegrees();

	checkParameters(person, over23Process, candidacyDate, degrees);
    }

    private void checkParameters(final Person person, final Over23IndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final List<Degree> degrees) {

	checkParameters(person, process, candidacyDate);

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not be associated to a person
	 * 
	 * 
	 * if (person.hasStudent()) { throw new DomainException("error.Over23IndividualCandidacy.invalid.person"); }
	 * 
	 * if(person.hasValidOver23IndividualCandidacy(process. getCandidacyExecutionInterval())) { throw newDomainException(
	 * "error.Over23IndividualCandidacy.person.already.has.candidacy", process .getCandidacyExecutionInterval().getName()); }
	 */

	/*
	 * 08/05/2009 - The candidacy process may be created with candidate personal information only. So we will not check the chosen
	 * degrees in initialisation
	 * 
	 * checkDegrees(degrees);
	 */
    }

    private void checkDegrees(final List<Degree> degrees) {
	if (degrees == null || degrees.isEmpty()) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.degrees");
	}
    }

    private void createDegreeEntries(final List<Degree> degrees) {
	for (int index = 0; index < degrees.size(); index++) {
	    new Over23IndividualCandidacyDegreeEntry(this, degrees.get(index), index + 1);
	}
    }

    private void removeExistingDegreeEntries() {
	while (hasAnyOver23IndividualCandidacyDegreeEntries()) {
	    getOver23IndividualCandidacyDegreeEntries().get(0).delete();
	}
    }

    @Override
    protected void createDebt(final Person person) {
	new Over23IndividualCandidacyEvent(this, person);
    }

    void saveChoosedDegrees(final List<Degree> degrees) {
	if (!degrees.isEmpty()) {
	    removeExistingDegreeEntries();
	    createDegreeEntries(degrees);
	}
    }

    @Override
    public Over23IndividualCandidacyProcess getCandidacyProcess() {
	return (Over23IndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final LocalDate candidacyDate, final List<Degree> degrees, final String disabilities,
	    final String education, final String languagesRead, final String languagesWrite, final String languagesSpeak) {

	checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);
	checkDegrees(degrees);

	setCandidacyDate(candidacyDate);
	saveChoosedDegrees(degrees);
	setDisabilities(disabilities);
	setEducation(education);
	setLanguagesRead(languagesRead);
	setLanguagesSpeak(languagesSpeak);
	setLanguagesWrite(languagesWrite);
    }

    List<Degree> getSelectedDegrees() {
	final List<Degree> result = new ArrayList<Degree>(getOver23IndividualCandidacyDegreeEntriesCount());
	for (final Over23IndividualCandidacyDegreeEntry entry : getOver23IndividualCandidacyDegreeEntries()) {
	    result.add(entry.getDegree());
	}
	return result;
    }

    List<Degree> getSelectedDegreesSortedByOrder() {
	final Set<Over23IndividualCandidacyDegreeEntry> entries = new TreeSet<Over23IndividualCandidacyDegreeEntry>(
		Over23IndividualCandidacyDegreeEntry.COMPARATOR_BY_ORDER);
	entries.addAll(getOver23IndividualCandidacyDegreeEntries());

	final List<Degree> result = new ArrayList<Degree>(entries.size());
	for (final Over23IndividualCandidacyDegreeEntry entry : entries) {
	    result.add(entry.getDegree());
	}
	return result;
    }

    void editCandidacyResult(final IndividualCandidacyState state, final Degree acceptedDegree) {
	checkParameters(state, acceptedDegree);
	setAcceptedDegree(acceptedDegree);
	if (isCandidacyResultStateValid(state)) {
	    setState(state);
	}
    }

    private void checkParameters(final IndividualCandidacyState state, final Degree acceptedDegree) {
	if (state != null) {
	    if (state == IndividualCandidacyState.ACCEPTED
		    && (acceptedDegree == null || !getSelectedDegrees().contains(acceptedDegree))) {
		throw new DomainException("error.Over23IndividualCandidacy.invalid.acceptedDegree");
	    }

	    if (isAccepted() && state != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
		throw new DomainException("error.Over23IndividualCandidacy.cannot.change.state.from.accepted.candidacies");
	    }
	}
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

	final ResourceBundle candidateBundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	Formatter formatter = new Formatter(result);
	formatter.format("%s: %s\n", candidateBundle.getString("label.over23.languages.read"), StringUtils
		.isEmpty(getLanguagesRead()) ? StringUtils.EMPTY : getLanguagesRead());
	formatter.format("%s: %s\n", candidateBundle.getString("label.over23.languages.write"), StringUtils
		.isEmpty(getLanguagesWrite()) ? StringUtils.EMPTY : getLanguagesWrite());
	formatter.format("%s: %s\n", candidateBundle.getString("label.over23.languages.speak"), StringUtils
		.isEmpty(getLanguagesSpeak()) ? StringUtils.EMPTY : getLanguagesSpeak());
    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasAcceptedDegree() ? ": " + getAcceptedDegree().getNameI18N() : "");
    }

    public boolean isOver23() {
	return true;
    }

}
