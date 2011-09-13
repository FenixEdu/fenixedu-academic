package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Alert;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdFinalProofRequestAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PublicPhdMissingCandidacyValidationAlert;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.phd.guidance.PhdGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AcceptEnrolments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInCandidacyState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInThesisDiscussionState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInWorkDevelopmentState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddCustomAlert;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingsInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddJobInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddQualifications;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.CancelPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ConfigurePhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteAssistantGuiding;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteCustomAlert;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteJobInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DissociateRegistration;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditQualificationExams;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditWhenStartedStudies;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.FlunkedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.NotAdmittedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RejectEnrolments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RemoveCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RemoveLastStateOnPhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicThesisPresentation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SendPhdEmail;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SuspendPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.TransferToAnotherProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ValidatedByCandidate;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationGuiding;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessDataBean;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static abstract protected class PhdActivity extends Activity<PhdIndividualProgramProcess> {

	@Override
	final public void checkPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	    if (process != null && !process.getActiveState().isActive()) {
		throw new PreConditionNotValidException();
	    }
	}

	protected void email(String email, String subject, String body) {
	    final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	    new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
	}

	abstract protected void activityPreConditions(final PhdIndividualProgramProcess process, final IUserView userView);
    }

    static protected List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditPersonalInformation());

	activities.add(new AddQualification());
	activities.add(new AddQualifications());
	activities.add(new DeleteQualification());

	activities.add(new AddJobInformation());
	activities.add(new DeleteJobInformation());

	activities.add(new EditIndividualProcessInformation());

	activities.add(new AddGuidingInformation());
	activities.add(new AddGuidingsInformation());
	activities.add(new DeleteGuiding());
	activities.add(new AddAssistantGuidingInformation());
	activities.add(new DeleteAssistantGuiding());

	activities.add(new CancelPhdProgramProcess());
	activities.add(new NotAdmittedPhdProgramProcess());
	activities.add(new SuspendPhdProgramProcess());
	activities.add(new FlunkedPhdProgramProcess());

	activities.add(new AddCandidacyReferees());
	activities.add(new RemoveCandidacyReferee());
	activities.add(new UploadDocuments());

	activities.add(new AddCustomAlert());
	activities.add(new DeleteCustomAlert());

	activities.add(new ValidatedByCandidate());

	activities.add(new AddStudyPlan());
	activities.add(new EditStudyPlan());
	activities.add(new AddStudyPlanEntry());
	activities.add(new DeleteStudyPlanEntry());
	activities.add(new DeleteStudyPlan());

	activities.add(new EditQualificationExams());

	activities.add(new RequestPublicPresentationSeminarComission());
	activities.add(new ExemptPublicPresentationSeminarComission());

	activities.add(new RequestPublicThesisPresentation());

	activities.add(new AcceptEnrolments());
	activities.add(new RejectEnrolments());

	activities.add(new EditWhenStartedStudies());

	activities.add(new ActivatePhdProgramProcessInCandidacyState());
	activities.add(new ActivatePhdProgramProcessInWorkDevelopmentState());
	activities.add(new ActivatePhdProgramProcessInThesisDiscussionState());

	activities.add(new ConfigurePhdIndividualProgramProcess());
	activities.add(new RemoveLastStateOnPhdIndividualProgramProcess());

	activities.add(new SendPhdEmail());

	activities.add(new UploadGuidanceDocument());
	activities.add(new EditPhdParticipant());
	activities.add(new TransferToAnotherProcess());
	activities.add(new DissociateRegistration());
    }

    @StartActivity
    static public class CreateCandidacy extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, IUserView userView,
		Object object) {

	    final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) object;
	    final Person person = getOrCreatePerson(bean);

	    final PhdIndividualProgramProcess createdProcess = new PhdIndividualProgramProcess(bean, person);
	    final PhdProgramCandidacyProcess candidacyProcess = Process.createNewProcess(userView,
		    PhdProgramCandidacyProcess.class, new Object[] { bean, person });

	    candidacyProcess.setIndividualProgramProcess(createdProcess);
	    if (bean.hasCandidacyHashCode()) {
		new PublicPhdMissingCandidacyValidationAlert(createdProcess);
	    }

	    createdProcess.createState(PhdIndividualProgramProcessState.CANDIDACY, person, "");

	    return createdProcess;
	}

	protected Person getOrCreatePerson(final PhdProgramCandidacyProcessBean bean) {
	    Person result;

	    if (!bean.getPersonBean().hasPerson()) {
		result = new Person(bean.getPersonBean());
	    } else {
		if (bean.getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)
			|| bean.getPersonBean().getPerson().hasAnyPersonRoles() || bean.getPersonBean().getPerson().hasUser()
			|| bean.getPersonBean().getPerson().hasStudent() || bean.hasInstitutionId()) {
		    result = bean.getPersonBean().getPerson();
		} else {
		    /*
		     * if person never had any identity in the system then let
		     * edit information
		     */
		    result = bean.getPersonBean().getPerson().edit(bean.getPersonBean());
		}
	    }
	    return result;
	}
    }

    private PhdIndividualProgramProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
	super();

	checkParameters(person, bean.getExecutionYear());
	setPerson(person);

	setPhdProgramFocusArea(bean.getFocusArea());
	setPhdProgram(bean.getProgram());
	setExternalPhdProgram(bean.getExternalPhdProgram());

	setExecutionYear(bean.getExecutionYear());

	setCollaborationType(bean);
	setThesisTitle(bean.getThesisTitle());

	if (bean.getMigratedProcess()) {
	    setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(
		    bean.getCandidacyDate().getYear(), bean.getPhdStudentNumber()));
	    setPhdConfigurationIndividualProgramProcess(PhdConfigurationIndividualProgramProcess
		    .createMigratedProcessConfiguration());
	} else {
	    setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(
		    bean.getCandidacyDate().getYear(), null));
	    setPhdConfigurationIndividualProgramProcess(PhdConfigurationIndividualProgramProcess.createDefault());
	}

	updatePhdParticipantsWithCoordinators();
    }

    public void removeLastState() {
	if (getStatesCount() == 1) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.has.only.one.state");
	}

	getMostRecentState().delete();
    }

    /*
     * 
     * TODO: Refactor -> Participants should be shared at PhdProcessesManager
     * level, and each PhdProgram should also have phdparticipants as
     * coordinators
     */
    private void updatePhdParticipantsWithCoordinators() {
	for (final Person person : getCoordinatorsFor(getExecutionYear())) {
	    if (getParticipant(person) == null) {
		PhdParticipant.getUpdatedOrCreate(this, new PhdParticipantBean().setInternalParticipant(person));
	    }
	}
    }

    public void createState(final PhdIndividualProgramProcessState state, final Person person, final String remarks) {
	PhdProgramProcessState.createWithInferredStateDate(this, state, person, remarks);
    }

    private void setCollaborationType(PhdProgramCandidacyProcessBean bean) {
	if (bean.getCollaborationType() != null) {
	    setCollaborationType(bean.getCollaborationType());
	} else {
	    setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	}
    }

    private void checkParameters(final Person person, final ExecutionYear executionYear) {

	check(person, "error.phd.PhdIndividualProgramProcess.person.cannot.be.null");
	check(executionYear, "error.phd.PhdIndividualProgramProcess.executionYear.cannot.be.null");
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return true;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    public String getCollaborationTypeName() {
	return getCollaborationType().getLocalizedName()
		+ (isOtherCollaborationType() ? " (" + getOtherCollaborationType() + ")" : "");
    }

    private boolean isOtherCollaborationType() {
	return getCollaborationType() == PhdIndividualProgramCollaborationType.OTHER;
    }

    public PhdIndividualProgramProcess addQualification(final Person person, final QualificationBean bean) {
	final Qualification qualification = new Qualification(getPerson(), bean);
	qualification.setCreator(person);
	return this;
    }

    public PhdIndividualProgramProcess addJobInformation(final Person person, final JobBean bean) {
	final Job job = new Job(getPerson(), bean);
	job.setCreator(person);
	return this;
    }

    public PhdParticipant addGuiding(final PhdParticipantBean bean) {
	PhdParticipant phdParticipant = bean.hasParticipant() ? bean.getParticipant() : createPhdParticipant(bean);
	addGuidings(phdParticipant);
	return phdParticipant;
    }

    public PhdIndividualProgramProcess deleteGuiding(final PhdParticipant guiding) {
	if (hasGuidings(guiding)) {
	    removeGuidings(guiding);
	    guiding.tryDelete();
	}
	return this;
    }

    public PhdIndividualProgramProcess addAssistantGuiding(final PhdParticipantBean bean) {
	addAssistantGuidings(bean.hasParticipant() ? bean.getParticipant() : createPhdParticipant(bean));
	return this;
    }

    private PhdParticipant createPhdParticipant(final PhdParticipantBean bean) {
	return PhdParticipant.getUpdatedOrCreate(this, bean);
    }

    public PhdIndividualProgramProcess deleteAssistantGuiding(final PhdParticipant assistant) {
	if (hasAssistantGuidings(assistant)) {
	    removeAssistantGuidings(assistant);
	    assistant.tryDelete();
	}
	return this;
    }

    public String getProcessNumber() {
	return getPhdIndividualProcessNumber().getFullProcessNumber();
    }

    public PhdIndividualProgramProcess edit(final IUserView userView, final PhdIndividualProgramProcessBean bean) {

	checkParameters(getPerson(), getExecutionYear());

	if (hasCandidacyProcess() && !getCandidacyDate().equals(bean.getCandidacyDate())) {
	    getCandidacyProcess().executeActivity(userView, PhdProgramCandidacyProcess.EditCandidacyDate.class.getSimpleName(),
		    bean.getCandidacyDate());
	}

	setPhdProgram(bean.getPhdProgram());
	setPhdProgramFocusArea(bean.getFocusArea());

	setThesisTitle(bean.getThesisTitle());
	setCollaborationType(bean.getCollaborationType());

	if (bean.getCollaborationType().needExtraInformation()) {
	    check(bean.getOtherCollaborationType(), "error.PhdIndividualProgramProcess.invalid.other.collaboration.type");
	    setOtherCollaborationType(bean.getOtherCollaborationType());
	}

	setQualificationExamsRequired(bean.getQualificationExamsRequiredBooleanValue());
	setQualificationExamsPerformed(bean.getQualificationExamsPerformedBooleanValue());

	if (isRegistrationFormalized()) {
	    getCandidacyProcess().setWhenRatified(bean.getWhenRatified());
	    setWhenFormalizedRegistration(bean.getWhenFormalizedRegistration());
	}

	getPhdIndividualProcessNumber().edit(bean);

	setLatexThesisTitle(bean.getLatexThesisTitle());

	return this;
    }

    public LocalDate getCandidacyDate() {
	return getCandidacyProcess().getCandidacyDate();
    }

    public boolean isCancelled() {
	return getActiveState() == PhdIndividualProgramProcessState.CANCELLED;
    }

    public List<PhdCandidacyReferee> getPhdCandidacyReferees() {
	return getCandidacyProcess().getCandidacyReferees();
    }

    public List<Qualification> getQualifications() {
	return getPerson().getAssociatedQualifications();
    }

    public List<Qualification> getQualificationsSortedByAttendedEndDate() {
	final List<Qualification> result = new ArrayList<Qualification>(getQualifications());
	Collections.sort(result, Qualification.COMPARATOR_BY_MOST_RECENT_ATTENDED_END);
	return result;
    }

    public List<PhdProgramProcessDocument> getCandidacyProcessDocuments() {
	return new ArrayList<PhdProgramProcessDocument>(getCandidacyProcess().getLatestDocumentVersions());
    }

    public boolean hasCandidacyProcessDocument(final PhdIndividualProgramDocumentType type) {
	return getCandidacyProcess().getLatestDocumentVersionFor(type) != null;
    }

    public int getCandidacyProcessDocumentsCount(final PhdIndividualProgramDocumentType type) {
	return getCandidacyProcess().getDocumentsCount(type);
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyProcessHashCode() {
	return getCandidacyProcess().getCandidacyHashCode();
    }

    static public List<PhdIndividualProgramProcess> search(Predicate<PhdIndividualProgramProcess> searchPredicate) {
	return search(null, searchPredicate);
    }

    static public List<PhdIndividualProgramProcess> search(final ExecutionYear year,
	    final Predicate<PhdIndividualProgramProcess> searchPredicate) {

	final Set<PhdIndividualProgramProcess> processesToSearch = new HashSet<PhdIndividualProgramProcess>();
	for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : RootDomainObject.getInstance()
		.getPhdIndividualProcessNumbers()) {
	    if (year == null || phdIndividualProgramProcessNumber.getProcess().getExecutionYear().equals(year)) {
		processesToSearch.add(phdIndividualProgramProcessNumber.getProcess());
	    }
	}

	return CollectionUtils.filter(processesToSearch, searchPredicate);
    }

    public Set<PhdAlert> getActiveAlerts() {
	final Set<PhdAlert> result = new HashSet<PhdAlert>();

	for (final PhdAlert each : getAlerts()) {
	    if (each.isActive()) {
		result.add(each);
	    }
	}

	return result;
    }

    public Set<PhdAlertMessage> getUnreadAlertMessagesForLoggedPerson() {
	return getUnreadedAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getUnreadedAlertMessagesFor(final Person person) {
	final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

	for (final PhdAlertMessage each : getAlertMessages()) {
	    if (!each.isReaded() && each.isFor(person)) {
		result.add(each);
	    }
	}

	return result;
    }

    public Set<PhdAlertMessage> getAlertMessagesForLoggedPerson() {
	return getAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getAlertMessagesFor(Person person) {
	final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

	for (final PhdAlertMessage each : getAlertMessages()) {
	    if (each.isFor(person)) {
		result.add(each);
	    }
	}

	return result;
    }

    public boolean isValidatedByCandidate() {
	return getCandidacyProcess().isValidatedByCandidate();
    }

    public Set<PhdProgramProcessDocument> getStudyPlanRelevantDocuments() {
	return getCandidacyProcess().getStudyPlanRelevantDocuments();
    }

    public boolean isRegistrationFormalized() {
	return getWhenFormalizedRegistration() != null;
    }

    private boolean hasAnyActiveAlertFor(Class<? extends PhdAlert> type) {
	for (final Alert alert : getActiveAlerts()) {
	    if (alert.getClass().equals(type)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyRegistrationFormalizationActiveAlert() {
	return hasAnyActiveAlertFor(PhdRegistrationFormalizationAlert.class);
    }

    @Override
    public PhdProgramProcessState getMostRecentState() {
	return (PhdProgramProcessState) super.getMostRecentState();
    }

    @Override
    public PhdIndividualProgramProcessState getActiveState() {
	return (PhdIndividualProgramProcessState) super.getActiveState();
    }

    public Student getStudent() {
	return getPerson().getStudent();
    }

    public boolean isCoordinatorForPhdProgram(Person person) {
	final ExecutionDegree executionDegree = getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan()
		.getExecutionDegreeByAcademicInterval(ExecutionYear.readCurrentExecutionYear().getAcademicInterval());

	if (executionDegree != null) {
	    for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
		if (coordinator.getPerson() == person) {
		    return true;
		}
	    }
	}

	return false;
    }

    public boolean isGuiderOrAssistentGuider(Person person) {
	return isGuider(person) || isAssistantGuider(person);
    }

    public boolean isGuider(Person person) {
	for (final PhdParticipant guiding : getGuidings()) {
	    if (guiding.isFor(person)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isGuider(PhdParticipant participant) {
	return hasGuidings(participant);
    }

    public boolean isAssistantGuider(Person person) {
	for (final PhdParticipant guiding : getAssistantGuidings()) {
	    if (guiding.isFor(person)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isAssistantGuider(PhdParticipant participant) {
	return hasAssistantGuidings(participant);
    }

    public boolean isRegistrationAvailable() {
	return hasRegistration();
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
	return this;
    }

    public Set<PhdParticipant> getGuidingsAndAssistantGuidings() {
	final Set<PhdParticipant> result = new HashSet<PhdParticipant>();
	result.addAll(getAssistantGuidings());
	result.addAll(getGuidings());

	return result;
    }

    public void cancelDebts(final Person person) {
	if (hasCandidacyProcess() && !getCandidacyProcess().hasAnyPayments()) {
	    getCandidacyProcess().cancelDebt(person.getEmployee());
	}

	if (hasRegistrationFee() && !getRegistrationFee().hasAnyPayments()) {
	    getRegistrationFee().cancel(person.getEmployee());
	}
    }

    public boolean hasSchoolPartConcluded() {
	boolean concluded = hasRegistration() && (getRegistration().isSchoolPartConcluded() || getRegistration().isConcluded());
	return (hasStudyPlan() && getStudyPlan().isExempted()) || concluded;
    }

    public boolean hasQualificationExamsToPerform() {
	return isQualificationExamsRequired() && !isQualificationExamsPerformed();
    }

    private boolean isQualificationExamsPerformed() {
	return getQualificationExamsPerformed() != null && getQualificationExamsPerformed();
    }

    private boolean isQualificationExamsRequired() {
	return getQualificationExamsRequired() != null && getQualificationExamsRequired();
    }

    public boolean hasSeminarReportDocument() {
	return hasSeminarProcess() && getSeminarProcess().hasReportDocument();
    }

    public Set<Person> getCoordinatorsFor(ExecutionYear executionYear) {
	return getPhdProgram().getCoordinatorsFor(executionYear);
    }

    public Set<Person> getResponsibleCoordinatorsFor(ExecutionYear executionYear) {
	return getPhdProgram().getResponsibleCoordinatorsFor(executionYear);
    }

    public boolean hasPhdPublicPresentationSeminarAlert() {
	return hasPhdAlert(PhdPublicPresentationSeminarAlert.class);
    }

    public boolean hasPhdFinalProofRequestAlert() {
	return hasPhdAlert(PhdFinalProofRequestAlert.class);
    }

    protected boolean hasPhdAlert(final Class<? extends PhdAlert> clazz) {
	for (final Alert alert : getAlerts()) {
	    if (clazz.isAssignableFrom(alert.getClass())) {
		return true;
	    }
	}
	return false;
    }

    public PhdParticipant getParticipant(final Person person) {
	for (final PhdParticipant participant : getParticipants()) {
	    if (participant.isFor(person)) {
		return participant;
	    }
	}
	return null;
    }

    public boolean isParticipant(final Person person) {
	return getParticipant(person) != null;
    }

    public Collection<PhdProcessStateType> getAllPhdProcessStateTypes() {
	final Collection<PhdProcessStateType> result = new HashSet<PhdProcessStateType>();

	if (hasCandidacyProcess()) {

	    result.add(getCandidacyProcess().getActiveState());

	    if (getCandidacyProcess().hasFeedbackRequest()) {
		result.add(getCandidacyProcess().getFeedbackRequest().getActiveState());
	    }
	}

	if (hasSeminarProcess()) {
	    result.add(getSeminarProcess().getActiveState());
	}

	if (hasThesisProcess()) {
	    result.add(getThesisProcess().getActiveState());
	}

	return result;
    }

    public boolean hasCandidacyWithMissingInformation(final ExecutionYear executionYear) {
	return hasCandidacyProcess() && getCandidacyProcess().hasCandidacyWithMissingInformation(executionYear);
    }

    public CandidacyInformationBean getCandidacyInformationBean() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyInformationBean() : null;
    }

    public Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol() {

	if (!hasStudyPlan()) {
	    return Collections.emptySet();
	}

	final Collection<CompetenceCourse> result = new HashSet<CompetenceCourse>();
	for (PhdStudyPlanEntry entry : getStudyPlan().getEntries()) {
	    if (entry.isInternalEntry()) {
		result.add(((InternalPhdStudyPlanEntry) entry).getCompetenceCourse());
	    }
	}
	return result;
    }

    public LocalDate getConclusionDate() {
	if (isConclusionProcessed()) {
	    return getLastConclusionProcess().getConclusionDate();
	}

	return hasThesisProcess() ? getThesisProcess().getConclusionDate() : null;
    }

    public boolean hasCurricularCoursesToEnrol() {
	return hasStudyPlan() && !getStudyPlan().isExempted() && getStudyPlan().isToEnrolInCurricularCourses();
    }

    public boolean hasPropaeudeuticsOrExtraEntriesApproved() {

	if (!hasStudyPlan() || !hasRegistration()) {
	    return false;
	}

	return getStudyPlan().isExempted() || getStudyPlan().hasPropaeudeuticsOrExtraEntriesApproved();
    }

    public boolean isFlunked() {
	return getMostRecentState().isFlunked();
    }

    public boolean isSuspended() {
	return getMostRecentState().isSuspended();
    }

    protected List<PhdProgramProcessState> getActiveStates() {
	List<PhdProgramProcessState> result = new ArrayList<PhdProgramProcessState>();
	org.apache.commons.collections.CollectionUtils.select(getStates(), new org.apache.commons.collections.Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((PhdProgramProcessState) arg0).getType().isActive();
	    }

	}, result);

	return result;
    }

    protected boolean hasActiveStates() {
	return !getActiveStates().isEmpty();
    }

    public PhdProgramProcessState getLastActiveState() {
	return hasActiveStates() ? Collections.max(getActiveStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public boolean isMigratedProcess() {
	return getPhdConfigurationIndividualProgramProcess().isMigratedProcess();
    }

    public Integer getPhdStudentNumber() {
	return getPhdIndividualProcessNumber().getPhdStudentNumber();
    }

    @Override
    public boolean isProcessIndividualProgram() {
	return true;
    }

    public boolean isConcluded() {
	return hasThesisProcess() && getThesisProcess().isConcluded();
    }

    public boolean isInWorkDevelopment() {
	for (PhdProgramProcessState state: this.getActiveStates()) {
	    if (state.getType().equals(PhdIndividualProgramProcessState.WORK_DEVELOPMENT)){
		return true;
	    }
	}
	return false;
    }
    
    public PhdThesisFinalGrade getFinalGrade() {
	if (!isConcluded()) {
	    return null;
	}

	if (isConclusionProcessed()) {
	    return getLastConclusionProcess().getFinalGrade();
	}

	return getThesisProcess().getFinalGrade();
    }

    public Set<PhdGuidanceDocument> getLatestGuidanceDocumentVersions() {
	Set<PhdGuidanceDocument> documents = new HashSet<PhdGuidanceDocument>();

	org.apache.commons.collections.CollectionUtils.select(getLatestDocumentVersions(),
		new org.apache.commons.collections.Predicate() {

		    @Override
		    public boolean evaluate(Object arg0) {
			return ((PhdProgramProcessDocument) arg0).getDocumentType().isForGuidance();
		    }

		}, documents);

	return documents;
    }

    public static class PublicPhdIndividualProgramProcess extends PhdIndividualProgramProcess {
	static {
	    activities.add(new CreatePublicCandidacy());
	}

	// Prevent instantiation of this class
	private PublicPhdIndividualProgramProcess() {
	    super(null, null);
	}

	@StartActivity
	static public class CreatePublicCandidacy extends CreateCandidacy {

	    @Override
	    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		// no precondition to check
	    }

	    @Override
	    protected Person getOrCreatePerson(final PhdProgramCandidacyProcessBean bean) {
		Person result;

		if (!bean.getPersonBean().hasPerson()) {
		    result = new Person(bean.getPersonBean());
		} else {
		    if (bean.getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)
			    || bean.getPersonBean().getPerson().hasAnyPersonRoles() || bean.getPersonBean().getPerson().hasUser()
			    || bean.getPersonBean().getPerson().hasStudent() || bean.hasInstitutionId()) {
			result = bean.getPersonBean().getPerson();
		    } else {
			/*
			 * if person never had any identity in the system then
			 * let edit information
			 */
			result = bean.getPersonBean().getPerson().editByPublicCandidate(bean.getPersonBean());
		    }
		}
		return result;
	    }
	}

    }

    public boolean hasAssociatedMigrationProcess() {
	return getAssociatedMigrationProcess() != null;
    }

    public PhdMigrationIndividualProcessData getAssociatedMigrationProcess() {
	if (!isMigratedProcess() || getPhdStudentNumber() == null) {
	    return null;
	}

	for (final PhdMigrationProcess migrationProcess : RootDomainObject.getInstance().getPhdMigrationProcesses()) {
	    for (final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessData()) {
		if (processData.getNumber().equals(getPhdStudentNumber())) {
		    return processData;
		}
	    }
	}

	return null;
    }

    public PhdMigrationGuiding getAssociatedMigrationGuiding() {
	if (!hasAssociatedMigrationProcess()) {
	    return null;
	}

	final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

	return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getGuiderNumber());
    }

    public PhdMigrationGuiding getAssociatedMigrationAssistantGuiding() {
	if (!hasAssociatedMigrationProcess()) {
	    return null;
	}

	final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

	return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getAssistantGuiderNumber());
    }

    private PhdMigrationGuiding getAssociatedMigrationgGuidingOrAssistant(String guiderNumber) {
	for (final PhdMigrationProcess migrationProcess : RootDomainObject.getInstance().getPhdMigrationProcesses()) {
	    for (final PhdMigrationGuiding guidingData : migrationProcess.getPhdMigrationGuiding()) {
		if (guidingData.getTeacherNumber().equals(guiderNumber)) {
		    return guidingData;
		}
	    }
	}

	return null;
    }

    static public List<PhdMigrationIndividualProcessData> searchMigrationProcesses(final ExecutionYear year,
	    final Predicate<PhdMigrationIndividualProcessData> searchPredicate) {
	final List<PhdMigrationIndividualProcessData> processDataList = new ArrayList<PhdMigrationIndividualProcessData>();

	for (final PhdMigrationProcess migrationProcess : RootDomainObject.getInstance().getPhdMigrationProcesses()) {
	    for (final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessData()) {
		final ExecutionYear processYear = processData.getExecutionYear();
		if (processYear == null || year == null || processYear.equals(year)) {
		    processDataList.add(processData);
		}
	    }
	}

	return CollectionUtils.filter(processDataList, searchPredicate);

    }

    public static List<PhdMigrationProcess> getMigrationProcesses() {
	return RootDomainObject.getInstance().getPhdMigrationProcesses();
    }

    public boolean isTransferable() {
	return isInWorkDevelopment() && !hasDestiny();
    }

    public boolean isTransferred() {
	return PhdIndividualProgramProcessState.TRANSFERRED.equals(getActiveState());
    }

    public boolean isFromTransferredProcess() {
	return hasSource();
    }

    public void transferToAnotherProcess(final PhdIndividualProgramProcess destiny, final Person responsible, String remarks) {
	if (!isTransferable()) {
	    throw new DomainException("phd.PhdIndividualProgramProcess.cannot.be.transferred");
	}

	if (hasRegistration() && getRegistration().isConcluded()) {
	    throw new DomainException("phd.PhdIndividualProgramProcess.source.registration.is.concluded");
	}

	this.createState(PhdIndividualProgramProcessState.TRANSFERRED, getPerson(), remarks);

	if (hasRegistration() && getRegistration().isActive()) {
	    RegistrationStateCreator.createState(getRegistration(), responsible, new DateTime(),
		    RegistrationStateType.INTERNAL_ABANDON);
	}

	super.setDestiny(destiny);
	destiny.assignSource(this);
    }

    private void assignSource(PhdIndividualProgramProcess source) {
	if (source.getDestiny() != this) {
	    throw new DomainException("phdIndividualProgramProcess.source.has.different.destiny");
	}

	super.setSource(source);
    }

    @Override
    public void setSource(PhdIndividualProgramProcess source) {
	throw new DomainException("phd.PhdIndividualProgramProcess.cannot.modify.source");
    }

    @Override
    public void setDestiny(PhdIndividualProgramProcess destiny) {
	throw new DomainException("phd.PhdIndividualProgramProcess.cannot.modify.destiny");
    }

    final public boolean hasInsuranceDebts(final ExecutionYear executionYear) {
	return hasAnyNotPayedInsuranceEventUntil(executionYear);
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebts(final AdministrativeOffice office,
	    final ExecutionYear executionYear) {
	return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(office, executionYear);
    }

    private boolean hasAnyNotPayedInsuranceEventUntil(final ExecutionYear executionYear) {
	for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEventsUntil(executionYear)) {
	    if (event.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(final AdministrativeOffice office,
	    final ExecutionYear executionYear) {
	for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
		.getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(office, executionYear)) {
	    if (event.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyNotPayedInsuranceEvents() {
	for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEvents()) {
	    if (event.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(final AdministrativeOffice office) {
	for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
		.getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(office)) {
	    if (event.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    final public boolean hasInsuranceDebtsCurrently() {
	return hasAnyNotPayedInsuranceEvents();
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(final AdministrativeOffice administrativeOffice) {
	return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(administrativeOffice);
    }

    public boolean hasDiplomaRequest() {
	for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
	    if (academicServiceRequest.isDiploma()) {
		return true;
	    }
	}

	return false;
    }

    public PhdRegistryDiplomaRequest getRegistryDiplomaRequest() {
	for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
	    if (academicServiceRequest.isRegistryDiploma() && !academicServiceRequest.isCancelled()
		    && !academicServiceRequest.isRejected()) {
		return (PhdRegistryDiplomaRequest) academicServiceRequest;
	    }
	}

	return null;
    }

    public boolean hasRegistryDiplomaRequest() {
	return getRegistryDiplomaRequest() != null;
    }

    public PhdDiplomaRequest getDiplomaRequest() {
	for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
	    if (academicServiceRequest.isDiploma() && !academicServiceRequest.isCancelled()
		    && !academicServiceRequest.isRejected()) {
		return (PhdDiplomaRequest) academicServiceRequest;
	    }
	}

	return null;
    }

    public PhdDiplomaSupplementRequest getDiplomaSupplementRequest() {
	if (!hasRegistryDiplomaRequest()) {
	    return null;
	}

	return getRegistryDiplomaRequest().getDiplomaSupplement();
    }

    public PhdConclusionProcess getLastConclusionProcess() {
	if (getPhdConclusionProcesses().isEmpty()) {
	    return null;
	}

	Set<PhdConclusionProcess> conclusionProcessSet = new TreeSet<PhdConclusionProcess>(
		PhdConclusionProcess.VERSION_COMPARATOR);
	conclusionProcessSet.addAll(getPhdConclusionProcesses());
	return conclusionProcessSet.iterator().next();
    }

    public boolean isConclusionProcessed() {
	return !getPhdConclusionProcesses().isEmpty();
    }

    public String getGraduateTitle(Locale locale) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);
	StringBuilder stringBuilder = new StringBuilder(bundle.getString("label.phd.graduated.title.in")).append(" ");
	stringBuilder.append(getPhdProgram().getName().getContent(Language.valueOf(locale.getLanguage())));

	return stringBuilder.toString();
    }

    public Boolean isBolonha() {
	return getPhdConfigurationIndividualProgramProcess().getIsBolonha() != null
		&& getPhdConfigurationIndividualProgramProcess().getIsBolonha();
    }

    public List<PhdAcademicServiceRequest> getNewAcademicServiceRequests() {
	List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

	for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
	    if (!request.isNewRequest()) {
		continue;
	    }

	    result.add(request);
	}

	return result;
    }

    public List<PhdAcademicServiceRequest> getProcessingAcademicServiceRequests() {
	List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

	for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
	    if (request.isNewRequest()) {
		continue;
	    }

	    if (request.isDelivered() || request.isDeliveredSituationAccepted()) {
		continue;
	    }

	    if (request.isCancelled() || request.isRejected()) {
		continue;
	    }

	    result.add(request);
	}

	return result;
    }

    public List<PhdAcademicServiceRequest> getToDeliverAcademicServiceRequests() {
	List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

	for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
	    if (!request.isDeliveredSituationAccepted()) {
		continue;
	    }

	    result.add(request);
	}

	return result;
    }

    public List<PhdAcademicServiceRequest> getHistoricalAcademicServiceRequests() {
	List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

	for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
	    if (request.isDelivered() || request.isCancelled() || request.isRejected()) {
		result.add(request);
		continue;
	    }
	}

	return result;
    }

    public String getThesisTitleForCertificateGeneration() {
	if (!StringUtils.isEmpty(getLatexThesisTitle())) {
	    return getLatexThesisTitle();
	}

	return getThesisTitle();
    }

    public boolean hasPhdGratuityEventForYear(int year) {
	for (PhdGratuityEvent event : getPhdGratuityEvents()) {
	    if (event.getYear() == year) {
		return true;
	    }
	}
	return false;
    }
}
