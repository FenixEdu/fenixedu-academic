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
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdProgramCandidacyProcess> {

	@Override
	final public void checkPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	    // if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    // throw new PreConditionNotValidException();
	    // }
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
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView arg1) {
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

    static public class RatifyCandidacy extends PhdActivity {
	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {

	    process.ratify((RatifyCandidacyBean) object, userView != null ? userView.getPerson() : null);

	    return process;
	}

    }

    static public class RequestCandidacyReview extends PhdActivity {

	static final private List<PhdProgramCandidacyProcessState> PREVIOUS_STATE = Arrays.asList(

	PhdProgramCandidacyProcessState.PRE_CANDIDATE,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION,

	PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION,

	PhdProgramCandidacyProcessState.WAITING_FOR_CIENTIFIC_COUNCIL_RATIFICATION);

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (PREVIOUS_STATE.contains(process.getActiveState())) {
		return;
	    }
	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    process.createState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION, userView.getPerson());
	    return process;
	}
    }

    static public class UploadCandidacyReview extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)
		    || process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		return;
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
	activities.add(new RatifyCandidacy());

	activities.add(new RequestCandidacyReview());
	activities.add(new UploadCandidacyReview());
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
	super();

	checkCandidacyDate(bean.getExecutionYear(), bean.getCandidacyDate());
	setCandidacyDate(bean.getCandidacyDate());
	setValidatedByCandidate(false);

	// TODO: receive person as argument?
	// TODO: public candidacies, do not create student and user: pay
	// attention to public candidacies
	// if (!person.hasStudent()) {
	// TODO: generate when creating registration?
	// new Student(person);
	// }
	// person.setIstUsername();

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
	// TODO: add some state to hash code to be changed after create some
	// student and identification or when changed by other entity?
	return hasCandidacyHashCode();
    }

    private void checkCandidacyDate(ExecutionYear executionYear, LocalDate candidacyDate) {
	check(candidacyDate, "error.phd.candidacy.PhdProgramCandidacyProcess.invalid.candidacy.date");
	if (!executionYear.containsDate(candidacyDate)) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.executionYear.doesnot.contains.candidacy.date", candidacyDate
			    .toString("dd/MM/yyyy"), executionYear.getQualifiedName(), executionYear.getBeginDateYearMonthDay()
			    .toString("dd/MM/yyyy"), executionYear.getEndDateYearMonthDay().toString("dd/MM/yyyy"));
	}
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
	new PhdCandidacyProcessState(this, state, person, null);
    }

    public PhdProgramCandidacyProcessState getActiveState() {
	final PhdCandidacyProcessState state = Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE);
	return (state != null) ? state.getType() : null;
    }

    public boolean isInState(final PhdProgramCandidacyProcessState state) {
	return getActiveState().equals(state);
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
    }

    public void removeDocumentsByType(PhdIndividualProgramDocumentType type) {
	for (final PhdProgramCandidacyProcessDocument each : getDocuments()) {
	    if (each.getDocumentType() == type) {
		each.delete();
	    }
	}
    }

}
