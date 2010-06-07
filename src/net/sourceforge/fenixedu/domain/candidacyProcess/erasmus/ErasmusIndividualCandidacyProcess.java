package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ErasmusIndividualCandidacyProcess extends ErasmusIndividualCandidacyProcess_Base {

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
    }

    public ErasmusIndividualCandidacyProcess() {
	super();
    }

    public ErasmusIndividualCandidacyProcess(final ErasmusIndividualCandidacyProcessBean bean) {
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

	setValidatedByErasmusCoordinator(false);
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

    private void setSpecificIndividualCandidacyDocumentFiles(ErasmusIndividualCandidacyProcessBean bean) {

    }

    @Override
    protected void checkParameters(final CandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    protected void createIndividualCandidacy(final IndividualCandidacyProcessBean bean) {
	new ErasmusIndividualCandidacy(this, (ErasmusIndividualCandidacyProcessBean) bean);
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

	return ((ErasmusCandidacyProcess) getCandidacyProcess()).isTeacherErasmusCoordinatorForDegree(userView.getPerson()
		.getTeacher(), getCandidacy().getSelectedDegree());
    }

    @Override
    public final ErasmusIndividualCandidacy getCandidacy() {
	return (ErasmusIndividualCandidacy) super.getCandidacy();
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
	
	if(file != null) {
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

	if (getActiveFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	}

	return missingDocumentFiles;
    }

    private ErasmusIndividualCandidacyProcess editCandidacyInformation(final ErasmusIndividualCandidacyProcessBean bean) {
	getCandidacy().getErasmusStudentData().edit(bean.getErasmusStudentDataBean());
	return this;
    }

    private ErasmusIndividualCandidacyProcess editDegreeAndCoursesInformation(final ErasmusIndividualCandidacyProcessBean bean) {
	getCandidacy().editDegreeAndCoursesInformation(bean);

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

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    /*
	     * 06/04/2009 The candidacy may be submited by someone who's not
	     * authenticated in the system
	     * 
	     * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
	     * PreConditionNotValidException();}
	     */
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess dummy, IUserView userView,
		Object object) {
	    return new ErasmusIndividualCandidacyProcess((ErasmusIndividualCandidacyProcessBean) object);
	}
    }

    static private class CancelCandidacy extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || !process.isCandidacyInStandBy() || process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    final ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((ErasmusIndividualCandidacyProcessBean) object);
	}
    }

    static private class EditDegreeAndCoursesInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView) && !isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editDegreeAndCoursesInformation((ErasmusIndividualCandidacyProcessBean) object);
	}

    }

    static private class SendEmailForApplicationSubmission extends Activity<ErasmusIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
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

    static private class EditPublicCandidacyPersonalInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((ErasmusIndividualCandidacyProcessBean) object).getPersonBean());
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

    static private class EditPublicCandidacyInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((ErasmusIndividualCandidacyProcessBean) object);
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

    static private class EditPublicDegreeAndCoursesInformation extends Activity<ErasmusIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editDegreeAndCoursesInformation((ErasmusIndividualCandidacyProcessBean) object);
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

    static private class SetGriValidation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isGriOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) object;
	    process.setValidatedByGri(bean.getValidatedByGri());

	    if (bean.getCreateAlert()) {
		new ErasmusAlert(process, bean.getSendEmail(), new LocalDate(), new MultiLanguageString(bean.getAlertSubject()),
			new MultiLanguageString(bean.getAlertBody()), ErasmusAlertEntityType.GRI);
	    }

	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class SetCoordinatorValidation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    // TODO Check Permissions
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) object;

	    process.setValidatedByErasmusCoordinator(bean.getValidatedByErasmusCoordinator());

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

    static private class VisualizeAlerts extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {

	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process;
	}

	@Override
	public Boolean isVisibleForCoordinator() {
	    return Boolean.TRUE;
	}

    }

    static private class EditDocuments extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
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

    static private class EditPublicCandidacyDocumentFile extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
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

    static private class CreateStudentData extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
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

    private static class SetEIdentifierForTesting extends Activity<ErasmusIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) object;

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

    private static class ImportToLDAP extends Activity<ErasmusIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isManager(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
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

    private static class BindLinkSubmitedIndividualCandidacyWithEidentifier extends Activity<ErasmusIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!StringUtils.isEmpty(process.getPersonalDetails().getEidentifier())) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    String eidentifier = (String) object;
	    
	    if (StringUtils.isEmpty(eidentifier)) {
		throw new DomainException("error.erasmus.candidacy.eidentifer.must.not.be.empty");
	    }
	    
	    ErasmusCandidacyProcess parentProcess =  (ErasmusCandidacyProcess) process.getCandidacyProcess();

	    if(parentProcess.getOpenChildProcessByEidentifier(eidentifier) != null) {
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

    private static class UploadApprovedLearningAgreement extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if(isManager(userView)) {
		return;
	    }
	    
	    if(isGriOfficeEmployee(userView)) {
		return;
	    }
	    
	    if (process.isCoordinatorOfProcess(userView)) {
		return;
	    }
	    
	    throw new PreConditionNotValidException();
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().addApprovedLearningAgreements((IndividualCandidacyDocumentFile) object);

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
    }

    private static boolean importToLDAP(ErasmusIndividualCandidacyProcess process) {
	String ldapServiceImportationURL = PropertiesManager.getProperty("ldap.user.importation.service.url");

	Request request = new Request(Method.POST, ldapServiceImportationURL + process.getPersonalDetails().getPerson()
			.getIstUsername());
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
	    throw new DomainException("error.erasmus.create.user", new String[] {
		    process.getPersonalDetails().getPerson().getIstUsername(), response.getStatus().getName(),
		    new Integer(response.getStatus().getCode()).toString() });
	}
    }
}
