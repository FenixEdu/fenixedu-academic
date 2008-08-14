package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class Over23CandidacyProcess extends Over23CandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null && candidacyPeriod instanceof Over23CandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.Over23CandidacyProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendInformationToJury());
	activities.add(new PrintCandidacies());
	activities.add(new IntroduceCandidacyResults());
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    private Over23CandidacyProcess() {
	super();
    }

    private Over23CandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new Over23CandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public List<Over23IndividualCandidacyProcess> getOver23IndividualCandidaciesThatCanBeSendToJury() {
	final List<Over23IndividualCandidacyProcess> result = new ArrayList<Over23IndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final Over23IndividualCandidacyProcess over23CP = (Over23IndividualCandidacyProcess) child;
	    if (over23CP.isCandidacyValid()) {
		result.add(over23CP);
	    }
	}
	return result;
    }

    public List<Over23IndividualCandidacyProcess> getAcceptedOver23IndividualCandidacies() {
	final List<Over23IndividualCandidacyProcess> result = new ArrayList<Over23IndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
		result.add((Over23IndividualCandidacyProcess) child);
	    }
	}
	return result;
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new Over23CandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendInformationToJury extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
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
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_JURY);
	    return process;
	}
    }

    static private class PrintCandidacies extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class IntroduceCandidacyResults extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final List<Over23IndividualCandidacyResultBean> beans = (List<Over23IndividualCandidacyResultBean>) object;
	    for (final Over23IndividualCandidacyResultBean bean : beans) {
		if (bean.isValid()) {
		    bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
		}
	    }
	    return process;
	}
    }

    static private class PublishCandidacyResults extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static private class CreateRegistrations extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToJury()) {
		throw new PreConditionNotValidException();
	    }

	    // if (!process.isPublished()) {
	    // throw new PreConditionNotValidException();
	    // }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    for (final IndividualCandidacyProcess candidacyProcess : process.getChildProcesses()) {
		final Over23IndividualCandidacyProcess over23CP = (Over23IndividualCandidacyProcess) candidacyProcess;
		if (over23CP.isCandidacyValid() && over23CP.isCandidacyAccepted() && !over23CP.hasRegistrationForCandidacy()) {
		    over23CP.executeActivity(userView, "CreateRegistration", null);
		}
	    }
	    return process;
	}
    }
}
