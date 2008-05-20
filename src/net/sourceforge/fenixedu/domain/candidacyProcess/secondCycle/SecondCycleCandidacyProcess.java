package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class SecondCycleCandidacyProcess extends SecondCycleCandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null && candidacyPeriod instanceof SecondCycleCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.SecondCycleCandidacyProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendToCoordinator());
	activities.add(new PrintCandidacies());
	// TODO: activities.add(new IntroduceCandidacyResults());
	// TODO: activities.add(new SendToScientificCouncil());
	// TODO: activities.add(new PublishCandidacyResults());
	// TODO: activities.add(new CreateRegistrations());
    }

    private SecondCycleCandidacyProcess() {
	super();
    }

    private SecondCycleCandidacyProcess(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionInterval, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new SecondCycleCandidacyPeriod(this, executionInterval, start, end);
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

    public List<SecondCycleIndividualCandidacyProcess> getValidSecondCycleIndividualCandidacies() {
	final List<SecondCycleIndividualCandidacyProcess> result = new ArrayList<SecondCycleIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
	    if (process.isValid()) {
		result.add(process);
	    }
	}
	return result;
    }

    public Map<Degree, List<SecondCycleIndividualCandidacyProcess>> getValidSecondCycleIndividualCandidaciesByDegree() {
	final Map<Degree, List<SecondCycleIndividualCandidacyProcess>> result = new HashMap<Degree, List<SecondCycleIndividualCandidacyProcess>>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) child;
	    if (process.isValid()) {
		addCandidacy(result, process);
	    }
	}
	return result;
    }

    private void addCandidacy(final Map<Degree, List<SecondCycleIndividualCandidacyProcess>> result,
	    final SecondCycleIndividualCandidacyProcess process) {
	List<SecondCycleIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
	if (values == null) {
	    result.put(process.getCandidacySelectedDegree(), values = new ArrayList<SecondCycleIndividualCandidacyProcess>());
	}
	values.add(process);
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
	    final SecondCycleCandidacyProcessBean bean = (SecondCycleCandidacyProcessBean) object;
	    return new SecondCycleCandidacyProcess(bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
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
	    final SecondCycleCandidacyProcessBean bean = (SecondCycleCandidacyProcessBean) object;
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

	    if (!process.hasCandidacyPeriod() || process.hasOpenCandidacyPeriod()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isInStandBy()) {
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

	    if (!process.isSentToCoordinator()) {
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

	    if (!process.isSentToCoordinator()) {
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
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
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

	    if (!process.isPublished()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {

	    for (final IndividualCandidacyProcess candidacyProcess : process.getChildProcesses()) {
		final SecondCycleIndividualCandidacyProcess secondCycleCP = (SecondCycleIndividualCandidacyProcess) candidacyProcess;
		if (secondCycleCP.isCandidacyAccepted() && !secondCycleCP.hasRegistrationForCandidacy()) {
		    secondCycleCP.executeActivity(userView, "CreateRegistration", null);
		}
	    }

	    return process;
	}
    }
}
