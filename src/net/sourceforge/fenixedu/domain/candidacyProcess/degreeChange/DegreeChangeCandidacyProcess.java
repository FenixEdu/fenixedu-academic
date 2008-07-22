package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

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
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeChangeCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class DegreeChangeCandidacyProcess extends DegreeChangeCandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);
		if (candidacyProcess != null && candidacyPeriod != null && candidacyPeriod instanceof DegreeChangeCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.DegreeChangeCandidacyProcess.candidacy.period.already.has.process");
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
	activities.add(new PrintCandidaciesFromInstitutionDegrees());
	activities.add(new PrintCandidaciesFromExternalDegrees());
	activities.add(new IntroduceCandidacyResults());
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    private DegreeChangeCandidacyProcess() {
	super();
    }

    private DegreeChangeCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new DegreeChangeCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.DegreeChangeCandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.DegreeChangeCandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidDegreeChangeIndividualCandidacyProcessesByDegree() {
	final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result = new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);
	for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
	    final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
	    if (child.isCandidacyValid()) {
		addCandidacy(result, child);
	    }
	}
	return result;
    }

    public List<DegreeChangeIndividualCandidacyProcess> getValidDegreeChangeIndividualCandidacyProcesses(final Degree degree) {
	if (degree == null) {
	    return Collections.emptyList();
	}

	final List<DegreeChangeIndividualCandidacyProcess> result = new ArrayList<DegreeChangeIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
	    final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
	    if (child.isCandidacyValid() && child.hasCandidacyForSelectedDegree(degree)) {
		result.add(child);
	    }
	}
	return result;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidInstitutionIndividualCandidacyProcessesByDegree() {
	final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result = new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);

	for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
	    final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
	    if (child.isCandidacyValid() && !child.getCandidacyPrecedentDegreeInformation().isExternal()) {
		addCandidacy(result, child);
	    }
	}

	return result;
    }

    public Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> getValidExternalIndividualCandidacyProcessesByDegree() {
	final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result = new TreeMap<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);

	for (final IndividualCandidacyProcess process : getChildProcessesSet()) {
	    final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) process;
	    if (child.isCandidacyValid() && child.getCandidacyPrecedentDegreeInformation().isExternal()) {
		addCandidacy(result, child);
	    }
	}

	return result;
    }

    private void addCandidacy(final Map<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> result,
	    final DegreeChangeIndividualCandidacyProcess process) {
	SortedSet<DegreeChangeIndividualCandidacyProcess> values = result.get(process.getCandidacySelectedDegree());
	if (values == null) {
	    result.put(process.getCandidacySelectedDegree(), values = new TreeSet<DegreeChangeIndividualCandidacyProcess>(
		    DegreeChangeIndividualCandidacyProcess.COMPARATOR_BY_CANDIDACY_PERSON));
	}
	values.add(process);
    }

    public List<DegreeChangeIndividualCandidacyProcess> getAcceptedDegreeChangeIndividualCandidacyProcesses() {
	final List<DegreeChangeIndividualCandidacyProcess> result = new ArrayList<DegreeChangeIndividualCandidacyProcess>();
	for (final IndividualCandidacyProcess child : getChildProcessesSet()) {
	    if (child.isCandidacyValid() && child.isCandidacyAccepted()) {
		result.add((DegreeChangeIndividualCandidacyProcess) child);
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
    static public class CreateCandidacyPeriod extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess dummy, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new DegreeChangeCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendToCoordinator extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_COORDINATOR);
	    return process;
	}
    }

    static private class SendToScientificCouncil extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_SCIENTIFIC_COUNCIL);
	    return process;
	}
    }

    static private class PrintCandidaciesFromInstitutionDegrees extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class PrintCandidaciesFromExternalDegrees extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static private class IntroduceCandidacyResults extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    final List<DegreeChangeIndividualCandidacyResultBean> beans = (List<DegreeChangeIndividualCandidacyResultBean>) object;
	    for (final DegreeChangeIndividualCandidacyResultBean bean : beans) {
		bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
	    }
	    return process;
	}
    }

    static private class PublishCandidacyResults extends Activity<DegreeChangeCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static private class CreateRegistrations extends Activity<DegreeChangeCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeCandidacyProcess executeActivity(DegreeChangeCandidacyProcess process, IUserView userView,
		Object object) {
	    for (final IndividualCandidacyProcess each : process.getChildProcessesSet()) {
		final DegreeChangeIndividualCandidacyProcess child = (DegreeChangeIndividualCandidacyProcess) each;
		if (child.isCandidacyValid() && child.isCandidacyAccepted() && !child.hasRegistrationForCandidacy()) {
		    child.executeActivity(userView, "CreateRegistration", null);
		}
	    }
	    return process;
	}
    }

}