package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.commons.CurricularCourseByExecutionSemesterBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class StandaloneIndividualCandidacyProcess extends StandaloneIndividualCandidacyProcess_Base {

    static private final List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
    }

    private StandaloneIndividualCandidacyProcess() {
	super();
    }

    public StandaloneIndividualCandidacyProcess(final StandaloneIndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new StandaloneIndividualCandidacy(this, bean);
    }

    private void checkParameters(final StandaloneCandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.StandaloneIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public StandaloneIndividualCandidacy getCandidacy() {
	return (StandaloneIndividualCandidacy) super.getCandidacy();
    }

    @Override
    public ExecutionSemester getCandidacyExecutionInterval() {
	return (ExecutionSemester) super.getCandidacyExecutionInterval();
    }

    public List<CurricularCourseByExecutionSemesterBean> getCurricularCourseBeans() {
	final List<CurricularCourseByExecutionSemesterBean> result = new ArrayList<CurricularCourseByExecutionSemesterBean>(
		getCandidacy().getCurricularCoursesCount());
	for (final CurricularCourse curricularCourse : getCandidacy().getCurricularCoursesSet()) {
	    result.add(new CurricularCourseByExecutionSemesterBean(curricularCourse, getCandidacyExecutionInterval()));
	}
	return result;
    }

    public List<CurricularCourse> getCurricularCourses() {
	return getCandidacy().getCurricularCourses();
    }

    public StandaloneIndividualCandidacyProcess editCandidacyInformation(final StandaloneIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean.getCandidacyDate(), bean.getCurricularCourses());
	return this;
    }

    public Degree getCandidacySelectedDegree() {
	return Degree.readEmptyDegree();
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess dummy,
		IUserView userView, Object object) {
	    return new StandaloneIndividualCandidacyProcess((StandaloneIndividualCandidacyProcessBean) object);
	}
    }

    static private class CandidacyPayment extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interface
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    final StandaloneIndividualCandidacyProcessBean bean = (StandaloneIndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((StandaloneIndividualCandidacyProcessBean) object);
	}
    }

    static private class IntroduceCandidacyResult extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((StandaloneIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<StandaloneIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(StandaloneIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }

	    if (process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected StandaloneIndividualCandidacyProcess executeActivity(StandaloneIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    createRegistration(process);
	    return process;
	}

	private void createRegistration(final StandaloneIndividualCandidacyProcess process) {
	    process.getCandidacy().createRegistration(DegreeCurricularPlan.readEmptyDegreeCurricularPlan(), null, Ingression.STC);
	}
    }

}
