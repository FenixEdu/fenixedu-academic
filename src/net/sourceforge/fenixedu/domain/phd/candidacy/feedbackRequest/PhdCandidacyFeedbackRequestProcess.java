package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.access.ExternalAccessPhdActivity;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

public class PhdCandidacyFeedbackRequestProcess extends PhdCandidacyFeedbackRequestProcess_Base {

    private PhdCandidacyFeedbackRequestProcess() {
	super();
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
	return getCandidacyProcess().getIndividualProgramProcess();
    }

    @Override
    protected Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return true;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    @Override
    public PhdCandidacyFeedbackStateType getActiveState() {
	return (PhdCandidacyFeedbackStateType) super.getActiveState();
    }

    protected void createState(final PhdCandidacyFeedbackStateType type, final Person person, final String remarks) {
	new PhdCandidacyFeedbackState(this, type, person, remarks);
    }

    protected PhdCandidacyFeedbackRequestElement getCandidacyFeedbackRequestElement(final PhdParticipant participant) {
	for (final PhdCandidacyFeedbackRequestElement element : getElementsSet()) {
	    if (element.getParticipant() == participant) {
		return element;
	    }
	}
	return null;
    }

    public Set<PhdIndividualProgramDocumentType> getSortedSharedDocumentTypes() {
	return getSharedDocuments().getSortedTypes();
    }

    static abstract private class PhdActivity extends Activity<PhdCandidacyFeedbackRequestProcess> {

	@Override
	final public void checkPreConditions(final PhdCandidacyFeedbackRequestProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdCandidacyFeedbackRequestProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PhdCandidacyFeedbackRequestProcess process, final IUserView userView);
    }

    @StartActivity
    static public class CreateCandidacy extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess noProcess, IUserView userView) {

	}

	@Override
	protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess noProcess,
		IUserView userView, Object object) {

	    final PhdCandidacyFeedbackRequestProcessBean bean = (PhdCandidacyFeedbackRequestProcessBean) object;
	    final PhdCandidacyFeedbackRequestProcess process = new PhdCandidacyFeedbackRequestProcess();

	    process.setSharedDocuments(new PhdCandidacySharedDocumentsList(bean.getSharedDocuments()));
	    process.setCandidacyProcess(bean.getCandidacyProcess());

	    process.createState(PhdCandidacyFeedbackStateType.NEW, userView.getPerson(), null);

	    return process;
	}
    }

    static public class EditSharedDocumentTypes extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, IUserView userView) {
	    if (!process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process,
		IUserView userView, Object object) {

	    final PhdCandidacyFeedbackRequestProcessBean bean = (PhdCandidacyFeedbackRequestProcessBean) object;
	    process.setSharedDocuments(new PhdCandidacySharedDocumentsList(bean.getSharedDocuments()));

	    return process;
	}
    }

    static public class AddPhdCandidacyFeedbackRequestElement extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, IUserView userView) {
	    if (!process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process,
		IUserView userView, Object object) {

	    final PhdCandidacyFeedbackRequestElementBean bean = (PhdCandidacyFeedbackRequestElementBean) object;
	    final PhdCandidacyFeedbackRequestElement element = PhdCandidacyFeedbackRequestElement.create(process, bean);

	    addAccessPermissionsIfNecessary(process, element);
	    notifyElement(process, element, bean);

	    return process;
	}

	private void addAccessPermissionsIfNecessary(final PhdCandidacyFeedbackRequestProcess process,
		final PhdCandidacyFeedbackRequestElement element) {

	    if (!element.getParticipant().isInternal()) {
		element.getParticipant().addAccessType(PhdProcessAccessType.CANDIDACY_FEEDBACK_DOCUMENTS_DOWNLOAD,
			PhdProcessAccessType.CANDIDACY_FEEDBACK_UPLOAD);
	    }
	}

	private void notifyElement(final PhdCandidacyFeedbackRequestProcess process,
		final PhdCandidacyFeedbackRequestElement element, final PhdCandidacyFeedbackRequestElementBean bean) {

	    final String body = bean.getMailBody() + "\n\n"
		    + getAccessInformation(process.getIndividualProgramProcess(), element.getParticipant()) + "\n\n";

	    email(element.getEmail(), bean.getMailSubject(), body);
	}

	private String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant) {

	    if (!participant.isInternal()) {
		return AlertMessage.get("message.phd.external.access", PhdProperties.getPhdExternalAccessLink(), participant
			.getAccessHashCode(), participant.getPassword());

	    } else {
		final Person person = ((InternalPhdParticipant) participant).getPerson();

		if (process.isCoordinatorForPhdProgram(person)) {
		    return AlertMessage.get("message.phd.candidacy.feedback.coordinator.access");

		} else if (process.isGuiderOrAssistentGuider(person) || person.hasRole(RoleType.TEACHER)) {
		    return AlertMessage.get("message.phd.candidacy.feedback.teacher.access");
		}
	    }

	    throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
	}

	private void email(String email, String subject, String body) {
	    final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	    new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
	}
    }

    static public class DownloadCandidacyFeedbackDocuments extends ExternalAccessPhdActivity<PhdCandidacyFeedbackRequestProcess> {

	@Override
	public void checkPreConditions(PhdCandidacyFeedbackRequestProcess process, IUserView userView) {
	    // TODO Auto-generated method stub
	}

	@Override
	protected PhdCandidacyFeedbackRequestProcess internalExecuteActivity(PhdCandidacyFeedbackRequestProcess process,
		IUserView userView, PhdExternalOperationBean bean) {
	    // TODO Auto-generated method stub
	    return process;
	}
    }

    static public class UploadCandidacyFeedback extends ExternalAccessPhdActivity<PhdCandidacyFeedbackRequestProcess> {

	@Override
	public void checkPreConditions(PhdCandidacyFeedbackRequestProcess process, IUserView userView) {

	    if (!process.getCandidacyProcess().isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected PhdCandidacyFeedbackRequestProcess internalExecuteActivity(PhdCandidacyFeedbackRequestProcess process,
		IUserView userView, PhdExternalOperationBean bean) {

	    if (bean.getDocumentBean().hasAnyInformation()) {

		final PhdCandidacyFeedbackRequestElement element = process.getCandidacyFeedbackRequestElement(bean
			.getParticipant());

		final PhdProgramDocumentUploadBean documentBean = bean.getDocumentBean();

		new PhdCandidacyFeedbackRequestDocument(element, documentBean.getRemarks(), documentBean.getFileContent(),
			documentBean.getFilename(), null);
	    }

	    return process;
	}
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditSharedDocumentTypes());
	activities.add(new AddPhdCandidacyFeedbackRequestElement());

	activities.add(new DownloadCandidacyFeedbackDocuments());
    }
}
