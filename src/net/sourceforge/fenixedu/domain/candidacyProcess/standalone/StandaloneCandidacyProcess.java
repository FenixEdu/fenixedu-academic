package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.StandaloneCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

public class StandaloneCandidacyProcess extends StandaloneCandidacyProcess_Base {

    static private final List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendToCoordinator());
	activities.add(new PrintCandidacies());
	activities.add(new IntroduceCandidacyResults());
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    private StandaloneCandidacyProcess() {
	super();
    }

    public StandaloneCandidacyProcess(final ExecutionSemester executionSemester, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionSemester, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new StandaloneCandidacyPeriod(this, executionSemester, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.StandaloneCandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.StandaloneCandidacyProcess.invalid.interval");
	}
    }

    @Override
    public boolean canExecuteActivity(final IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    public List<StandaloneIndividualCandidacyProcess> getSortedStandaloneIndividualCandidaciesThatCanBeSendToJury() {
	final List<StandaloneIndividualCandidacyProcess> result = new ArrayList<StandaloneIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.isCandidacyValid()) {
		result.add((StandaloneIndividualCandidacyProcess) child);
	    }
	}
	Collections.sort(result, StandaloneIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON);
	return result;
    }

    public List<StandaloneIndividualCandidacyProcess> getAcceptedStandaloneIndividualCandidacies() {
	final List<StandaloneIndividualCandidacyProcess> result = new ArrayList<StandaloneIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
		result.add((StandaloneIndividualCandidacyProcess) child);
	    }
	}
	return result;
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new StandaloneCandidacyProcess((ExecutionSemester) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendToCoordinator extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasCandidacyPeriod() || !process.hasStarted() || process.hasOpenCandidacyPeriod()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
	    return process;
	}
    }

    static private class PrintCandidacies extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class IntroduceCandidacyResults extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    final List<StandaloneIndividualCandidacyResultBean> beans = (List<StandaloneIndividualCandidacyResultBean>) object;
	    for (final StandaloneIndividualCandidacyResultBean bean : beans) {
		bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
	    }
	    return process;
	}
    }

    static private class PublishCandidacyResults extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static private class CreateRegistrations extends Activity<StandaloneCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneCandidacyProcess executeActivity(StandaloneCandidacyProcess process, IUserView userView, Object object) {
	    for (final IndividualCandidacyProcess candidacyProcess : process.getChildProcesses()) {
		final StandaloneIndividualCandidacyProcess child = (StandaloneIndividualCandidacyProcess) candidacyProcess;
		if (child.isCandidacyValid() && child.isCandidacyAccepted() && !child.hasRegistrationForCandidacy()) {
		    child.executeActivity(userView, "CreateRegistration", null);
		}
	    }
	    return null;
	}
    }
}
