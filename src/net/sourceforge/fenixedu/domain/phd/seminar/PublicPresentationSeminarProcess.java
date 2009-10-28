package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;

public class PublicPresentationSeminarProcess extends PublicPresentationSeminarProcess_Base {

    static abstract private class PhdActivity extends Activity<PublicPresentationSeminarProcess> {

	@Override
	public void checkPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PublicPresentationSeminarProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PublicPresentationSeminarProcess process, final IUserView userView);
    }

    @StartActivity
    static public class RequestComission extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    // Activity on main process ensures access control

	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess noProcess,
		IUserView userView, Object object) {
	    final PublicPresentationSeminarProcess result = new PublicPresentationSeminarProcess();
	    result.createState(PublicPresentationSeminarProcessStateType.WAITING_FOR_COMISSION_CONSTITUTION,
		    userView.getPerson(), ((PublicPresentationSeminarProcessBean) object).getRemarks());

	    return result;
	}
    }

    static public class SubmitComission extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    if (process.getActiveState() != PublicPresentationSeminarProcessStateType.WAITING_FOR_COMISSION_CONSTITUTION) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)
		    && !process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
	    bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
	    process.addDocument(bean.getDocument(), userView.getPerson());
	    process.createState(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION,
		    userView.getPerson(), bean.getRemarks());

	    AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.comission.validation.subject",
		    "message.phd.alert.public.presentation.seminar.comission.validation.body");

	    return process;
	}

    }

    static public class ValidateComission extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {

	    if (process.getActiveState() != PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView))
		throw new PreConditionNotValidException();
	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
	    bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
	    process.addDocument(bean.getDocument(), userView.getPerson());
	    process.createState(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED, userView.getPerson(), bean
		    .getRemarks());

	    AlertService.alertGuiders(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.comission.validated.subject",
		    "message.phd.alert.public.presentation.seminar.comission.validated.body");

	    AlertService.alertStudent(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.comission.validated.subject",
		    "message.phd.alert.public.presentation.seminar.comission.validated.body");

	    return process;
	}

    }

    static public class SchedulePresentationDate extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {

	    if (process.getActiveState() != PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)
		    && !process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson()))
		throw new PreConditionNotValidException();
	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
	    process.setPresentationDate(bean.getPresentationDate());
	    process.createState(PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED, userView
		    .getPerson(), bean.getRemarks());

	    AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");

	    AlertService.alertGuiders(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");

	    AlertService.alertStudent(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.subject",
		    "message.phd.alert.public.presentation.seminar.scheduled.presentation.date.body");

	    return process;
	}
    }

    static public class UploadReport extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    if (process.getActiveState() != PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)
		    && !process.getIndividualProgramProcess().isGuider(userView.getPerson()))
		throw new PreConditionNotValidException();
	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
	    bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);
	    process.addDocument(bean.getDocument(), userView.getPerson());
	    process.createState(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION, userView.getPerson(),
		    bean.getRemarks());

	    AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.report.uploaded.subject",
		    "message.phd.alert.public.presentation.seminar.report.uploaded.body");

	    return process;
	}
    }

    static public class ValidateReport extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    if (process.getActiveState() != PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) {
		throw new PreConditionNotValidException();
	    }

	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView))
		throw new PreConditionNotValidException();
	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {

	    final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
	    bean.getDocument().setType(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);
	    process.addDocument(bean.getDocument(), userView.getPerson());
	    process.createState(PublicPresentationSeminarProcessStateType.REPORT_VALIDATED, userView.getPerson(), bean
		    .getRemarks());

	    AlertService.alertCoordinator(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.report.validated.subject",
		    "message.phd.alert.public.presentation.seminar.report.validated.body");

	    AlertService.alertGuiders(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.report.validated.subject",
		    "message.phd.alert.public.presentation.seminar.report.validated.body");

	    AlertService.alertStudent(process.getIndividualProgramProcess(),
		    "message.phd.alert.public.presentation.seminar.report.validated.subject",
		    "message.phd.alert.public.presentation.seminar.report.validated.body");

	    return process;
	}
    }

    static public class DownloadReportDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {

	    if (!process.hasReportDocument()) {
		throw new PreConditionNotValidException();
	    }

	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)
		    || process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())
		    || process.getIndividualProgramProcess().isGuiderOrAssistentGuider(userView.getPerson())) {
		return;
	    }

	    if (process.getActiveState() == PublicPresentationSeminarProcessStateType.REPORT_VALIDATED
		    && process.getIndividualProgramProcess().getPerson() == userView.getPerson()) {
		return;
	    }

	    throw new PreConditionNotValidException();

	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {
	    // Nothing to be done
	    return null;
	}

    }

    static public class DownloadComissionDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    if (!process.hasComissionDocument()) {
		throw new PreConditionNotValidException();
	    }

	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)
		    || process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())
		    || process.getIndividualProgramProcess().isGuiderOrAssistentGuider(userView.getPerson())) {
		return;
	    }

	    if (process.hasState(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED)
		    && process.getIndividualProgramProcess().getPerson() == userView.getPerson()) {
		return;
	    }

	    throw new PreConditionNotValidException();

	}

	@Override
	protected PublicPresentationSeminarProcess executeActivity(PublicPresentationSeminarProcess process, IUserView userView,
		Object object) {
	    // Nothing to be done

	    return null;
	}

    }

    static private List<Activity> activities = new ArrayList<Activity>();

    static {
	activities.add(new SubmitComission());
	activities.add(new ValidateComission());
	activities.add(new SchedulePresentationDate());
	activities.add(new UploadReport());
	activities.add(new ValidateReport());
	activities.add(new DownloadReportDocument());
	activities.add(new DownloadComissionDocument());
    }

    private PublicPresentationSeminarProcess() {

    }

    public boolean hasReportDocument() {
	return getReportDocument() != null;
    }

    public PhdProgramProcessDocument getReportDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_REPORT);
    }

    public boolean hasComissionDocument() {
	return getComissionDocument() != null;
    }

    public PhdProgramProcessDocument getComissionDocument() {
	return getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public PublicPresentationSeminarState getMostRecentState() {
	return hasAnyStates() ? Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public String getActiveStateRemarks() {
	return getMostRecentState().getRemarks();
    }

    public PublicPresentationSeminarProcessStateType getActiveState() {
	final PublicPresentationSeminarState state = getMostRecentState();
	return state != null ? state.getType() : null;
    }

    private void createState(final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {
	new PublicPresentationSeminarState(this, type, person, remarks);
    }

    @Override
    protected Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    public boolean hasState(PublicPresentationSeminarProcessStateType type) {
	for (final PublicPresentationSeminarState state : getStates()) {
	    if (state.getType() == type) {
		return true;
	    }
	}

	return false;
    }
}
