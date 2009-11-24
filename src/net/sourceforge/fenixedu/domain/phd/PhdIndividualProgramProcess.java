package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
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
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PublicPhdMissingCandidacyValidationAlert;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdIndividualProgramProcess> {

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

	abstract protected void activityPreConditions(final PhdIndividualProgramProcess process, final IUserView userView);
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

	activities.add(new CancelPhdProgramProcess());
	activities.add(new NotAdmittedPhdProgramProcess());
	activities.add(new SuspendPhdProgramProcess());
	activities.add(new FlunkedPhdProgramProcess());

	activities.add(new AddCandidacyReferees());

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

	activities.add(new RequestPublicThesisPresentation());
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

    static public class CancelPhdProgramProcess extends PhdActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // remove restrictions
	}

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    process.createState(PhdIndividualProgramProcessState.CANCELLED, userView.getPerson());
	    process.cancelDebts(userView.getPerson());

	    if (process.hasRegistration() && process.getRegistration().isActive()) {
		RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
			RegistrationStateType.CANCELED);
	    }

	    return process;
	}
    }

    static public class NotAdmittedPhdProgramProcess extends PhdActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // remove restrictions
	}

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.createState(PhdIndividualProgramProcessState.NOT_ADMITTED, userView.getPerson());
	    process.cancelDebts(userView.getPerson());

	    if (process.hasRegistration() && process.getRegistration().isActive()) {
		RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
			RegistrationStateType.CANCELED);
	    }

	    return process;
	}
    }

    static public class SuspendPhdProgramProcess extends PhdActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // remove restrictions
	}

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.createState(PhdIndividualProgramProcessState.SUSPENDED, userView.getPerson());
	    process.cancelDebts(userView.getPerson());

	    if (process.hasRegistration() && process.getRegistration().isActive()) {
		RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
			RegistrationStateType.INTERRUPTED);
	    }

	    return process;
	}
    }

    static public class FlunkedPhdProgramProcess extends PhdActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // remove restrictions
	}

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.createState(PhdIndividualProgramProcessState.FLUNKED, userView.getPerson());
	    process.cancelDebts(userView.getPerson());

	    if (process.hasRegistration() && process.getRegistration().isActive()) {
		RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
			RegistrationStateType.CANCELED);
	    }

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

	    new PhdCustomAlert((PhdCustomAlertBean) object);

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

    static public class ValidatedByCandidate extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.getCandidacyProcess().executeActivity(userView,
		    PhdProgramCandidacyProcess.ValidatedByCandidate.class.getSimpleName(), null);
	    return process;
	}
    }

    static public class AddStudyPlan extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    final PhdProgramCandidacyProcess candidacyProcess = process.getCandidacyProcess();

	    if (candidacyProcess.hasState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    new PhdStudyPlan((PhdStudyPlanBean) object);
	    return process;
	}

    }

    static public class EditStudyPlan extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.getStudyPlan().edit((PhdStudyPlanBean) object);
	    return process;
	}

    }

    static public class AddStudyPlanEntry extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    process.getStudyPlan().createEntries((PhdStudyPlanEntryBean) object);

	    return process;
	}

    }

    static public class DeleteStudyPlanEntry extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    ((PhdStudyPlanEntry) object).delete();

	    return process;
	}

    }

    static public class DeleteStudyPlan extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    ((PhdStudyPlan) object).delete();

	    return process;
	}

    }

    static public class EditQualificationExams extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    process.edit(userView, (PhdIndividualProgramProcessBean) object);

	    return process;
	}

    }

    static public class RequestPublicPresentationSeminarComission extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (process.hasSeminarProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView) && !process.isGuider(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcess publicPresentationSeminarProcess = Process.createNewProcess(userView,
		    PublicPresentationSeminarProcess.class, object);

	    publicPresentationSeminarProcess.setIndividualProgramProcess(individualProcess);

	    AlertService.alertCoordinator(individualProcess,
		    "message.phd.alert.public.presentation.seminar.comission.definition.subject",
		    "message.phd.alert.public.presentation.seminar.comission.definition.body");

	    return individualProcess;
	}
    }

    static public class RequestPublicThesisPresentation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	    if (process.hasSeminarProcess() && !process.getSeminarProcess().isExempted()
		    && !process.getSeminarProcess().isConcluded()) {
		throw new PreConditionNotValidException();
	    }

	    if (process.hasThesisProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, IUserView userView,
		Object object) {

	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	    final PhdThesisProcess thesisProcess = Process.createNewProcess(userView, PhdThesisProcess.class, bean);

	    thesisProcess.setIndividualProgramProcess(individualProcess);

	    thesisProcess.addDocuments(bean.getDocuments(), userView.getPerson());

	    /*
	     * 
	     * TODO: SEND ALERTS ALERTS
	     */

	    return individualProcess;
	}

    }

    private PhdIndividualProgramProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
	super();

	checkParameters(person, bean.getExecutionYear());
	setPerson(person);

	setPhdProgramFocusArea(bean.getFocusArea());
	setPhdProgram(bean.getProgram());
	setExecutionYear(bean.getExecutionYear());

	setCollaborationType(bean);
	createState(PhdIndividualProgramProcessState.CANDIDACY, person);
	setThesisTitle(bean.getThesisTitle());

	setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(bean.getCandidacyDate().getYear()));
    }

    public void createState(final PhdIndividualProgramProcessState state, final Person person) {
	createState(state, person, null);
    }

    public void createState(final PhdIndividualProgramProcessState state, final Person person, final String remarks) {
	new PhdProgramProcessState(this, state, person, remarks);
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
	return PhdProgramGuiding.create(bean);
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

    static public List<PhdIndividualProgramProcess> search(SearchPhdIndividualProgramProcessBean searchBean) {

	final Set<PhdIndividualProgramProcess> processesToSearch = new HashSet<PhdIndividualProgramProcess>();
	for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : RootDomainObject.getInstance()
		.getPhdIndividualProcessNumbers()) {
	    processesToSearch.add(phdIndividualProgramProcessNumber.getProcess());
	}

	return CollectionUtils.filter(processesToSearch, searchBean.getPredicates());

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

    public Set<PhdAlertMessage> getUnreadedAlertMessagesFor(final Person person) {
	final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

	for (final PhdAlertMessage each : getAlertMessages()) {
	    if (!each.isReaded() && each.isFor(person)) {
		result.add(each);
	    }
	}

	return result;
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
	for (final PhdAlert alert : getActiveAlerts()) {
	    if (alert.getClass().equals(type)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyRegistrationFormalizationActiveAlert() {
	return hasAnyActiveAlertFor(PhdRegistrationFormalizationAlert.class);
    }

    public PhdIndividualProgramProcessState getActiveState() {
	if (!hasAnyStates()) {
	    return null;
	}
	final PhdProgramProcessState state = Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE);
	return (state != null) ? state.getType() : null;
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
	for (final PhdProgramGuiding guiding : getGuidings()) {
	    if (guiding.isFor(person)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isAssistantGuider(Person person) {
	for (final PhdProgramGuiding guiding : getAssistantguidings()) {
	    if (guiding.isFor(person)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isRegistrationAvailable() {
	return hasRegistration();
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
	return this;
    }

    public Set<PhdProgramGuiding> getGuidingsAndAssistantGuidings() {
	final Set<PhdProgramGuiding> result = new HashSet<PhdProgramGuiding>();
	result.addAll(getAssistantguidings());
	result.addAll(getGuidings());

	return result;
    }

    private void cancelDebts(final Person person) {
	if (hasCandidacyProcess() && !getCandidacyProcess().hasAnyPayments()) {
	    getCandidacyProcess().cancelDebt(person.getEmployee());
	}

	if (hasRegistrationFee() && !getRegistrationFee().hasAnyPayments()) {
	    getRegistrationFee().cancel(person.getEmployee());
	}
    }

    public boolean hasSchoolPartConcluded() {
	return (hasStudyPlan() && getStudyPlan().isExempted()) || (hasRegistration() && getRegistration().isConcluded());
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

}
