package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdFinalProofRequestAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotificationBean;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermissionType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdProgramCandidacyProcess> {

	protected PhdPermissionType getCandidacyProcessManagementPermission() {
	    return PhdPermissionType.CANDIDACY_PROCESS_MANAGEMENT;
	}

	@Override
	final public void checkPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView);
    }

    @StartActivity
    public static class CreateCandidacy extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final Object[] values = (Object[]) object;
	    final PhdProgramCandidacyProcessBean bean = getBean(values);
	    final Person person = getPerson(values);
	    final PhdProgramCandidacyProcess result = new PhdProgramCandidacyProcess(bean, person, bean.getMigratedProcess());
	    result.createState(bean.getState(), person, "");
	    return result;
	}

	private Person getPerson(final Object[] values) {
	    return (Person) values[1];
	}

	private PhdProgramCandidacyProcessBean getBean(final Object[] values) {
	    return (PhdProgramCandidacyProcessBean) values[0];
	}
    }

    public static class UploadDocuments extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (process.getActiveState() != PhdProgramCandidacyProcessState.PRE_CANDIDATE) {
		if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		    throw new PreConditionNotValidException();
		}
	    }

	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final List<PhdProgramDocumentUploadBean> documents = (List<PhdProgramDocumentUploadBean>) object;

	    for (final PhdProgramDocumentUploadBean each : documents) {
		if (each.hasAnyInformation()) {
		    process.addDocument(each, userView != null ? userView.getPerson() : null);
		}
	    }

	    return process;
	}
    }

    public static class DeleteDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    ((PhdProgramProcessDocument) object).delete();

	    return process;
	}
    }

    public static class EditCandidacyDate extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return process.edit((LocalDate) object);
	}
    }

    static public class AddCandidacyReferees extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    for (final PhdCandidacyRefereeBean bean : (List<PhdCandidacyRefereeBean>) object) {
		process.addCandidacyReferees(new PhdCandidacyReferee(process, bean));
	    }
	    return process;
	}
    }

    static public class ValidatedByCandidate extends PhdActivity {
	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    process.setValidatedByCandidate(true);
	    return process;
	}
    }

    static public class RequestRatifyCandidacy extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;

	    if (!process.getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		    && process.getCandidacyReviewDocuments().isEmpty()) {
		throw new DomainException(
			"error.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy.candidacy.review.document.is.required");
	    }

	    process.createState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION,
		    userView.getPerson(), bean.getRemarks());

	    if (bean.getGenerateAlert()) {
		AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
			getCandidacyProcessManagementPermission(), "message.phd.alert.candidacy.request.ratify.subject",
			"message.phd.alert.candidacy.request.ratify.body");
	    }

	    return process;
	}
    }

    static public class RatifyCandidacy extends PhdActivity {
	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!process.isInState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    process.ratify((RatifyCandidacyBean) object, userView != null ? userView.getPerson() : null);
	    return process;
	}
    }

    static public class RejectCandidacyProcess extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
	    process.createState(PhdProgramCandidacyProcessState.REJECTED, userView.getPerson(), bean.getRemarks());

	    AlertService.alertAcademicOffice(process.getIndividualProgramProcess(), getCandidacyProcessManagementPermission(),
		    "message.phd.alert.candidacy.reject.subject", "message.phd.alert.candidacy.reject.body");

	    return process;
	}

    }

    static public class RequestCandidacyReview extends PhdActivity {

	static final private List<PhdProgramCandidacyProcessState> PREVIOUS_STATE = Arrays.asList(

	PhdProgramCandidacyProcessState.PRE_CANDIDATE,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION,

	PhdProgramCandidacyProcessState.REJECTED,

	PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (PREVIOUS_STATE.contains(process.getActiveState())) {
		return;
	    }
	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {

	    final PhdIndividualProgramProcess mainProcess = process.getIndividualProgramProcess();
	    if (!mainProcess.hasPhdProgram()) {
		throw new DomainException(
			"error.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview.invalid.phd.program");
	    }

	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
	    process.createState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION, userView.getPerson(),
		    bean.getRemarks());

	    if (bean.getGenerateAlert()) {
		AlertService.alertCoordinators(mainProcess, subject(), body(mainProcess));
	    }

	    return process;
	}

	private AlertMessage subject() {
	    return AlertMessage.create("message.phd.alert.candidacy.review.subject");
	}

	private AlertMessage body(final PhdIndividualProgramProcess process) {
	    return AlertMessage.create("message.phd.alert.candidacy.review.body").args(process.getProcessNumber(),
		    process.getPerson().getName());
	}
    }

    static public class UploadCandidacyReview extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		if ((isMasterDegreeAdministrativeOfficeEmployee(userView) || process.getIndividualProgramProcess()
			.isCoordinatorForPhdProgram(userView.getPerson()))) {
		    return;
		}
	    }

	    if (process.isInState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION)) {
		if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		    return;
		}
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {

	    for (final PhdProgramDocumentUploadBean each : (List<PhdProgramDocumentUploadBean>) object) {
		if (each.hasAnyInformation()) {
		    process.addDocument(each, userView != null ? userView.getPerson() : null);
		}
	    }

	    return process;
	}
    }

    static public class DeleteCandidacyReview extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		return;
	    }

	    if (process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		if (process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
		    return;
		}
	    }

	    throw new PreConditionNotValidException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.DeleteCandidacyReview.cannot.delete.review.after.sending.for.ratification");

	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    PhdProgramProcessDocument document = (PhdProgramProcessDocument) object;

	    document.delete();

	    return process;
	}
    }

    static public class AddNotification extends PhdActivity {
	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    new PhdNotification((PhdNotificationBean) object);

	    return process;
	}
    }

    static public class RegistrationFormalization extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!process.isInState(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return process.registrationFormalization((RegistrationFormalizationBean) object, userView.getPerson());
	}

    }

    static public class AssociateRegistration extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {

	    if (!process.isInState(PhdProgramCandidacyProcessState.CONCLUDED)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasStudyPlan() || process.getIndividualProgramProcess().hasRegistration()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return process.associateRegistration((RegistrationFormalizationBean) object);
	}

    }

    static public class EditProcessAttributes extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) object;

	    process.setCandidacyDate(bean.getCandidacyDate());
	    process.setWhenRatified(bean.getWhenRatified());
	    return process;
	}

    }

    static public class AddState extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) object;

	    PhdCandidacyProcessState.createStateWithGivenStateDate(process, bean.getState(), userView.getPerson(), "", bean
		    .getStateDate().toDateTimeAtStartOfDay());

	    return process;
	}

    }

    static public class RemoveLastState extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    process.deleteLastState();
	    return process;
	}
    }

    static public class RemoveCandidacyDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		return;
	    }

	    if (process.isPublicCandidacy() && process.getPublicPhdCandidacyPeriod().isOpen()) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    PhdProgramProcessDocument phdDocument = (PhdProgramProcessDocument) object;

	    phdDocument.removeFromProcess();

	    return process;
	}

    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new UploadDocuments());
	activities.add(new DeleteDocument());
	activities.add(new EditCandidacyDate());
	activities.add(new AddCandidacyReferees());
	activities.add(new ValidatedByCandidate());

	activities.add(new RequestCandidacyReview());
	activities.add(new UploadCandidacyReview());
	activities.add(new DeleteCandidacyReview());
	activities.add(new RejectCandidacyProcess());

	activities.add(new RequestRatifyCandidacy());
	activities.add(new RatifyCandidacy());

	activities.add(new AddNotification());
	activities.add(new RegistrationFormalization());
	activities.add(new AssociateRegistration());

	activities.add(new AddState());
	activities.add(new RemoveLastState());
	activities.add(new EditProcessAttributes());

	activities.add(new RemoveCandidacyDocument());
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean bean, final Person person, boolean isMigratedProcess) {
	super();

	checkCandidacyDate(bean.getExecutionYear(), bean.getCandidacyDate());
	setCandidacyDate(bean.getCandidacyDate());
	setValidatedByCandidate(false);

	setCandidacyHashCode(bean.getCandidacyHashCode());
	setCandidacy(new PHDProgramCandidacy(person));

	if (bean.hasDegree()) {
	    getCandidacy().setExecutionDegree(bean.getExecutionDegree());
	}

	if (!isMigratedProcess) {
	    if (!bean.hasCollaborationType() || bean.getCollaborationType().generateCandidacyDebt()) {
		new PhdProgramCandidacyEvent(person, this);
	    }
	}

	if (isPublicCandidacy()) {
	    if (bean.getPhdCandidacyPeriod() == null) {
		throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.public.candidacy.period.is.missing");
	    }

	    setPublicPhdCandidacyPeriod(bean.getPhdCandidacyPeriod());
	}
    }

    public boolean isPublicCandidacy() {
	return hasCandidacyHashCode();
    }

    public IndividualCandidacyPaymentCode getAssociatedPaymentCode() {
	if (!hasEvent()) {
	    return null;
	}

	return getEvent().getAssociatedPaymentCode();
    }

    private void checkCandidacyDate(ExecutionYear executionYear, LocalDate candidacyDate) {
	// TODO: check this - in august candidacy dates will not be contained in
	// given execution year

	check(candidacyDate, "error.phd.candidacy.PhdProgramCandidacyProcess.invalid.candidacy.date");
	// if (!executionYear.containsDate(candidacyDate)) {
	// throw new DomainException(
	// "error.phd.candidacy.PhdProgramCandidacyProcess.executionYear.doesnot.contains.candidacy.date",
	// candidacyDate
	// .toString("dd/MM/yyyy"), executionYear.getQualifiedName(),
	// executionYear.getBeginDateYearMonthDay()
	// .toString("dd/MM/yyyy"),
	// executionYear.getEndDateYearMonthDay().toString("dd/MM/yyyy"));
	// }
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    private PhdProgramCandidacyProcess edit(final LocalDate candidacyDate) {
	checkCandidacyDate(getExecutionYear(), candidacyDate);
	setCandidacyDate(candidacyDate);
	return this;
    }

    private ExecutionYear getExecutionYear() {
	return getIndividualProgramProcess().getExecutionYear();
    }

    public boolean hasAnyPayments() {
	return hasEvent() && getEvent().hasAnyPayments();
    }

    public void cancelDebt(final Employee employee) {
	if (hasEvent()) {
	    getEvent().cancel(employee);
	}
    }

    public String getProcessNumber() {
	return getIndividualProgramProcess().getProcessNumber();
    }

    public Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    public Set<PhdProgramProcessDocument> getCandidacyReviewDocuments() {
	return getLatestDocumentsByType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);
    }

    public boolean hasAnyDocuments(final PhdIndividualProgramDocumentType type) {
	return !getLatestDocumentsByType(type).isEmpty();
    }

    public int getDocumentsCount(final PhdIndividualProgramDocumentType type) {
	return getLatestDocumentsByType(type).size();
    }

    public boolean isValidatedByCandidate() {
	return getValidatedByCandidate() != null && getValidatedByCandidate().booleanValue();
    }

    public Set<PhdProgramProcessDocument> getStudyPlanRelevantDocuments() {
	final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();
	result.addAll(getLatestDocumentsByType(PhdIndividualProgramDocumentType.STUDY_PLAN));
	result.addAll(getLatestDocumentsByType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW));

	return result;
    }

    public void createState(final PhdProgramCandidacyProcessState stateType, final Person person, final String remarks) {
	PhdCandidacyProcessState.createStateWithInferredStateDate(this, stateType, person, remarks);
    }

    @Override
    public PhdCandidacyProcessState getMostRecentState() {
	return (PhdCandidacyProcessState) super.getMostRecentState();
    }

    @Override
    public PhdProgramCandidacyProcessState getActiveState() {
	return (PhdProgramCandidacyProcessState) super.getActiveState();
    }

    public boolean isInState(final PhdProgramCandidacyProcessState state) {
	return getActiveState().equals(state);
    }

    public void ratify(RatifyCandidacyBean bean, Person responsible) {

	check(bean.getWhenRatified(), "error.phd.candidacy.PhdProgramCandidacyProcess.when.ratified.cannot.be.null");

	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		&& !bean.getRatificationFile().hasAnyInformation()) {
	    throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.ratification.document.is.required");
	}

	setWhenRatified(bean.getWhenRatified());

	if (bean.getRatificationFile().hasAnyInformation()) {
	    addDocument(bean.getRatificationFile(), responsible);
	}

	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		&& !getIndividualProgramProcess().hasAnyRegistrationFormalizationActiveAlert()) {
	    new PhdRegistrationFormalizationAlert(getIndividualProgramProcess(), bean.getMaxDaysToFormalizeRegistration());
	}

	createState(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL, responsible, "");

    }

    private PhdProgramCandidacyProcess registrationFormalization(final RegistrationFormalizationBean bean,
	    final Person responsible) {

	if (!hasStudyPlan()) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.registrationFormalization.must.create.study.plan");
	}

	LocalDate whenFormalizedRegistration = new LocalDate();

	getIndividualProgramProcess().setWhenFormalizedRegistration(whenFormalizedRegistration);
	getIndividualProgramProcess().setWhenStartedStudies(bean.getWhenStartedStudies());

	assertPersonInformation();

	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()) {
	    final DegreeCurricularPlan dcp = getPhdProgramLastActiveDegreeCurricularPlan();
	    assertStudyPlanInformation(bean, dcp);
	    assertDebts(bean);
	    assertRegistrationFormalizationAlerts();
	}

	createState(PhdProgramCandidacyProcessState.CONCLUDED, responsible, "");
	getIndividualProgramProcess().createState(PhdIndividualProgramProcessState.WORK_DEVELOPMENT, responsible, "");

	return this;
    }

    private void assertStudyPlanInformation(final RegistrationFormalizationBean bean,
	    final DegreeCurricularPlan degreeCurricularPlan) {
	final ExecutionYear executionYear = ExecutionYear.readByDateTime(bean.getWhenStartedStudies());
	if (hasCurricularStudyPlan()) {
	    assertCandidacy(degreeCurricularPlan, executionYear);
	    assertRegistration(bean, degreeCurricularPlan, executionYear);
	}
    }

    public PhdProgramCandidacyProcess associateRegistration(final RegistrationFormalizationBean bean) {

	if (isStudyPlanExempted()) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.associateRegistration.study.plan.is.exempted");
	}

	assertStudyPlanInformation(bean, bean.getRegistration().getLastDegreeCurricularPlan());
	assertRegistrationFormalizationAlerts();

	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()) {
	    assertDebts(bean);
	}

	return this;
    }

    private void assertPersonInformation() {
	final Person person = getPerson();

	if (!getPerson().hasStudent()) {
	    new Student(getPerson());
	}

	person.addPersonRoleByRoleType(RoleType.PERSON);
	person.addPersonRoleByRoleType(RoleType.STUDENT);
	person.addPersonRoleByRoleType(RoleType.RESEARCHER);
	person.setIstUsername();

	if (!person.hasPersonalPhoto()) {
	    final Photograph photograph = person.getPersonalPhotoEvenIfPending();
	    if (photograph != null) {
		photograph.setState(PhotoState.APPROVED);
	    }
	}
    }

    private void assertCandidacy(final DegreeCurricularPlan dcp, final ExecutionYear executionYear) {
	if (!getCandidacy().hasExecutionDegree()) {
	    final ExecutionDegree executionDegree = dcp.getExecutionDegreeByAcademicInterval(executionYear.getAcademicInterval());
	    getCandidacy().setExecutionDegree(executionDegree);
	}

	getCandidacy().setIngression(Ingression.CIA3C);
    }

    private void assertRegistration(final RegistrationFormalizationBean bean, final DegreeCurricularPlan dcp,
	    final ExecutionYear executionYear) {

	final Registration registration = getOrCreateRegistration(bean, dcp, executionYear);

	registration.setHomologationDate(getWhenRatified());
	registration.setStudiesStartDate(bean.getWhenStartedStudies());
	registration.setIngression(Ingression.CIA3C);
	registration.setPhdIndividualProgramProcess(getIndividualProgramProcess());

	registration.editStartDates(bean.getWhenStartedStudies(), getWhenRatified(), bean.getWhenStartedStudies());

	if (registration.getStudentCandidacy() == getIndividualProgramProcess().getCandidacyProcess().getCandidacy()) {
	    return;
	}

	StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
	if (registration.getCandidacyInformationBean().isValid()) {
	    getIndividualProgramProcess().getCandidacyProcess().getCandidacy().copyFromStudentCandidacy(studentCandidacy);
	}

	studentCandidacy.delete();
    }

    private Registration getOrCreateRegistration(final RegistrationFormalizationBean bean, final DegreeCurricularPlan dcp,
	    final ExecutionYear executionYear) {

	final Registration registration;
	if (getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		&& bean.hasRegistration()) {
	    return bean.getRegistration();
	}

	if (hasActiveRegistrationFor(dcp)) {
	    if (!bean.hasRegistration()) {
		throw new DomainException("error.PhdProgramCandidacyProcess.regisration.formalization.already.has.registration");
	    }
	    registration = bean.getRegistration();
	} else {
	    registration = new Registration(getPerson(), dcp, getCandidacy(), RegistrationAgreement.NORMAL, null, executionYear);
	}

	return registration;
    }

    private void assertDebts(final RegistrationFormalizationBean bean) {
	assertPhdRegistrationFee();
	assertInsuranceEvent(ExecutionYear.readByDateTime(bean.getWhenStartedStudies()));
    }

    private void assertPhdRegistrationFee() {
	if (!getIndividualProgramProcess().hasRegistrationFee()) {
	    new PhdRegistrationFee(getIndividualProgramProcess());
	}
    }

    private void assertInsuranceEvent(final ExecutionYear executionYear) {
	if (!getPerson().hasInsuranceEventFor(executionYear)
		&& !getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
	    new InsuranceEvent(getPerson(), executionYear);
	}
    }

    private void assertRegistrationFormalizationAlerts() {
	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		&& !getIndividualProgramProcess().hasPhdPublicPresentationSeminarAlert()) {
	    new PhdPublicPresentationSeminarAlert(getIndividualProgramProcess());
	}

	if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
		&& !getIndividualProgramProcess().hasPhdFinalProofRequestAlert()) {
	    new PhdFinalProofRequestAlert(getIndividualProgramProcess());
	}
    }

    private boolean hasCurricularStudyPlan() {
	return hasStudyPlan() && !getIndividualProgramProcess().getStudyPlan().isExempted();
    }

    public DegreeCurricularPlan getPhdProgramLastActiveDegreeCurricularPlan() {
	return hasPhdProgram() ? getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan() : null;
    }

    PhdProgram getPhdProgram() {
	return getIndividualProgramProcess().getPhdProgram();
    }

    PhdProgramFocusArea getPhdProgramFocusArea() {
	return getIndividualProgramProcess().getPhdProgramFocusArea();
    }

    boolean hasPhdProgramFocusArea() {
	return getPhdProgramFocusArea() != null;
    }

    public boolean hasPhdProgram() {
	return getPhdProgram() != null;
    }

    public boolean hasStudyPlan() {
	return getIndividualProgramProcess().hasStudyPlan();
    }

    public boolean isStudyPlanExempted() {
	return hasStudyPlan() && getIndividualProgramProcess().getStudyPlan().isExempted();
    }

    public boolean hasActiveRegistrationFor(DegreeCurricularPlan degreeCurricularPlan) {
	return getPerson().hasStudent() ? getPerson().getStudent().hasActiveRegistrationFor(degreeCurricularPlan) : false;
    }

    public LocalDate getWhenStartedStudies() {
	return getIndividualProgramProcess().getWhenStartedStudies();
    }

    /*
     * !getCandidacy().hasRegistration() -> because if is connected to
     * registration then it wiil be corrected by existing code
     */
    public boolean hasCandidacyWithMissingInformation(final ExecutionYear executionYear) {
	return hasValidCandidacy() && !getCandidacy().getCandidacyInformationBean().isValid();
    }

    private boolean hasValidCandidacy() {
	return hasCandidacy() && getCandidacy().isActive() && !getCandidacy().hasRegistration() && isCandidacyCorrectlyAttached();
    }

    private boolean isCandidacyCorrectlyAttached() {
	return getCandidacy().hasExecutionDegree() || hasPhdProgram() || hasPhdProgramFocusArea();
    }

    public CandidacyInformationBean getCandidacyInformationBean() {
	return getCandidacy().getCandidacyInformationBean();
    }

    public void deleteLastState() {
	if (getStatesCount() <= 1) {
	    throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.cannot.delete.the.only.state");
	}

	getMostRecentState().delete();
    }

    public PhdCandidacyReferee getCandidacyRefereeByEmail(final String email) {
	List<PhdCandidacyReferee> candidacyReferees = getCandidacyReferees();

	for (PhdCandidacyReferee phdCandidacyReferee : candidacyReferees) {
	    if (phdCandidacyReferee.getEmail().trim().equals(email.trim())) {
		return phdCandidacyReferee;
	    }
	}

	return null;
    }
}
