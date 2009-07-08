package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdIndividualProgramProcess> {

	@Override
	final public void checkPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	    if (process != null && process.isCancelled()) {
		throw new PreConditionNotValidException("error.PhdIndividualProgramProcess.is.cancelled");
	    }
	}

	abstract protected void activityPreConditions(final PhdIndividualProgramProcess process, final IUserView userView);
    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView != null && userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    static private List<Activity> activities = new ArrayList<Activity>();
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

	activities.add(new CancelPhdIndividualProgramProcess());

	activities.add(new AddCandidacyReferees());

	activities.add(new UploadDocuments());

	activities.add(new AddCustomAlert());

	activities.add(new DeleteCustomAlert());
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

	    return createdProcess;

	}

	private Person getOrCreatePerson(final PhdProgramCandidacyProcessBean bean) {
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

    static public class EditPersonalInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final Person person = process.getPerson();
	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		person.edit((PersonBean) object);
	    } else if (!person.hasAnyPersonRoles() && !person.hasUser() && !person.hasStudent()
		    && process.getCandidacyProcess().isPublicCandidacy()) {
		// assuming public candidacy
		person.editPersonWithExternalData((PersonBean) object, true);
	    }
	    return process;
	}
    }

    static public class AddQualification extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addQualification(userView != null ? userView.getPerson() : null, (QualificationBean) object);
	}
    }

    static public class AddQualifications extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    for (final QualificationBean bean : (List<QualificationBean>) object) {
		process.addQualification(userView != null ? userView.getPerson() : null, bean);
	    }
	    return process;
	}
    }

    static public class DeleteQualification extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView userView) {
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    final Qualification qualification = (Qualification) object;
	    if (process.getPerson().hasAssociatedQualifications(qualification)) {
		if (!canDelete(qualification, process, userView != null ? userView.getPerson() : null)) {
		    throw new DomainException("error.PhdIndividualProgramProcess.DeleteQualification.not.authorized");
		}
		qualification.delete();
	    }
	    return process;
	}

	private boolean canDelete(final Qualification qualification, final PhdIndividualProgramProcess process,
		final Person person) {
	    if (!qualification.hasCreator()) {
		return process.getCandidacyProcess().isPublicCandidacy();
	    }
	    final Person creator = qualification.getCreator();
	    return creator == person || creator.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	}
    }

    static public class AddJobInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addJobInformation(userView.getPerson(), (JobBean) object);
	}
    }

    static public class DeleteJobInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    final Job job = (Job) object;
	    if (process.getPerson().hasJobs(job)) {
		if (!canDelete(job, userView.getPerson())) {
		    throw new DomainException("error.PhdIndividualProgramProcess.DeleteJobInformation.not.authorized");
		}
		job.delete();
	    }
	    return process;
	}

	private boolean canDelete(final Job job, final Person person) {
	    if (!job.hasCreator()) {
		return false;
	    }

	    return job.getCreator() == person || job.getCreator().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	}
    }

    static public class EditIndividualProcessInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.edit(userView, (PhdIndividualProgramProcessBean) object);
	}
    }

    static public class AddGuidingInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addGuiding((PhdProgramGuidingBean) object);
	}
    }

    static public class AddGuidingsInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    for (final PhdProgramGuidingBean bean : (List<PhdProgramGuidingBean>) object) {
		process.addGuiding(bean);
	    }
	    return process;
	}
    }

    static public class DeleteGuiding extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView userView) {
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.deleteGuiding((PhdProgramGuiding) object);
	}
    }

    static public class AddAssistantGuidingInformation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	    // no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addAssistantGuiding((PhdProgramGuidingBean) object);
	}
    }

    static public class DeleteAssistantGuiding extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.deleteAssistantGuiding((PhdProgramGuiding) object);
	}
    }

    static public class CancelPhdIndividualProgramProcess extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    // TODO: CHECK CONDITIONS TO CANCEL PROCESS

	    if (process.hasAnyPayments()) {
		throw new PreConditionNotValidException("error.PhdIndividualProgramProcess.cannot.cancel.because.has.payments");
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.setState(PhdIndividualProgramProcessState.CANCELLED);
	    process.getCandidacyProcess().cancelDebt(userView.getPerson().getEmployee());
	    return process;
	}
    }

    static public class AddCandidacyReferees extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // nothing to be done for now
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.getCandidacyProcess().executeActivity(userView,
		    PhdProgramCandidacyProcess.AddCandidacyReferees.class.getSimpleName(), object);
	    return process;
	}
    }

    static public class UploadDocuments extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.getCandidacyProcess().executeActivity(userView,
		    PhdProgramCandidacyProcess.UploadDocuments.class.getSimpleName(), object);
	    return process;
	}
    }

    static public class AddCustomAlert extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    new PhdCustomAlert(process, (PhdCustomAlertBean) object);

	    return process;
	}

    }

    static public class DeleteCustomAlert extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    ((PhdCustomAlert) object).delete();

	    return process;
	}

    }

    private PhdIndividualProgramProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
	super();

	checkParameters(person, bean.getExecutionYear(), bean.getFocusArea(), bean.getProgram());
	setPerson(person);

	setPhdProgramFocusArea(bean.getFocusArea());
	setPhdProgram(bean.getProgram());
	setExecutionYear(bean.getExecutionYear());

	setCollaborationType(bean);
	setState(PhdIndividualProgramProcessState.CANDIDACY);
	setThesisTitle(bean.getThesisTitle());

	setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(bean.getCandidacyDate().getYear()));
    }

    private void setCollaborationType(PhdProgramCandidacyProcessBean bean) {
	if (bean.getCollaborationType() != null) {
	    setCollaborationType(bean.getCollaborationType());
	} else {
	    setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	}
    }

    private void checkParameters(Person person, ExecutionYear executionYear, final PhdProgramFocusArea phdProgramFocusArea,
	    final PhdProgram phdProgram) {

	check(person, "error.phd.PhdIndividualProgramProcess.person.cannot.be.null");
	check(executionYear, "error.phd.PhdIndividualProgramProcess.executionYear.cannot.be.null");

	if (phdProgramFocusArea == null && phdProgram == null) {
	    check(phdProgram, "error.phd.PhdIndividualProgramProcess.phdProgram.or.focus.area.cannot.be.null");
	}
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

    private PhdIndividualProgramProcess addQualification(final Person person, final QualificationBean bean) {
	final Qualification qualification = new Qualification(getPerson(), bean);
	qualification.setCreator(person);
	return this;
    }

    private PhdIndividualProgramProcess addJobInformation(final Person person, final JobBean bean) {
	final Job job = new Job(getPerson(), bean);
	job.setCreator(person);
	return this;
    }

    private PhdIndividualProgramProcess addGuiding(final PhdProgramGuidingBean bean) {
	addGuidings(createPhdProgramGuiding(bean));
	return this;
    }

    public PhdIndividualProgramProcess deleteGuiding(final PhdProgramGuiding guiding) {
	if (hasGuidings(guiding)) {
	    guiding.delete();
	}
	return this;
    }

    private PhdIndividualProgramProcess addAssistantGuiding(final PhdProgramGuidingBean bean) {
	addAssistantguidings(createPhdProgramGuiding(bean));
	return this;
    }

    private PhdProgramGuiding createPhdProgramGuiding(final PhdProgramGuidingBean bean) {
	final PhdProgramGuiding guiding;
	if (bean.isInternal()) {
	    guiding = PhdProgramGuiding.create(bean.getPerson());
	} else {
	    guiding = PhdProgramGuiding.create(bean.getName(), bean.getQualification(), bean.getWorkLocation(), bean.getEmail());
	    guiding.edit(bean.getCategory(), bean.getAddress(), bean.getPhone());
	}
	return guiding;
    }

    public PhdIndividualProgramProcess deleteAssistantGuiding(final PhdProgramGuiding assistant) {
	if (hasAssistantguidings(assistant)) {
	    assistant.delete();
	}
	return this;
    }

    public String getProcessNumber() {
	return getPhdIndividualProcessNumber().getFullProcessNumber();
    }

    private PhdIndividualProgramProcess edit(final IUserView userView, final PhdIndividualProgramProcessBean bean) {

	checkParameters(getPerson(), getExecutionYear(), bean.getFocusArea(), bean.getPhdProgram());

	if (hasCandidacyProcess() && !getCandidacyDate().equals(bean.getCandidacyDate())) {
	    getCandidacyProcess().executeActivity(userView, PhdProgramCandidacyProcess.EditCandidacyDate.class.getSimpleName(),
		    bean.getCandidacyDate());
	}

	setThesisTitle(bean.getThesisTitle());
	setCollaborationType(bean.getCollaborationType());
	setPhdProgramFocusArea(bean.getFocusArea());

	if (bean.getCollaborationType().needExtraInformation()) {
	    check(bean.getOtherCollaborationType(), "error.PhdIndividualProgramProcess.invalid.other.collaboration.type");
	    setOtherCollaborationType(bean.getOtherCollaborationType());
	}

	return this;
    }

    public LocalDate getCandidacyDate() {
	return getCandidacyProcess().getCandidacyDate();
    }

    public boolean isCancelled() {
	return getState() == PhdIndividualProgramProcessState.CANCELLED;
    }

    private boolean hasAnyPayments() {
	// TODO: for now just check candidacy, but is necessary to check another
	// debts?
	return getCandidacyProcess().hasAnyPayments();
    }

    public List<PhdCandidacyReferee> getPhdCandidacyReferees() {
	return getCandidacyProcess().getCandidacyReferees();
    }

    public List<Qualification> getQualifications() {
	return getPerson().getAssociatedQualifications();
    }

    public List<PhdProgramCandidacyProcessDocument> getCandidacyProcessDocuments() {
	return getCandidacyProcess().getDocuments();
    }

    public boolean hasCandidacyProcessDocument(final PhdIndividualProgramDocumentType type) {
	return getCandidacyProcess().hasAnyDocuments(type);
    }

    public int getCandidacyProcessDocumentsCount(final PhdIndividualProgramDocumentType type) {
	return getCandidacyProcess().getDocumentsCount(type);
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyProcessHashCode() {
	return getCandidacyProcess().getCandidacyHashCode();
    }

    static public Set<PhdIndividualProgramProcess> search(SearchPhdIndividualProgramProcessBean searchBean) {
	final Set<PhdIndividualProgramProcess> result = new HashSet<PhdIndividualProgramProcess>();
	final Set<Process> processesToSearch = new HashSet<Process>();

	if (!StringUtils.isEmpty(searchBean.getProcessNumber())) {

	    for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : RootDomainObject.getInstance()
		    .getPhdIndividualProcessNumbers()) {
		if (phdIndividualProgramProcessNumber.getFullProcessNumber().equals(searchBean.getProcessNumber())) {
		    processesToSearch.add(phdIndividualProgramProcessNumber.getProcess());
		}
	    }

	} else {
	    processesToSearch.addAll(RootDomainObject.getInstance().getProcesses());
	}

	for (final Process each : processesToSearch) {
	    if (each instanceof PhdIndividualProgramProcess) {
		final PhdIndividualProgramProcess phdIndividualProgramProcess = (PhdIndividualProgramProcess) each;
		if (matchesExecutionYear(searchBean, phdIndividualProgramProcess)
			&& matchesProcessState(searchBean, phdIndividualProgramProcess)) {
		    result.add(phdIndividualProgramProcess);
		}
	    }
	}

	return result;
    }

    static private boolean matchesProcessState(SearchPhdIndividualProgramProcessBean searchBean,
	    final PhdIndividualProgramProcess phdIndividualProgramProcess) {
	return searchBean.getProcessState() == null || searchBean.getProcessState() == phdIndividualProgramProcess.getState();
    }

    static private boolean matchesExecutionYear(SearchPhdIndividualProgramProcessBean searchBean,
	    final PhdIndividualProgramProcess phdIndividualProgramProcess) {
	return searchBean.getExecutionYear() == null
		|| searchBean.getExecutionYear() == phdIndividualProgramProcess.getExecutionYear();
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

    public Set<PhdAlertMessage> getAlertMessagesWithTasksToPerform() {
	final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

	for (final PhdAlertMessage each : getAlertMessages()) {
	    if (!each.isTaskPerformed()) {
		result.add(each);
	    }
	}

	return result;
    }

}
