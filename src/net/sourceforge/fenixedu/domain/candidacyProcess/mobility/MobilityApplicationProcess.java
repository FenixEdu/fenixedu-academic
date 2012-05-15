package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

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
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancyBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ReceptionEmailExecutedAction;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports.ErasmusCandidacyProcessReport;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class MobilityApplicationProcess extends MobilityApplicationProcess_Base {

    static final public Comparator<IndividualCandidacyProcess> COMPARATOR_BY_CANDIDACY_PERSON = new Comparator<IndividualCandidacyProcess>() {
	public int compare(IndividualCandidacyProcess o1, IndividualCandidacyProcess o2) {
	    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(o1.getPersonalDetails(),
		    o2.getPersonalDetails());
	}
    };

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new ViewMobilityQuota());
	activities.add(new InsertMobilityQuota());
	activities.add(new RemoveMobilityQuota());
	activities.add(new ViewErasmusCoordinators());
	activities.add(new AssignCoordinator());
	activities.add(new RemoveTeacherFromCoordinators());
	activities.add(new ViewChildProcessWithMissingRequiredDocumentFiles());
	activities.add(new SendEmailToMissingRequiredDocumentsProcesses());
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendReceptionEmail());
	activities.add(new EditReceptionEmailMessage());
    }

    public MobilityApplicationProcess() {
	super();
    }

    private MobilityApplicationProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new MobilityApplicationPeriod(this, executionYear, start, end);
    }

    public void delete() {
	if (getChildProcessesCount() > 0) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.has.individual.application");
	}
	if (getCoordinatorsCount() > 0) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.has.coordinators");
	}
	if (getCandidacyPeriod() != null) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.defined.period");
	}
	if (getErasmusCandidacyProcessExecutedActionCount() > 0) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.has.executed.actions");
	}
	if (getErasmusCandidacyProcessReportsCount() > 0) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.has.reports");
	}
	if (getProcessLogsCount() > 0) {
	    throw new DomainException("error.mobiliy.application.proccess.cant.be.deleted.it.has.logs");
	}
	setForSemester(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    public List<MobilityIndividualApplicationProcess> getValidErasmusIndividualCandidacies() {
	final List<MobilityIndividualApplicationProcess> result = new ArrayList<MobilityIndividualApplicationProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) child;
	    if (process.isCandidacyValid()) {
		result.add(process);
	    }
	}
	return result;
    }

    public List<MobilityIndividualApplicationProcess> getValidErasmusIndividualCandidacies(final Degree degree) {
	if (degree == null) {
	    return Collections.emptyList();
	}
	final List<MobilityIndividualApplicationProcess> result = new ArrayList<MobilityIndividualApplicationProcess>();
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) child;
	    if (process.isCandidacyValid() && process.hasCandidacyForSelectedDegree(degree)) {
		result.add(process);
	    }
	}
	return result;
    }

    public Map<Degree, SortedSet<MobilityIndividualApplicationProcess>> getValidErasmusIndividualCandidaciesByDegree() {
	final Map<Degree, SortedSet<MobilityIndividualApplicationProcess>> result = new TreeMap<Degree, SortedSet<MobilityIndividualApplicationProcess>>(
		Degree.COMPARATOR_BY_NAME_AND_ID);
	for (final IndividualCandidacyProcess child : getChildProcesses()) {
	    final MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) child;
	    if (process.isCandidacyValid()) {
		SortedSet<MobilityIndividualApplicationProcess> values = result.get(process.getCandidacySelectedDegree());
		if (values == null) {
		    result.put(process.getCandidacySelectedDegree(), values = new TreeSet<MobilityIndividualApplicationProcess>(
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

    public MobilityIndividualApplicationProcess getProcessByEIdentifier(String eIdentifier) {
	List<MobilityIndividualApplicationProcess> childProcesses = new java.util.ArrayList<MobilityIndividualApplicationProcess>(
		(List) this.getChildProcesses());

	for (MobilityIndividualApplicationProcess process : childProcesses) {
	    if (eIdentifier.equals(process.getPersonalDetails().getPerson().getEidentifier())) {
		return process;
	    }
	}

	return null;
    }

    public MobilityIndividualApplicationProcess getOpenProcessByEIdentifier(String eIdentifier) {
	List<MobilityIndividualApplicationProcess> childProcesses = new java.util.ArrayList<MobilityIndividualApplicationProcess>(
		(List) this.getChildProcesses());

	for (MobilityIndividualApplicationProcess process : childProcesses) {
	    if (process.isCandidacyCancelled()) {
		continue;
	    }

	    if (StringUtils.isEmpty(process.getPersonalDetails().getEidentifier())) {
		continue;
	    }

	    if (eIdentifier.equals(process.getPersonalDetails().getEidentifier())) {
		return process;
	    }
	}

	return null;
    }

    public MobilityApplicationPeriod getCandidacyPeriod() {
	return (MobilityApplicationPeriod) super.getCandidacyPeriod();
    }

    public MobilityApplicationPeriod getApplicationPeriod() {
	return getCandidacyPeriod();
    }

    public List<MobilityCoordinator> getErasmusCoordinatorForTeacher(final Teacher teacher) {
	return new ArrayList<MobilityCoordinator>(CollectionUtils.select(getCoordinators(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((MobilityCoordinator) arg0).getTeacher() == teacher;
	    }

	}));
    }

    public MobilityCoordinator getCoordinatorForTeacherAndDegree(final Teacher teacher, final Degree degree) {
	List<MobilityCoordinator> coordinators = getErasmusCoordinatorForTeacher(teacher);

	return (MobilityCoordinator) CollectionUtils.find(coordinators, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		MobilityCoordinator coordinator = (MobilityCoordinator) arg0;
		return coordinator.getDegree() == degree;
	    }
	});
    }

    public List<Degree> getDegreesAssociatedToTeacherAsCoordinator(final Teacher teacher) {
	List<MobilityCoordinator> coordinators = getErasmusCoordinatorForTeacher(teacher);

	return new ArrayList<Degree>(CollectionUtils.collect(coordinators, new Transformer() {

	    @Override
	    public Object transform(Object arg0) {
		return ((MobilityCoordinator) arg0).getDegree();
	    }
	}));
    }

    public boolean isTeacherErasmusCoordinatorForDegree(final Teacher teacher, final Degree degree) {
	return getCoordinatorForTeacherAndDegree(teacher, degree) != null;
    }

    public List<MobilityIndividualApplicationProcess> getProcessesWithNotViewedApprovedLearningAgreements() {
	List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();
	CollectionUtils.select(getChildProcesses(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		MobilityIndividualApplicationProcess individualProcess = (MobilityIndividualApplicationProcess) arg0;

		return !individualProcess.isCandidacyCancelled()
			&& individualProcess.getCandidacy().isMostRecentApprovedLearningAgreementNotViewed();
	    }
	}, processList);

	return processList;
    }

    public List<MobilityIndividualApplicationProcess> getProcessesWithNotViewedAlerts() {
	List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();
	CollectionUtils.select(getChildProcesses(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) arg0;
		return process.isProcessWithMostRecentAlertMessageNotViewed();
	    }

	}, processList);

	return processList;
    }

    public List<ErasmusCandidacyProcessReport> getDoneReports() {
	List<ErasmusCandidacyProcessReport> jobList = new ArrayList<ErasmusCandidacyProcessReport>();

	CollectionUtils.select(getErasmusCandidacyProcessReports(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((QueueJob) arg0).getDone();
	    }
	}, jobList);

	return jobList;
    }

    public List<ErasmusCandidacyProcessReport> getUndoneReports() {
	return new ArrayList(CollectionUtils.subtract(getErasmusCandidacyProcessReports(), getDoneReports()));
    }

    public List<ErasmusCandidacyProcessReport> getPendingReports() {
	List<ErasmusCandidacyProcessReport> jobList = new ArrayList<ErasmusCandidacyProcessReport>();

	CollectionUtils.select(getErasmusCandidacyProcessReports(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((QueueJob) arg0).getIsNotDoneAndNotCancelled();
	    }
	}, jobList);

	return jobList;
    }

    public boolean isAbleToLaunchReportGenerationJob() {
	return getPendingReports().isEmpty();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new MobilityApplicationProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return true;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    private static class ViewMobilityQuota extends Activity<MobilityApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    return process;
	}
    }

    static private class InsertMobilityQuota extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    ErasmusVacancyBean bean = (ErasmusVacancyBean) object;

	    MobilityQuota.createVacancy(process.getCandidacyPeriod(), bean.getDegree(), bean.getMobilityProgram(),
		    bean.getUniversity(), bean.getNumberOfVacancies());

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    static private class RemoveMobilityQuota extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    ErasmusVacancyBean bean = (ErasmusVacancyBean) object;
	    MobilityQuota quota = bean.getQuota();

	    if (quota.isQuotaAssociatedWithAnyApplication()) {
		throw new DomainException("error.mobility.quota.is.associated.with.applications");
	    }

	    quota.delete();

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    static private class ViewErasmusCoordinators extends Activity<MobilityApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    return process;
	}
    }

    static private class AssignCoordinator extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    ErasmusCoordinatorBean bean = (ErasmusCoordinatorBean) object;
	    new MobilityCoordinator(process, bean);

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    static private class RemoveTeacherFromCoordinators extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    ErasmusCoordinatorBean bean = (ErasmusCoordinatorBean) object;

	    if (bean.getErasmusCoordinator() != null) {
		bean.getErasmusCoordinator().delete();
	    }
	    bean.setErasmusCoordinator(null);

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}
    }

    static private class ViewChildProcessWithMissingRequiredDocumentFiles extends Activity<MobilityApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView) && !isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return true;
	}

    }

    static private class SendEmailToMissingRequiredDocumentsProcesses extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    for (IndividualCandidacyProcess childProcess : process.getChildsWithMissingRequiredDocuments()) {
		MobilityIndividualApplicationProcess erasmusChildProcess = (MobilityIndividualApplicationProcess) childProcess;
		erasmusChildProcess.sendEmailForRequiredMissingDocuments();
	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    static private class SendReceptionEmail extends Activity<MobilityApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (isManager(userView)) {
		return;
	    }

	    if (isGriOfficeEmployee(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    SendReceptionEmailBean sendBean = (SendReceptionEmailBean) object;
	    ReceptionEmailExecutedAction.createAction(process, sendBean);

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return true;
	}
    }

    static private class EditReceptionEmailMessage extends Activity<MobilityApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityApplicationProcess process, IUserView userView) {
	    if (isManager(userView)) {
		return;
	    }

	    if (isGriOfficeEmployee(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityApplicationProcess executeActivity(MobilityApplicationProcess process, IUserView userView, Object object) {
	    process.editReceptionEmailMessage((SendReceptionEmailBean) object);

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return false;
	}

    }

    public void editReceptionEmailMessage(SendReceptionEmailBean sendReceptionEmailBean) {
	if (StringUtils.isEmpty(sendReceptionEmailBean.getEmailSubject())
		|| StringUtils.isEmpty(sendReceptionEmailBean.getEmailBody())) {
	    throw new DomainException("error.reception.email.subject.and.body.must.not.be.empty");
	}

	setReceptionEmailSubject(sendReceptionEmailBean.getEmailSubject());
	setReceptionEmailBody(sendReceptionEmailBean.getEmailBody());
    }

    public boolean isReceptionEmailMessageDefined() {
	return !StringUtils.isEmpty(getReceptionEmailSubject()) && !StringUtils.isEmpty(getReceptionEmailBody());
    }

    @Override
    public boolean isMobility() {
	return true;
    }

}
