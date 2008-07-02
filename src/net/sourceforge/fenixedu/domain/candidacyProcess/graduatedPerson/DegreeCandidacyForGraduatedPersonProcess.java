package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class DegreeCandidacyForGraduatedPersonProcess extends DegreeCandidacyForGraduatedPersonProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null
			&& candidacyPeriod instanceof DegreeCandidacyForGraduatedPersonCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException(
				"error.DegreeCandidacyForGraduatedPersonProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendToCoordinator());
	activities.add(new SendToScientificCouncil());
	activities.add(new PrintCandidacies());
	activities.add(new IntroduceCandidacyResults()); // TODO
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    private DegreeCandidacyForGraduatedPersonProcess() {
	super();
    }

    private DegreeCandidacyForGraduatedPersonProcess(final ExecutionInterval executionInterval, final DateTime start,
	    final DateTime end) {
	this();
	checkParameters(executionInterval, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new DegreeCandidacyForGraduatedPersonCandidacyPeriod(this, executionInterval, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.interval");
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

    public Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> getValidDegreeCandidaciesForGraduatedPersonsByDegree() {
	final Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> result = new TreeMap<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final DegreeCandidacyForGraduatedPersonIndividualProcess process = (DegreeCandidacyForGraduatedPersonIndividualProcess) child;
	    if (process.isCandidacyValid()) {
		addCandidacy(result, process);
	    }
	}
	return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> result,
	    final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
	SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess> values = result.get(process.getCandidacySelectedDegree());
	if (values == null) {
	    result.put(process.getCandidacySelectedDegree(),
		    values = new TreeSet<DegreeCandidacyForGraduatedPersonIndividualProcess>(
			    DegreeCandidacyForGraduatedPersonIndividualProcess.COMPARATOR_BY_CANDIDACY_PERSON));
	}
	values.add(process);
    }

    public List<DegreeCandidacyForGraduatedPersonIndividualProcess> getAcceptedDegreeCandidacyForGraduatedPersonIndividualCandidacies() {
	final List<DegreeCandidacyForGraduatedPersonIndividualProcess> result = new ArrayList<DegreeCandidacyForGraduatedPersonIndividualProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
		result.add((DegreeCandidacyForGraduatedPersonIndividualProcess) child);
	    }
	}
	return result;
    }

    // static methods

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new DegreeCandidacyForGraduatedPersonProcess(bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendToCoordinator extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
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
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
	    return process;
	}
    }

    static private class SendToScientificCouncil extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
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
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
	    return process;
	}
    }

    static private class PrintCandidacies extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class IntroduceCandidacyResults extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    final List<DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean> beans = (List<DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean>) object;
	    for (final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean : beans) {
		bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
	    }
	    return process;
	}
    }

    static private class PublishCandidacyResults extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static private class CreateRegistrations extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {

	    for (final IndividualCandidacyProcess child : process.getChildProcesses()) {
		final DegreeCandidacyForGraduatedPersonIndividualProcess childProcess = (DegreeCandidacyForGraduatedPersonIndividualProcess) child;
		if (child.isCandidacyValid() && child.isCandidacyAccepted() && !child.hasRegistrationForCandidacy()) {
		    childProcess.executeActivity(userView, "CreateRegistration", null);
		}
	    }

	    return process;
	}
    }

}
