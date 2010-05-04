package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ErasmusCandidacyProcess extends ErasmusCandidacyProcess_Base {

    static final public Comparator<IndividualCandidacyProcess> COMPARATOR_BY_CANDIDACY_PERSON = new Comparator<IndividualCandidacyProcess>() {
	public int compare(IndividualCandidacyProcess o1, IndividualCandidacyProcess o2) {
	    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(o1.getPersonalDetails(), o2
		    .getPersonalDetails());
	}
    };

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new ViewErasmusVancacies());
    }

    public ErasmusCandidacyProcess() {
	super();
    }

    private ErasmusCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new ErasmusCandidacyPeriod(this, executionYear, start, end);
    }

    public List<ErasmusIndividualCandidacyProcess> getValidErasmusIndividualCandidacies() {
	final List<ErasmusIndividualCandidacyProcess> result = new ArrayList<ErasmusIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid()) {
		result.add(process);
	    }
	}
	return result;
    }

    public List<ErasmusIndividualCandidacyProcess> getValidErasmusIndividualCandidacies(final Degree degree) {
	if (degree == null) {
	    return Collections.emptyList();
	}
	final List<ErasmusIndividualCandidacyProcess> result = new ArrayList<ErasmusIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid() && process.hasCandidacyForSelectedDegree(degree)) {
		result.add(process);
	    }
	}
	return result;
    }

    public Map<Degree, SortedSet<ErasmusIndividualCandidacyProcess>> getValidErasmusIndividualCandidaciesByDegree() {
	final Map<Degree, SortedSet<ErasmusIndividualCandidacyProcess>> result = new TreeMap<Degree, SortedSet<ErasmusIndividualCandidacyProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid()) {
		SortedSet<ErasmusIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
		if (values == null) {
		    result.put(process.getCandidacySelectedDegree(), values = new TreeSet<ErasmusIndividualCandidacyProcess>(
			    SecondCycleIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON));
		}
		values.add(process);
	    }
	}
	return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> result,
	    final SecondCycleIndividualCandidacyProcess process) {
	SortedSet<SecondCycleIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
	if (values == null) {
	    result.put(process.getCandidacySelectedDegree(), values = new TreeSet<SecondCycleIndividualCandidacyProcess>(
		    SecondCycleIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON));
	}
	values.add(process);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.interval");
	}
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.INTERNATIONAL_RELATION_OFFICE)
		|| userView.hasRoleType(RoleType.COORDINATOR);
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    static private boolean isGriOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.INTERNATIONAL_RELATION_OFFICE);
    }

    static private boolean isManager(IUserView userView) {
	return userView.hasRoleType(RoleType.MANAGER);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources", Language.getLocale()).getString(
		getClass().getSimpleName());
    }

    public ErasmusIndividualCandidacyProcess getProcessByEIdentifier(String eIdentifier) {
	List<ErasmusIndividualCandidacyProcess> childProcesses = new java.util.ArrayList<ErasmusIndividualCandidacyProcess>(
		(List) this.getChildProcesses());

	for (ErasmusIndividualCandidacyProcess process : childProcesses) {
	    if (eIdentifier.equals(process.getPersonalDetails().getPerson().getEidentifier())) {
		return process;
	    }
	}

	return null;
    }

    public ErasmusCandidacyPeriod getCandidacyPeriod() {
	return (ErasmusCandidacyPeriod) super.getCandidacyPeriod();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<ErasmusCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusCandidacyProcess executeActivity(ErasmusCandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new ErasmusCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<ErasmusCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusCandidacyProcess executeActivity(ErasmusCandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    private static class ViewErasmusVancacies extends Activity<ErasmusCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusCandidacyProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusCandidacyProcess executeActivity(ErasmusCandidacyProcess process, IUserView userView, Object object) {
	    return process;
	}
    }

    static private class InsertErasmusVacancy extends Activity<ErasmusCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusCandidacyProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected ErasmusCandidacyProcess executeActivity(ErasmusCandidacyProcess process, IUserView userView, Object object) {
	    ErasmusVacancyBean bean = (ErasmusVacancyBean) object;

	    new ErasmusVacancy(process.getCandidacyPeriod(), bean.getDegree(), bean.getUniversity(), bean.getNumberOfVacancies());

	    return process;
	}

    }
}
