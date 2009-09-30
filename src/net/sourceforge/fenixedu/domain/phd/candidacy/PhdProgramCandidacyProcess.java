package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdRegistrationFee;
import net.sourceforge.fenixedu.domain.phd.alert.PhdFinalProofRequestAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotificationBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdProgramCandidacyProcess> {

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
	    final PhdProgramCandidacyProcess result = new PhdProgramCandidacyProcess(bean, person);
	    result.createState(bean.getState(), person);
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
	    final List<PhdCandidacyDocumentUploadBean> documents = (List<PhdCandidacyDocumentUploadBean>) object;

	    for (final PhdCandidacyDocumentUploadBean each : documents) {
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
	    ((PhdProgramCandidacyProcessDocument) object).delete();

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

	    if (process.getCandidacyReviewDocuments().isEmpty()) {
		throw new DomainException(
			"error.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy.candidacy.review.document.is.required");
	    }

	    process.createState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION,
		    userView.getPerson(), bean.getRemarks());
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
	    return process;
	}

    }

    static public class RequestCandidacyReview extends PhdActivity {

	static final private List<PhdProgramCandidacyProcessState> PREVIOUS_STATE = Arrays.asList(

	PhdProgramCandidacyProcessState.PRE_CANDIDATE,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION,

	PhdProgramCandidacyProcessState.REJECTED);

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (PREVIOUS_STATE.contains(process.getActiveState())) {
		return;
	    }
	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {

	    if (!process.getIndividualProgramProcess().hasPhdProgram()) {
		throw new DomainException(
			"error.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview.invalid.phd.program");
	    }

	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
	    process.createState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION, userView.getPerson(), bean
		    .getRemarks());
	    return process;
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

	    for (final PhdCandidacyDocumentUploadBean each : (List<PhdCandidacyDocumentUploadBean>) object) {
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
	    if (process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		if ((isMasterDegreeAdministrativeOfficeEmployee(userView) || process.getIndividualProgramProcess()
			.isCoordinatorForPhdProgram(userView.getPerson()))) {
		    return;
		}
	    }

	    throw new PreConditionNotValidException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.DeleteCandidacyReview.cannot.delete.review.after.sending.for.ratification");

	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    PhdProgramCandidacyProcessDocument document = (PhdProgramCandidacyProcessDocument) object;

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

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
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
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
	super();

	checkCandidacyDate(bean.getExecutionYear(), bean.getCandidacyDate());
	setCandidacyDate(bean.getCandidacyDate());
	setValidatedByCandidate(false);

	setCandidacyHashCode(bean.getCandidacyHashCode());
	setCandidacy(new PHDProgramCandidacy(person));

	if (bean.hasDegree()) {
	    getCandidacy().setExecutionDegree(bean.getExecutionDegree());
	}

	if (!bean.hasCollaborationType() || bean.getCollaborationType().generateCandidacyDebt()) {
	    new PhdProgramCandidacyEvent(person, this);
	}
    }

    public boolean isPublicCandidacy() {
	return hasCandidacyHashCode();
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
	getEvent().cancel(employee);
    }

    public String getProcessNumber() {
	return getIndividualProgramProcess().getProcessNumber();
    }

    public Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    public List<PhdProgramCandidacyProcessDocument> getCandidacyReviewDocuments() {
	final List<PhdProgramCandidacyProcessDocument> documents = new ArrayList<PhdProgramCandidacyProcessDocument>();
	for (final PhdProgramCandidacyProcessDocument document : getDocuments()) {
	    if (document.hasType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW)) {
		documents.add(document);
	    }
	}
	return documents;
    }

    public boolean hasAnyDocuments(final PhdIndividualProgramDocumentType type) {
	for (final PhdProgramCandidacyProcessDocument document : getDocuments()) {
	    if (document.hasType(type)) {
		return true;
	    }
	}
	return false;
    }

    public int getDocumentsCount(final PhdIndividualProgramDocumentType type) {
	int total = 0;
	for (final PhdProgramCandidacyProcessDocument document : getDocuments()) {
	    if (document.hasType(type)) {
		total++;
	    }
	}
	return total;
    }

    public boolean isValidatedByCandidate() {
	return getValidatedByCandidate() != null && getValidatedByCandidate().booleanValue();
    }

    public Set<PhdProgramCandidacyProcessDocument> getStudyPlanRelevantDocuments() {
	final Set<PhdProgramCandidacyProcessDocument> result = new HashSet<PhdProgramCandidacyProcessDocument>();

	for (final PhdProgramCandidacyProcessDocument each : getDocuments()) {
	    if (each.hasType(PhdIndividualProgramDocumentType.STUDY_PLAN)
		    || each.hasType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW)) {
		result.add(each);
	    }
	}

	return result;
    }

    public void createState(final PhdProgramCandidacyProcessState state, final Person person) {
	createState(state, person, null);
    }

    public void createState(final PhdProgramCandidacyProcessState state, final Person person, final String remarks) {
	new PhdCandidacyProcessState(this, state, person, remarks);
    }

    private PhdCandidacyProcessState getMostRecentState() {
	return hasAnyStates() ? Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public PhdProgramCandidacyProcessState getActiveState() {
	final PhdCandidacyProcessState state = getMostRecentState();
	return (state != null) ? state.getType() : null;
    }

    public String getActiveStateRemarks() {
	return getMostRecentState().getRemarks();
    }

    public boolean isInState(final PhdProgramCandidacyProcessState state) {
	return getActiveState().equals(state);
    }

    public boolean hasState(PhdProgramCandidacyProcessState type) {
	final List<PhdCandidacyProcessState> states = new ArrayList<PhdCandidacyProcessState>(getStates());
	Collections.sort(states, PhdCandidacyProcessState.COMPARATOR_BY_DATE);

	for (final PhdCandidacyProcessState state : states) {
	    if (state.getType() == type) {
		return true;
	    }
	}

	return false;
    }

    public void addDocument(PhdCandidacyDocumentUploadBean each, Person responsible) {
	if (!each.getType().isMultipleDocumentsAllowed()) {
	    removeDocumentsByType(each.getType());
	}

	new PhdProgramCandidacyProcessDocument(this, each.getType(), each.getRemarks(), each.getFileContent(),
		each.getFilename(), responsible);
    }

    public void ratify(RatifyCandidacyBean bean, Person responsible) {

	check(bean.getWhenRatified(), "error.phd.candidacy.PhdProgramCandidacyProcess.when.ratified.cannot.be.null");

	if (!bean.getRatificationFile().hasAnyInformation()) {
	    throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.ratification.document.is.required");
	}

	setWhenRatified(bean.getWhenRatified());
	addDocument(bean.getRatificationFile(), responsible);

	if (!getIndividualProgramProcess().hasAnyRegistrationFormalizationActiveAlert()) {
	    new PhdRegistrationFormalizationAlert(getIndividualProgramProcess());
	}

	createState(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL, responsible);

    }

    public void removeDocumentsByType(PhdIndividualProgramDocumentType type) {
	for (final PhdProgramCandidacyProcessDocument each : getDocuments()) {
	    if (each.getDocumentType() == type) {
		each.delete();
	    }
	}
    }

    private PhdProgramCandidacyProcess registrationFormalization(final RegistrationFormalizationBean bean,
	    final Person responsible) {

	createState(PhdProgramCandidacyProcessState.CONCLUDED, responsible);

	getIndividualProgramProcess().setWhenFormalizedRegistration(new LocalDate());
	getIndividualProgramProcess().setWhenStartedStudies(bean.getWhenStartedStudies());

	assertPersonInformation();

	final ExecutionYear executionYear = ExecutionYear.readByDateTime(bean.getWhenStartedStudies());

	if (hasCurricularStudyPlan()) {
	    final DegreeCurricularPlan dcp = getLastActiveDegreeCurricularPlan();
	    assertCandidacy(dcp, executionYear);
	    assertRegistration(bean, dcp, executionYear);
	}

	assertDebts(executionYear);
	assertRegistrationFormalizationAlerts();

	getIndividualProgramProcess().createState(PhdIndividualProgramProcessState.WORK_DEVELOPMENT, responsible);

	return this;
    }

    private void assertPersonInformation() {
	final Person person = getPerson();

	if (!person.hasStudent()) {
	    new Student(person);
	}

	person.addPersonRoleByRoleType(RoleType.PERSON);
	person.addPersonRoleByRoleType(RoleType.STUDENT);
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

	getCandidacy().setIngression(Ingression.CIA3C); // TODO: check
    }

    private void assertRegistration(final RegistrationFormalizationBean bean, final DegreeCurricularPlan dcp,
	    final ExecutionYear executionYear) {

	if (getPerson().getStudent().hasActiveRegistrationFor(dcp)) {
	    throw new DomainException("error.PhdProgramCandidacyProcess.regisration.formalization.already.has.registration");
	}

	final Registration registration = new Registration(getPerson(), dcp, getCandidacy(), RegistrationAgreement.NORMAL, null,
		executionYear);

	registration.setHomologationDate(getWhenRatified());
	registration.setStudiesStartDate(bean.getWhenStartedStudies());
	registration.setPhdIndividualProgramProcess(getIndividualProgramProcess());
    }

    private void assertDebts(final ExecutionYear executionYear) {
	assertPhdRegistrationFee();
	assertInsuranceEvent(executionYear);
    }

    private void assertPhdRegistrationFee() {
	new PhdRegistrationFee(getPerson(), getIndividualProgramProcess());
    }

    private void assertInsuranceEvent(final ExecutionYear executionYear) {
	if (!getPerson().hasInsuranceEventFor(executionYear)
		&& !getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
	    new InsuranceEvent(getPerson(), executionYear);
	}
    }

    private void assertRegistrationFormalizationAlerts() {
	new PhdPublicPresentationSeminarAlert(getIndividualProgramProcess());
	new PhdFinalProofRequestAlert(getIndividualProgramProcess());
    }

    private boolean hasCurricularStudyPlan() {
	return getIndividualProgramProcess().hasStudyPlan()
		&& !getIndividualProgramProcess().getStudyPlan().getExempted().booleanValue();
    }

    private DegreeCurricularPlan getLastActiveDegreeCurricularPlan() {
	return getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan();
    }

    private PhdProgram getPhdProgram() {
	return getIndividualProgramProcess().getPhdProgram();
    }

}
