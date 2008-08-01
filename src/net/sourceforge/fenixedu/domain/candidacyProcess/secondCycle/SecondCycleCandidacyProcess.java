package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

public class SecondCycleCandidacyProcess extends SecondCycleCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendToCoordinator());
	activities.add(new PrintCandidacies());
	activities.add(new IntroduceCandidacyResults());
	activities.add(new SendToScientificCouncil());
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    private SecondCycleCandidacyProcess() {
	super();
    }

    private SecondCycleCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new SecondCycleCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)
		|| userView.hasRoleType(RoleType.COORDINATOR);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    public List<SecondCycleIndividualCandidacyProcess> getValidSecondCycleIndividualCandidacies() {
	final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid()) {
		result.add(process);
	    }
	}
	return result;
    }

    public List<SecondCycleIndividualCandidacyProcess> getValidSecondCycleIndividualCandidacies(final Degree degree) {
	if (degree == null) {
	    return Collections.emptyList();
	}
	final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid() && process.hasCandidacyForSelectedDegree(degree)) {
		result.add(process);
	    }
	}
	return result;
    }

    public Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> getValidSecondCycleIndividualCandidaciesByDegree() {
	final Map<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> result = new TreeMap<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
	    if (process.isCandidacyValid()) {
		addCandidacy(result, process);
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

    public List<SecondCycleIndividualCandidacyProcess> getAcceptedSecondCycleIndividualCandidacies() {
	final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
		result.add((SecondCycleIndividualCandidacyProcess) child);
	    }
	}
	return result;
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new SecondCycleCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendToCoordinator extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isInStandBy() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
	    return process;
	}
    }

    static private class PrintCandidacies extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class IntroduceCandidacyResults extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    final List<SecondCycleIndividualCandidacyResultBean> beans = (List<SecondCycleIndividualCandidacyResultBean>) object;
	    for (final SecondCycleIndividualCandidacyResultBean bean : beans) {
		bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
	    }
	    return process;
	}
    }

    static private class SendToScientificCouncil extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isInStandBy() && !process.isSentToCoordinator()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
	    return process;
	}
    }

    static private class PublishCandidacyResults extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static private class CreateRegistrations extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }

	    // if (!process.isPublished()) {
	    // throw new PreConditionNotValidException();
	    // }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {

	    for (final IndividualCandidacyProcess candidacyProcess : process.getChildProcesses()) {
		final SecondCycleIndividualCandidacyProcess secondCycleCP = (SecondCycleIndividualCandidacyProcess) candidacyProcess;
		if (secondCycleCP.isCandidacyValid() && secondCycleCP.isCandidacyAccepted()
			&& !secondCycleCP.hasRegistrationForCandidacy()) {
		    secondCycleCP.executeActivity(userView, "CreateRegistration", null);
		}
	    }
	    return process;
	}
    }
}
