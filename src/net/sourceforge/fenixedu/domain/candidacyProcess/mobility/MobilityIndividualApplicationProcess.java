package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationForIndividualCandidacyFactory;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ApprovedLearningAgreementExecutedAction;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusAlert;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusAlertEntityType;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcessExecutedAction;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessExecutedAction;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ExecutedActionType;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ReceptionEmailExecutedAction;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributesList;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MobilityIndividualApplicationProcess extends MobilityIndividualApplicationProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CancelCandidacy());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new EditDegreeAndCoursesInformation());
	activities.add(new SendEmailForApplicationSubmission());
	activities.add(new EditPublicCandidacyPersonalInformation());
	activities.add(new EditPublicCandidacyInformation());
	activities.add(new EditPublicDegreeAndCoursesInformation());
	activities.add(new SetGriValidation());
	activities.add(new SetCoordinatorValidation());
	activities.add(new VisualizeAlerts());
	activities.add(new EditDocuments());
	activities.add(new EditPublicCandidacyDocumentFile());
	activities.add(new CreateStudentData());
	activities.add(new SetEIdentifierForTesting());
	activities.add(new ImportToLDAP());
	activities.add(new BindLinkSubmitedIndividualCandidacyWithEidentifier());
	activities.add(new UploadApprovedLearningAgreement());
	activities.add(new ViewApprovedLearningAgreements());
	activities.add(new MarkAlertAsViewed());
	activities.add(new SendEmailToAcceptedStudent());
	activities.add(new SendEmailToCandidateForMissingDocuments());
	activities.add(new RevokeDocumentFile());
	activities.add(new RejectCandidacy());
	activities.add(new CreateRegistration());
	activities.add(new RevertCandidacyToStandBy());
	activities.add(new EnrolStudent());
	activities.add(new AnswerNationalIdCardAvoidanceOnSubmissionQuestion());

    }

    public MobilityIndividualApplicationProcess() {
	super();
    }

    public MobilityIndividualApplicationProcess(final MobilityIndividualApplicationProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);

	/*
	 * 27/04/2009 - New document files specific to Erasmus candidacies
	 */
	setSpecificIndividualCandidacyDocumentFiles(bean);

	setValidatedByMobilityCoordinator(false);
	setValidatedByGri(false);

	setPersonalFieldsFromStork(bean.getPersonalFieldsFromStork() != null ? bean.getPersonalFieldsFromStork()
		: StorkAttributesList.EMPTY);
    }

    @Override
    protected void init(IndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyProcess());

	if (bean.getPublicCandidacyHashCode() == null) {
	    throw new DomainException("error.IndividualCandidacy.hash.code.is.null");
	}

	if (existsIndividualCandidacyProcessForDocumentId(bean.getCandidacyProcess(), bean.getPersonBean().getIdDocumentType(),
		bean.getPersonBean().getDocumentIdNumber())) {
	    throw new DomainException("error.IndividualCandidacy.exists.for.same.document.id");
	}

	if (!StringUtils.isEmpty(bean.getPersonBean().getEidentifier())
		&& existsIndividualCandidacyProcessForEidentifier(bean.getCandidacyProcess(), bean.getPersonBean()
			.getEidentifier())) {
	    throw new DomainException("error.individualCandidacy.exists.for.same.eIdentifier");
	}

	setCandidacyProcess(bean.getCandidacyProcess());
	createIndividualCandidacy(bean);

	/*
	 * 11/04/2009 - An external candidacy submission requires documents like
	 * identification and habilitation certificate documents
	 */
	setCandidacyHashCode(bean.getPublicCandidacyHashCode());

	setCandidacyDocumentFiles(bean);

	setProcessCodeForThisIndividualCandidacy(bean.getCandidacyProcess());
    }

    protected boolean existsIndividualCandidacyProcessForEidentifier(final CandidacyProcess process, String eidentifier) {
	return process.getOpenChildProcessByEidentifier(eidentifier) != null;
    }

    private void setSpecificIndividualCandidacyDocumentFiles(MobilityIndividualApplicationProcessBean bean) {

    }

    @Override
    protected void checkParameters(final CandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    protected void createIndividualCandidacy(final IndividualCandidacyProcessBean bean) {
	new MobilityIndividualApplication(this, (MobilityIndividualApplicationProcessBean) bean);
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

    private boolean isCoordinatorOfProcess(IUserView userView) {
	if (!userView.getPerson().hasTeacher()) {
	    return false;
	}

	return ((MobilityApplicationProcess) getCandidacyProcess()).isTeacherErasmusCoordinatorForDegree(userView.getPerson()
		.getTeacher(), getCandidacy().getSelectedDegree());
    }

    @Override
    public final MobilityIndividualApplication getCandidacy() {
	return (MobilityIndividualApplication) super.getCandidacy();
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    public String getCandidacyNotes() {
	return getCandidacy().getNotes();
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean getIsCandidacyProcessWithEidentifer() {
	return !StringUtils.isEmpty(getPersonalDetails().getEidentifier());
    }

    @Override
    protected void executeOperationsBeforeDocumentFileBinding(IndividualCandidacyDocumentFile documentFile) {
	IndividualCandidacyDocumentFileType type = documentFile.getCandidacyFileType();
	IndividualCandidacyDocumentFile file = this.getActiveFileForType(type);

	if (file != null) {
	    file.setCandidacyFileActive(false);
	}
    }

    @Override
    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
	List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PHOTO);
	}

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	}

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT);
	}

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	}

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS);
	}

	return missingDocumentFiles;
    }

    private MobilityIndividualApplicationProcess editCandidacyInformation(final MobilityIndividualApplicationProcessBean bean) {
	getCandidacy().getMobilityStudentData().edit(bean.getMobilityStudentDataBean());
	PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);

	return this;
    }

    private MobilityIndividualApplicationProcess editDegreeAndCoursesInformation(
	    final MobilityIndividualApplicationProcessBean bean) {
	getCandidacy().editDegreeAndCoursesInformation(bean);
	editPrecedentDegreeInformation(bean);

	return this;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources", Language.getLocale()).getString(
		getClass().getSimpleName());
    }

    public List<CurricularCourse> getSortedSelectedCurricularCourses() {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>(getCandidacy().getCurricularCoursesSet());
	Collections.sort(curricularCourses, CurricularCourse.COMPARATOR_BY_NAME);
	return curricularCourses;
    }

    public List<ErasmusAlert> getAlertsNotViewed() {
	List<ErasmusAlert> alertsNotViewed = new ArrayList<ErasmusAlert>();

	CollectionUtils.select(getAlert(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		ErasmusAlert alert = (ErasmusAlert) arg0;
		return alert.isToFire();
	    }

	}, alertsNotViewed);

	Collections.sort(alertsNotViewed, Collections.reverseOrder(ErasmusAlert.WHEN_CREATED_COMPARATOR));

	return alertsNotViewed;
    }

    public ErasmusAlert getMostRecentAlert() {
	List<ErasmusAlert> alerts = new ArrayList<ErasmusAlert>(getAlert());
	Collections.sort(alerts, Collections.reverseOrder(ErasmusAlert.WHEN_CREATED_COMPARATOR));

	return alerts.get(0);
    }

    public boolean isProcessWithMostRecentAlertMessageNotViewed() {
	List<ErasmusAlert> alertsNotViewed = getAlertsNotViewed();

	return !alertsNotViewed.isEmpty() && alertsNotViewed.get(0) == getMostRecentAlert();
    }

    public boolean isStudentAccepted() {
	return isStudentAcceptedAtDate(new DateTime());
    }

    public boolean isStudentAcceptedAtDate(final DateTime dateTime) {
	return !isCandidacyCancelled() && !isCandidacyRejected() && getValidatedByMobilityCoordinator() && getValidatedByGri()
		&& getCandidacy().getMostRecentApprovedLearningAgreement() != null
		&& getCandidacy().getMostRecentApprovedLearningAgreement().getUploadTime().isBefore(dateTime);
    }

    public boolean isStudentAcceptedAndNotifiedAtDate(final DateTime dateTime) {
	return isStudentAcceptedAtDate(dateTime) && getCandidacy().hasProcessWithAcceptNotificationAtDate(dateTime);
    }

    public boolean isStudentAcceptedAndNotified() {
	return isStudentAcceptedAndNotifiedAtDate(new DateTime());
    }

    public boolean isStudentNotifiedWithReceptionEmail() {
	return !getAllReceptionEmailNotifications().isEmpty();
    }

    List<ReceptionEmailExecutedAction> getAllReceptionEmailNotifications() {
	List<ReceptionEmailExecutedAction> list = new ArrayList<ReceptionEmailExecutedAction>();

	for (ErasmusCandidacyProcessExecutedAction executedAction : ((MobilityApplicationProcess) getCandidacyProcess())
		.getErasmusCandidacyProcessExecutedAction()) {
	    if (executedAction.isReceptionEmailExecutedAction()
		    && executedAction.getSubjectMobilityIndividualApplicationProcess().contains(this)) {
		list.add((ReceptionEmailExecutedAction) executedAction);
	    }
	}

	Collections.sort(list, Collections.reverseOrder(new BeanComparator("whenOccured")));

	return list;
    }

    public DateTime getLastReceptionEmailSent() {
	List<ReceptionEmailExecutedAction> list = getAllReceptionEmailNotifications();
	return list.isEmpty() ? null : list.get(0).getWhenOccured();
    }

    public String getErasmusCandidacyStateDescription() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	String registeredMessage = getCandidacy().hasRegistration() ? "/"
		+ bundle.getString("label.erasmus.candidacy.state.registered") : "";
	if (isCandidacyCancelled()) {
	    return bundle.getString("label.erasmus.candidacy.state.description.cancelled") + registeredMessage;
	}

	if (isCandidacyRejected()) {
	    return bundle.getString("label.erasmus.candidacy.state.description.rejected") + registeredMessage;
	}

	if (isCandidacyInStandBy() && isStudentAccepted()) {
	    return bundle.getString("label.erasmus.candidacy.state.description.student.accepted") + registeredMessage;
	}

	return bundle.getString("label.erasmus.candidacy.state.description.student.pending") + registeredMessage;
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    /*
	     * 06/04/2009 The candidacy may be submited by someone who's not
	     * authenticated in the system
	     * 
	     * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
	     * PreConditionNotValidException();}
	     */
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess dummy,
		IUserView userView, Object object) {
	    return new MobilityIndividualApplicationProcess((MobilityIndividualApplicationProcessBean) object);
	}
    }

    static private class CancelCandidacy extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || !process.isCandidacyInStandBy() || process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    final MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((MobilityIndividualApplicationProcessBean) object);
	}
    }

    static private class EditDegreeAndCoursesInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process.editDegreeAndCoursesInformation((MobilityIndividualApplicationProcessBean) object);
	}

    }

    static private class SendEmailForApplicationSubmission extends Activity<MobilityIndividualApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
	    hashCode.sendEmailForApplicationSuccessfullySubmited();
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((MobilityIndividualApplicationProcessBean) object).getPersonBean());
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}
    }

    static private class EditPublicCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((MobilityIndividualApplicationProcessBean) object);
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditPublicDegreeAndCoursesInformation extends Activity<MobilityIndividualApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process.editDegreeAndCoursesInformation((MobilityIndividualApplicationProcessBean) object);
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class SetGriValidation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
	    process.setValidatedByGri(bean.getValidatedByGri());

	    if (bean.getCreateAlert()) {
		ErasmusAlert alert = new ErasmusAlert(process, bean.getSendEmail(), new LocalDate(), new MultiLanguageString(
			bean.getAlertSubject()), new MultiLanguageString(bean.getAlertBody()), ErasmusAlertEntityType.GRI);
		alert.setFireDate(new DateTime());
	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class SetCoordinatorValidation extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    // TODO Check Permissions
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;

	    process.setValidatedByMobilityCoordinator(bean.getValidatedByErasmusCoordinator());

	    if (bean.getCreateAlert()) {
		new ErasmusAlert(process, bean.getSendEmail(), new LocalDate(), new MultiLanguageString(bean.getAlertSubject()),
			new MultiLanguageString(bean.getAlertBody()), ErasmusAlertEntityType.COORDINATOR);
	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.TRUE;
	}
    }

    static private class VisualizeAlerts extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {

	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.TRUE;
	}

    }

    static private class EditDocuments extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}
    }

    static private class CreateStudentData extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    if (!process.getPersonalDetails().getPerson().hasStudent()) {
		new Student(process.getPersonalDetails().getPerson(), null);
		process.getPersonalDetails().getPerson().addPersonRoleByRoleType(RoleType.PERSON);
		process.getPersonalDetails().getPerson().addPersonRoleByRoleType(RoleType.CANDIDATE);
		process.getPersonalDetails().getPerson().setIstUsername();

		if (StringUtils.isEmpty(process.getPersonalDetails().getPerson().getIstUsername())) {
		    throw new DomainException("error.erasmus.create.user", new String[] { null, "User not created" });
		}

	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.TRUE;
	}

    }

    private static class SetEIdentifierForTesting extends Activity<MobilityIndividualApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;

	    String eidentifier = bean.getPersonBean().getEidentifier();
	    process.getPersonalDetails().getPerson().setEidentifier(eidentifier);

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.TRUE;
	}

    }

    private static class ImportToLDAP extends Activity<MobilityIndividualApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    boolean result = importToLDAP(process);

	    if (!result) {
		throw new DomainException("error.erasmus.candidacy.user.not.imported");
	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}
    }

    private static class BindLinkSubmitedIndividualCandidacyWithEidentifier extends
	    Activity<MobilityIndividualApplicationProcess> {
	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!StringUtils.isEmpty(process.getPersonalDetails().getEidentifier())) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    String eidentifier = (String) object;

	    if (StringUtils.isEmpty(eidentifier)) {
		throw new DomainException("error.erasmus.candidacy.eidentifer.must.not.be.empty");
	    }

	    MobilityApplicationProcess parentProcess = (MobilityApplicationProcess) process.getCandidacyProcess();

	    if (parentProcess.getOpenChildProcessByEidentifier(eidentifier) != null) {
		throw new DomainException("error.erasmus.candidacy.already.exists.with.eidentifier");
	    }

	    process.getPersonalDetails().getPerson().setEidentifier(eidentifier);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.FALSE;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return Boolean.FALSE;
	}

    }

    private static class UploadApprovedLearningAgreement extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (isManager(userView)) {
		return;
	    }

	    if (isGriOfficeEmployee(userView)) {
		return;
	    }

	    if (process.isCoordinatorOfProcess(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().addApprovedLearningAgreements((ApprovedLearningAgreementDocumentFile) object);

	    return process;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return true;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return true;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    private static class ViewApprovedLearningAgreements extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (isManager(userView)) {
		return;
	    }

	    if (isGriOfficeEmployee(userView)) {
		return;
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    return process;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return false;
	}

	@Override
	public Boolean isVisibleForGriOffice() {
	    return true;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return false;
	}
    }

    private static class MarkAlertAsViewed extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (isGriOfficeEmployee(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    ErasmusAlert alert = (ErasmusAlert) object;
	    alert.setFireDate(new DateTime());

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

    private static class SendEmailToAcceptedStudent extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isStudentAccepted()) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    MobilityApplicationPeriod candidacyPeriod = (MobilityApplicationPeriod) process.getCandidacyProcess()
		    .getCandidacyPeriod();

	    MobilityEmailTemplate emailTemplateFor = candidacyPeriod
		    .getEmailTemplateFor(MobilityEmailTemplateType.CANDIDATE_ACCEPTED);

	    emailTemplateFor.sendEmailFor(process.getCandidacyHashCode());

	    new ApprovedLearningAgreementExecutedAction(process.getCandidacy().getMostRecentApprovedLearningAgreement(),
		    ExecutedActionType.SENT_EMAIL_ACCEPTED_STUDENT);

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

    private static class SendEmailToCandidateForMissingDocuments extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isProcessMissingRequiredDocumentFiles()) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.sendEmailForRequiredMissingDocuments();

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

    static protected class RevokeDocumentFile extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
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

    static private class RejectCandidacy extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!process.isCandidacyInStandBy()) {
		throw new PreConditionNotValidException();
	    }

	    if (isGriOfficeEmployee(userView)) {
		return;
	    }

	    if (isManager(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.rejectCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.getCandidacy().hasRegistration()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isStudentAcceptedAndNotified()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.createRegistration();

	    return process;
	}

    }

    static private class EnrolOnFirstSemester extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.getCandidacy().hasRegistration()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isStudentAcceptedAndNotified()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isStudentNotifiedWithReceptionEmail()) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.enrol();
	    return process;
	}

    }

    static private class RevertCandidacyToStandBy extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyRejected()) {
		return;
	    }

	    if (process.isCandidacyCancelled()) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    process.revertToStandBy(userView.getPerson());

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

    static private class EnrolStudent extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.getCandidacy().getRegistration() == null) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
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

    };

    static private class AnswerNationalIdCardAvoidanceOnSubmissionQuestion extends Activity<MobilityIndividualApplicationProcess> {

	@Override
	public void checkPreConditions(MobilityIndividualApplicationProcess process, IUserView userView) {

	    if (!NationalIdCardAvoidanceQuestion.UNANSWERED.equals(process.getCandidacy().getNationalIdCardAvoidanceQuestion())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
		IUserView userView, Object object) {
	    MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
	    MobilityIndividualApplication candidacy = process.getCandidacy();

	    candidacy.answerNationalIdCardAvoidanceOnSubmission(bean);

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
    };

    private void createRegistration() {
	getCandidacy().createRegistration(getDegreeCurricularPlan(this), CycleType.SECOND_CYCLE, null);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final MobilityIndividualApplicationProcess candidacyProcess) {
	return candidacyProcess.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
    }

    private void enrol() {
	getCandidacy().enrol();
    }

    private String getMissingRequiredDocumentListText() {
	StringBuilder sb = new StringBuilder();

	for (IndividualCandidacyDocumentFileType missingDocumentType : getMissingRequiredDocumentFiles()) {
	    sb.append("- ").append(missingDocumentType.localizedName(Locale.ENGLISH)).append("\n");
	}

	return sb.toString();
    }

    // Mid-refactor hack. Change visibility back to protected.
    public void sendEmailForRequiredMissingDocuments() {
	if (!isProcessMissingRequiredDocumentFiles()) {
	    throw new DomainException("error.erasmus.indivudual.candidacy.is.not.incomplete");
	}

	String subject = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale()).getString(
		"message.erasmus.missing.required.documents.email.subject");
	String body = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale()).getString(
		"message.erasmus.missing.required.documents.email.body");

	String formattedBody = String
		.format(body, getMissingRequiredDocumentListText(), getCandidacyEnd().toString("dd/MM/yyyy"));
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();

	new ErasmusIndividualCandidacyProcessExecutedAction(this, ExecutedActionType.SENT_EMAIL_FOR_MISSING_REQUIRED_DOCUMENTS);

	new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, subject, formattedBody,
		getCandidacyHashCode().getEmail());
    }

    private static boolean importToLDAP(MobilityIndividualApplicationProcess process) {
	String ldapServiceImportationURL = PropertiesManager.getProperty("ldap.user.importation.service.url");

	Request request = new Request(Method.POST, ldapServiceImportationURL
		+ process.getPersonalDetails().getPerson().getIstUsername());
	ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;

	String ldapServiceUsername = PropertiesManager.getProperty("ldap.user.importation.service.username");
	String ldapServicePassword = PropertiesManager.getProperty("ldap.user.importation.service.password");

	ChallengeResponse authentication = new ChallengeResponse(scheme, ldapServiceUsername, ldapServicePassword);
	request.setChallengeResponse(authentication);

	// Ask to the HTTP client connector to handle the call
	Client client = new Client(Protocol.HTTPS);
	Response response = client.handle(request);

	if (response.getStatus().equals(Status.SUCCESS_OK)) {
	    System.out.println(String.format("Imported username %s", process.getPersonalDetails().getPerson().getIstUsername()));
	    return true;
	} else {
	    System.out.println("error.erasmus.create.user: " + process.getPersonalDetails().getPerson().getIstUsername() + " "
		    + response.getStatus().getName() + " " + new Integer(response.getStatus().getCode()).toString());
	    throw new DomainException("error.erasmus.create.user", new String[] {
		    process.getPersonalDetails().getPerson().getIstUsername(), response.getStatus().getName(),
		    new Integer(response.getStatus().getCode()).toString() });
	}
    }

    public DateTime getMostRecentSentEmailAcceptedStudentActionWhenOccured() {
	if (getCandidacy().getMostRecentApprovedLearningAgreement() == null) {
	    return null;
	}

	return getCandidacy().getMostRecentApprovedLearningAgreement().getMostRecentSentLearningAgreementActionWhenOccured();
    }

    public MobilityProgram getMobilityProgram() {
	return getCandidacy().getMobilityProgram();
    }

}
