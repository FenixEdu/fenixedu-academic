package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.Pair;

public class PhdThesisProcess extends PhdThesisProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdThesisProcess> {

	@Override
	final public void checkPreConditions(final PhdThesisProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdThesisProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PhdThesisProcess process, final IUserView userView);
    }

    @StartActivity
    static public class RequestThesis extends PhdActivity {

	@Override
	public void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    // Activity on main process ensures access control
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcess result = new PhdThesisProcess();
	    final PhdThesisProcessBean phdThesisProcessBean = (PhdThesisProcessBean) object;
	    result.createState(PhdThesisProcessStateType.NEW, userView.getPerson(), phdThesisProcessBean.getRemarks());
	    return result;
	}
    }

    static public class RequestJuryElements extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (!process.getActiveState().equals(PhdThesisProcessStateType.NEW)) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
	    process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION, userView.getPerson(), bean.getRemarks());

	    AlertService.alertCoordinator(process.getIndividualProgramProcess(),
		    "message.phd.alert.request.jury.elements.subject", "message.phd.alert.request.jury.elements.body");

	    return process;
	}
    }

    static public class SubmitJuryElementsDocuments extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (!process.isJuryValidated()) {
		return;
	    }

	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		return;
	    }

	    if (userView.getPerson() != null
		    && process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	    boolean anyDocumentSubmitted = false;

	    for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
		if (each.hasAnyInformation()) {

		    process.addDocument(each, userView.getPerson());
		    alertIfNecessary(process, each, userView.getPerson());

		    anyDocumentSubmitted = true;
		}
	    }

	    if (anyDocumentSubmitted) {
		if (!process.hasState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION)) {
		    process.createState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION, userView.getPerson(), bean
			    .getRemarks());
		}
	    }

	    return process;
	}

	private void alertIfNecessary(PhdThesisProcess process, PhdProgramDocumentUploadBean each, Person person) {

	    switch (each.getType()) {
	    case JURY_PRESIDENT_ELEMENT:
		AlertService.alertCoordinator(process.getIndividualProgramProcess(),
			"message.phd.alert.request.jury.president.subject", "message.phd.alert.request.jury.president.body");
		break;

	    case JURY_ELEMENTS:
		if (process.getIndividualProgramProcess().isCoordinatorForPhdProgram(person)) {
		    AlertService
			    .alertAcademicOffice(process.getIndividualProgramProcess(),
				    "message.phd.alert.jury.elements.submitted.subject",
				    "message.phd.alert.jury.elements.submitted.body");
		}
		break;
	    }
	}
    }

    static public class RejectJuryElements extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (!process.getActiveState().equals(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION)
		    || !isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION, userView.getPerson(),
		    ((PhdThesisProcessBean) object).getRemarks());

	    AlertService.alertCoordinator(process.getIndividualProgramProcess(),
		    "message.phd.alert.jury.elements.rejected.subject", "message.phd.alert.jury.elements.rejected.body");

	    return process;
	}
    }

    static public class SubmitThesis extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (!process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	    for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
		if (each.hasAnyInformation()) {
		    process.addDocument(each, userView.getPerson());
		}
	    }

	    return process;

	}
    }

    static public class AddJuryElement extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    ThesisJuryElement.create(process, (PhdThesisJuryElementBean) object);
	    return process;
	}
    }

    static public class DeleteJuryElement extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final ThesisJuryElement element = (ThesisJuryElement) object;
	    process.deleteJuryElement(element);
	    return process;
	}
    }

    static public class SwapJuryElementsOrder extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final Pair<ThesisJuryElement, ThesisJuryElement> elements = (Pair<ThesisJuryElement, ThesisJuryElement>) object;
	    process.swapJuryElementsOrder(elements.getKey(), elements.getValue());
	    return process;
	}

    }

    static public class AddPresidentJuryElement extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    if (process.hasPresidentJuryElement()) {
		process.getPresidentJuryElement().delete();
	    }

	    ThesisJuryElement.createPresident(process, (PhdThesisJuryElementBean) object);
	    return process;
	}
    }

    static public class ValidateJury extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
	    process.setWhenJuryValidated(bean.getWhenJuryValidated());
	    process.setWhenJuryDesignated(bean.getWhenJuryDesignated());

	    /*
	     * TODO: SEND ALERT!!!!!!!!!!!!
	     */

	    return process;
	}
    }

    static public class PrintJuryElementsDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (!process.isJuryValidated()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    // nothing to be done
	    return process;
	}

    }

    // TODO: find clean solution to return documents
    // grouped?
    static public class DownloadProvisionalThesisDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.hasProvisionalThesisDocument() && isParticipant(process, userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    // nothing to be done
	    return null;
	}
    }

    static public class DownloadFinalThesisDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.hasFinalThesisDocument() && isParticipant(process, userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    // nothing to be done

	    return null;
	}

    }

    static public class DownloadThesisRequirement extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	    if (process.hasThesisRequirementDocument() && isParticipant(process, userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    // nothing to be done

	    return null;
	}

    }

    static public class RequestJuryReviews extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		return;
	    }

	    throw new PreConditionNotValidException();
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	    createExternalAccesses(process);

	    if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
		process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(), null);
	    }

	    return process;
	}

	private void createExternalAccesses(PhdThesisProcess process) {

	    for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {
		if (!isCoordinatorOrGuider(process, juryElement)) {

		    final PhdParticipant participant = juryElement.getParticipant();
		    participant.addAccessType(PhdProcessAccessType.JURY_DOCUMENTS_DOWNLOAD);

		    if (juryElement.getReporter().booleanValue()) {
			participant.addAccessType(PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD);
		    }
		    
		    //TODO: send login information
		    //TODO: generate alert
		}
	    }
	}

	private boolean isCoordinatorOrGuider(PhdThesisProcess process, final ThesisJuryElement juryElement) {
	    if (!juryElement.isInternal()) {
		return false;
	    }

	    final Person person = ((InternalPhdParticipant) juryElement.getParticipant()).getPerson();

	    return process.getIndividualProgramProcess().isCoordinatorForPhdProgram(person)
		    || (process.getIndividualProgramProcess().isGuiderOrAssistentGuider(person) && person
			    .hasRole(RoleType.TEACHER));
	}
    }

    static abstract protected class ExternalAccessPhdActivity extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdExternalOperationBean bean = (PhdExternalOperationBean) object;
	    bean.getParticipant().checkAccessCredentials(bean.getEmail(), bean.getPassword());

	    return internalExecuteActivity(process, userView, bean);
	}

	abstract protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, IUserView userView,
		PhdExternalOperationBean bean);

    }

    static public class JuryDocumentsDownload extends ExternalAccessPhdActivity {

	@Override
	protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, IUserView userView,
		PhdExternalOperationBean bean) {

	    return process;
	}

    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new RequestJuryElements());
	activities.add(new SubmitJuryElementsDocuments());
	activities.add(new AddJuryElement());
	activities.add(new DeleteJuryElement());
	activities.add(new SwapJuryElementsOrder());
	activities.add(new AddPresidentJuryElement());
	activities.add(new ValidateJury());
	activities.add(new PrintJuryElementsDocument());
	activities.add(new SubmitThesis());
	activities.add(new DownloadProvisionalThesisDocument());
	activities.add(new DownloadFinalThesisDocument());
	activities.add(new DownloadThesisRequirement());
	activities.add(new RequestJuryReviews());
	activities.add(new JuryDocumentsDownload());
    }

    private PhdThesisProcess() {
	super();
    }

    public boolean isJuryValidated() {
	return getWhenJuryValidated() != null;
    }

    private void swapJuryElementsOrder(ThesisJuryElement e1, ThesisJuryElement e2) {
	if (hasThesisJuryElements(e1) && hasThesisJuryElements(e2)) {
	    final Integer order1 = e1.getElementOrder();
	    final Integer order2 = e2.getElementOrder();
	    e1.setElementOrder(order2);
	    e2.setElementOrder(order1);
	}
    }

    private void deleteJuryElement(ThesisJuryElement element) {
	if (hasThesisJuryElements(element)) {
	    final Integer elementOrder = element.getElementOrder();
	    element.delete();
	    reorderJuryElements(elementOrder);
	}
    }

    private void reorderJuryElements(Integer removedElementOrder) {
	for (final ThesisJuryElement element : getOrderedThesisJuryElements()) {
	    if (element.getElementOrder().compareTo(removedElementOrder) > 0) {
		element.setElementOrder(Integer.valueOf(element.getElementOrder().intValue() - 1));
	    }
	}
    }

    public TreeSet<ThesisJuryElement> getOrderedThesisJuryElements() {
	final TreeSet<ThesisJuryElement> result = new TreeSet<ThesisJuryElement>(ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER);
	result.addAll(getThesisJuryElementsSet());
	return result;
    }

    public void createState(PhdThesisProcessStateType type, Person person, String remarks) {
	new PhdThesisProcessState(this, type, person, remarks);
    }

    @Override
    public Person getPerson() {
	return getIndividualProgramProcess().getPerson();
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

    @Override
    public PhdThesisProcessState getMostRecentState() {
	return (PhdThesisProcessState) super.getMostRecentState();
    }

    @Override
    public PhdThesisProcessStateType getActiveState() {
	return (PhdThesisProcessStateType) super.getActiveState();
    }

    // TODO: find clean solution to return specific documents
    // grouped??
    public PhdProgramProcessDocument getProvisionalThesisDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS);
    }

    public boolean hasProvisionalThesisDocument() {
	return getProvisionalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getFinalThesisDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.FINAL_THESIS);
    }

    public boolean hasFinalThesisDocument() {
	return getFinalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getThesisRequirementDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.THESIS_REQUIREMENT);
    }

    public boolean hasThesisRequirementDocument() {
	return getThesisRequirementDocument() != null;
    }

    boolean isConcluded() {
	return getActiveState() == PhdThesisProcessStateType.CONCLUDED;
    }

    public PhdProgramProcessDocument getJuryElementsDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_ELEMENTS);
    }

    public PhdProgramProcessDocument getJuryPresidentDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT);
    }

    public DateTime getWhenRequestJury() {
	return getLastExecutionDateOf(RequestJuryElements.class);
    }

    public DateTime getWhenReceivedJury() {
	return getLastExecutionDateOf(SubmitJuryElementsDocuments.class);
    }
}
