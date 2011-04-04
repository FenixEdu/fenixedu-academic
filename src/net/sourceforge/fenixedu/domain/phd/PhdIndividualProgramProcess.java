package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Alert;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;
import net.sourceforge.fenixedu.domain.phd.alert.PhdFinalProofRequestAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PublicPhdMissingCandidacyValidationAlert;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;
import net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmail;
import net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean;
import net.sourceforge.fenixedu.domain.phd.guidance.PhdGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationGuiding;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData.PhdMigrationIndividualProcessDataBean;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.predicates.Predicate;

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
	    return process.addGuiding((PhdParticipantBean) object);
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
	    for (final PhdParticipantBean bean : (List<PhdParticipantBean>) object) {
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
	    return process.deleteGuiding((PhdParticipant) object);
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
	    return process.addAssistantGuiding((PhdParticipantBean) object);
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
	    return process.deleteAssistantGuiding((PhdParticipant) object);
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

	    if (process.getCandidacyProcess().hasCandidacy()) {
		process.getCandidacyProcess().getCandidacy().cancelCandidacy();
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

	    if (((PublicPresentationSeminarProcessBean) object).getGenerateAlert()) {
		AlertService.alertCoordinators(individualProcess,
			"message.phd.alert.public.presentation.seminar.comission.definition.subject",
			"message.phd.alert.public.presentation.seminar.comission.definition.body");

	    }

	    return individualProcess;
	}
    }

    static public class ExemptPublicPresentationSeminarComission extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (process.hasSeminarProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcess seminarProcess = Process.createNewProcess(userView,
		    PublicPresentationSeminarProcess.class, object);

	    seminarProcess.setIndividualProgramProcess(process);
	    seminarProcess.createState(PublicPresentationSeminarProcessStateType.EXEMPTED, userView.getPerson(), null);

	    discardPublicSeminarAlerts(process);

	    return process;
	}

	private void discardPublicSeminarAlerts(final PhdIndividualProgramProcess process) {
	    for (final PhdAlert alert : process.getActiveAlerts()) {
		if (alert instanceof PhdPublicPresentationSeminarAlert) {
		    alert.discard();
		}
	    }
	}

    }

    static public class RequestPublicThesisPresentation extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	    if (!process.hasSeminarProcess()
		    || (!process.getSeminarProcess().isExempted() && !process.getSeminarProcess().isConcluded())) {
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
	    bean.setProcess(individualProcess);

	    Process.createNewProcess(userView, PhdThesisProcess.class, bean);
	    individualProcess.createState(PhdIndividualProgramProcessState.THESIS_DISCUSSION, userView.getPerson());

	    return individualProcess;
	}

    }

    static public class AcceptEnrolments extends PhdActivity {

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	    if (!process.isCoordinatorForPhdProgram(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) object;

	    for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
		if (process.getRegistration().hasEnrolments(enrolment)) {
		    enrolment.setEnrolmentCondition(EnrollmentCondition.VALIDATED);
		}
	    }

	    AlertService.alertStudent(process, AlertMessage.create(bean.getMailSubject()).isKey(false), AlertMessage.create(
		    buildBody(bean)).isKey(false));

	    // TODO: wich group should be used in academic office?
	    // AlertService.alertAcademicOffice(process, permissionType,
	    // subjectKey, bodyKey)

	    return process;
	}

	private String buildBody(ManageEnrolmentsBean bean) {
	    final StringBuilder sb = new StringBuilder();
	    sb.append(AlertService.getMessageFromResource("label.phd.accepted.enrolments")).append("\n");
	    for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
		sb.append("- ").append(enrolment.getPresentationName()).append(enrolment.getExecutionPeriod().getQualifiedName())
			.append("\n");
	    }
	    return sb.toString();
	}

    }

    static public class RejectEnrolments extends PhdActivity {

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	    if (!process.isCoordinatorForPhdProgram(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) object;
	    final StudentCurricularPlan scp = process.getRegistration().getLastStudentCurricularPlan();
	    final String mailBody = buildBody(bean);

	    scp.enrol(bean.getSemester(), Collections.EMPTY_SET, getCurriculumModules(bean.getEnrolmentsToValidate()),
		    CurricularRuleLevel.ENROLMENT_WITH_RULES);

	    AlertService.alertStudent(process, AlertMessage.create(bean.getMailSubject()).isKey(false), AlertMessage.create(
		    mailBody).isKey(false));

	    // TODO: wich group should be used in academic office?
	    // AlertService.alertAcademicOffice(process, permissionType,
	    // subjectKey, bodyKey)

	    return process;
	}

	private String buildBody(ManageEnrolmentsBean bean) {
	    final StringBuilder sb = new StringBuilder();
	    sb.append(AlertService.getMessageFromResource("label.phd.rejected.enrolments")).append("\n");
	    for (final Enrolment enrolment : bean.getEnrolmentsToValidate()) {
		sb.append("- ").append(enrolment.getPresentationName()).append(enrolment.getExecutionPeriod().getQualifiedName())
			.append("\n");
	    }
	    return sb.toString();
	}

	private List<CurriculumModule> getCurriculumModules(List<Enrolment> enrolmentsToValidate) {
	    return new ArrayList<CurriculumModule>(enrolmentsToValidate);
	}

    }

    static public class EditWhenStartedStudies extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final RegistrationFormalizationBean bean = (RegistrationFormalizationBean) object;

	    process.check(bean.getWhenStartedStudies(),
		    "error.PhdIndividualProgramProcess.EditWhenStartedStudies.invalid.when.started.studies");

	    process.setWhenStartedStudies(bean.getWhenStartedStudies());

	    if (process.hasRegistration()) {
		process.getRegistration().editStartDates(bean.getWhenStartedStudies(),
			process.getCandidacyProcess().getWhenRatified(), bean.getWhenStartedStudies());
	    }

	    return process;
	}

    }

    static public class ActivatePhdProgramProcessInCandidacyState extends PhdActivity {

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
	    final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

	    /*
	     * 1 - Check if there's no registration 2 - Check if last active
	     * state was candidacy
	     */
	    if (process.hasRegistration()) {
		throw new DomainException("error.PhdIndividualProgramProcess.set.candidacy.state.has.registration");
	    }

	    if (!process.getLastActiveState().getType().equals(PhdIndividualProgramProcessState.CANDIDACY)) {
		throw new DomainException(
			"error.PhdIndividualProgramProcess.set.candidacy.state.previous.active.state.is.not.candidacy");
	    }

	    process.createState(process.getLastActiveState().getType(), userView.getPerson());

	    return process;
	}
    }

    static public class ActivatePhdProgramProcessInWorkDevelopmentState extends PhdActivity {
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

	    process.createState(PhdIndividualProgramProcessState.WORK_DEVELOPMENT, userView.getPerson());

	    /*
	     * If it is associated to a registration we check that is not active
	     * and try to reactivate it setting the last active state of the
	     * registration
	     */

	    if (!process.hasRegistration()) {
		return process;
	    }

	    /*
	     * The registration is concluded so we skip
	     */
	    if (process.getRegistration().isConcluded()) {
		return process;
	    }

	    if (process.getRegistration().isActive()) {
		throw new DomainException("error.PhdIndividualProgramProcess.set.work.development.state.registration.is.active");
	    }

	    RegistrationState registrationLastActiveState = process.getRegistration().getLastActiveState();

	    if (!registrationLastActiveState.isActive()) {
		throw new DomainException(
			"error.PhdIndividualProgramProcess.set.work.development.state.registration.last.state.is.not.active");
	    }

	    RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
		    registrationLastActiveState.getStateType());

	    return process;
	}
    }

    static public class ActivatePhdProgramProcessInThesisDiscussionState extends PhdActivity {
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

	    process.createState(PhdIndividualProgramProcessState.THESIS_DISCUSSION, userView.getPerson());

	    /*
	     * If the program is associated to a registration we check if it is
	     * concluded
	     */

	    if (!process.hasRegistration()) {
		return process;
	    }

	    if (!process.getRegistration().isConcluded()) {
		throw new DomainException(
			"error.PhdIndividualProgramProcess.set.thesis.discussion.state.registration.is.not.concluded");
	    }

	    return process;
	}
    }

    static public class ConfigurePhdIndividualProgramProcess extends PhdActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // Turn off any preconditions
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
	    PhdConfigurationIndividualProgramProcessBean bean = (PhdConfigurationIndividualProgramProcessBean) object;

	    process.getPhdConfigurationIndividualProgramProcess().configure(bean);
	    return process;
	}

    }

    static public class SendPhdEmail extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {

	    final PhdIndividualProgramProcessEmailBean bean = (PhdIndividualProgramProcessEmailBean) object;
	    bean.setProcess(process);
	    PhdIndividualProgramProcessEmail.createEmail(bean);

	    return process;
	}

    }

    static public class RemoveLastStateOnPhdIndividualProgramProcess extends PhdActivity {

	@Override
	public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected void processPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.removeLastState();

	    return process;
	}

    }

    static public class UploadGuidanceDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (process.isGuiderOrAssistentGuider(userView.getPerson())) {
		return;
	    }

	    if (process.isCoordinatorForPhdProgram(userView.getPerson())) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    PhdProgramDocumentUploadBean bean = (PhdProgramDocumentUploadBean) object;

	    new PhdGuidanceDocument(process, bean.getType(), bean.getRemarks(),
		    bean.getFileContent(), bean.getFilename(), userView.getPerson());

	    return process;
	}

    }

    static public class EditPhdParticipant extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    PhdParticipantBean bean = (PhdParticipantBean) object;
	    bean.getParticipant().edit(bean);

	    return process;
	}

    }

    static public class TransferToAnotherProcess extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;
	    process.transferToAnotherProcess(bean.getDestiny(), userView.getPerson(), bean.getRemarks());

	    return process;
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
	createState(PhdIndividualProgramProcessState.CANDIDACY, person);
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

    public void createState(final PhdIndividualProgramProcessState state, final Person person) {
	createState(state, person, null);
    }

    public void createState(final PhdIndividualProgramProcessState state, final Person person, final String remarks) {
	if (!getPossibleNextStates().contains(state)) {
	    throw new DomainException("error.phd.PhdIndividualProgramProcess.invalid.next.state");
	}

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

    private PhdIndividualProgramProcess addGuiding(final PhdParticipantBean bean) {
	addGuidings(bean.hasParticipant() ? bean.getParticipant() : createPhdParticipant(bean));
	return this;
    }

    public PhdIndividualProgramProcess deleteGuiding(final PhdParticipant guiding) {
	if (hasGuidings(guiding)) {
	    removeGuidings(guiding);
	    guiding.tryDelete();
	}
	return this;
    }

    private PhdIndividualProgramProcess addAssistantGuiding(final PhdParticipantBean bean) {
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

	if (isRegistrationFormalized()) {
	    getCandidacyProcess().setWhenRatified(bean.getWhenRatified());
	    setWhenFormalizedRegistration(bean.getWhenFormalizedRegistration());
	}

	getPhdIndividualProcessNumber().edit(bean);

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

    static public List<PhdIndividualProgramProcess> search(Predicate<PhdIndividualProgramProcess> searchPredicate) {
	return search(null, searchPredicate);
    }

    static public List<PhdIndividualProgramProcess> search(final ExecutionYear year,
	    final Predicate<PhdIndividualProgramProcess> searchPredicate) {

	final Set<PhdIndividualProgramProcess> processesToSearch = new HashSet<PhdIndividualProgramProcess>();
	for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : RootDomainObject.getInstance()
		.getPhdIndividualProcessNumbers()) {
	    if (year == null || phdIndividualProgramProcessNumber.belongsTo(year)
		    || phdIndividualProgramProcessNumber.getProcess().getExecutionYear().equals(year)) {
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

    public List<PhdIndividualProgramProcessState> getPossibleNextStates() {
	PhdIndividualProgramProcessState activeState = getActiveState();

	if (activeState == null) {
	    return Collections.singletonList(PhdIndividualProgramProcessState.CANDIDACY);
	}

	switch (activeState) {
	case CANDIDACY:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { PhdIndividualProgramProcessState.WORK_DEVELOPMENT,
		    PhdIndividualProgramProcessState.NOT_ADMITTED, PhdIndividualProgramProcessState.SUSPENDED,
		    PhdIndividualProgramProcessState.FLUNKED, PhdIndividualProgramProcessState.CANCELLED });
	case WORK_DEVELOPMENT:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { PhdIndividualProgramProcessState.THESIS_DISCUSSION,
		    PhdIndividualProgramProcessState.NOT_ADMITTED,
		    PhdIndividualProgramProcessState.SUSPENDED, PhdIndividualProgramProcessState.FLUNKED,
		    PhdIndividualProgramProcessState.CANCELLED, PhdIndividualProgramProcessState.TRANSFERRED });
	case THESIS_DISCUSSION:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { PhdIndividualProgramProcessState.NOT_ADMITTED,
		    PhdIndividualProgramProcessState.SUSPENDED, PhdIndividualProgramProcessState.FLUNKED,
		    PhdIndividualProgramProcessState.CANCELLED });
	case NOT_ADMITTED:
	case SUSPENDED:
	case FLUNKED:
	case CANCELLED:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { getLastActiveState().getType() });
	case CONCLUDED:
	    return Collections.emptyList();
	case TRANSFERRED:
	    return Collections.singletonList(PhdIndividualProgramProcessState.WORK_DEVELOPMENT);
	default:
	    throw new DomainException("error.PhdIndividualProgramProcess.unknown.process.state.types");
	}
    }

    @Override
    public boolean isProcessIndividualProgram() {
	return true;
    }

    public boolean isConcluded() {
	return hasThesisProcess() && getThesisProcess().isConcluded();
    }

    public boolean isInWorkDevelopment() {
	return PhdIndividualProgramProcessState.WORK_DEVELOPMENT.equals(getActiveState());
    }

    public PhdThesisFinalGrade getFinalGrade() {
	if (!isConcluded()) {
	    return null;
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
	    for(final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessData()) {
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

    private void transferToAnotherProcess(final PhdIndividualProgramProcess destiny, final Person responsible, String remarks) {
	if (!isTransferable()) {
	    throw new DomainException("phd.PhdIndividualProgramProcess.cannot.be.transferred");
	}

	if (hasRegistration() && getRegistration().isConcluded()) {
	    throw new DomainException("phd.PhdIndividualProgramProcess.source.registration.is.concluded");
	}

	this.createState(PhdIndividualProgramProcessState.TRANSFERRED, getPerson(), remarks);

	if (hasRegistration() && getRegistration().isActive()) {
	    RegistrationStateCreator.createState(getRegistration(), responsible, getWhenCreated(),
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

}
