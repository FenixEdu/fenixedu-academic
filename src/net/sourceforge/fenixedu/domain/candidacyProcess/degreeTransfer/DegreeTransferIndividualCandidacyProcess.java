package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeTransferIndividualCandidacyProcess extends DegreeTransferIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new EditCandidacyCurricularCoursesInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
    }

    private DegreeTransferIndividualCandidacyProcess() {
	super();
    }

    public DegreeTransferIndividualCandidacyProcess(final DegreeTransferIndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new DegreeTransferIndividualCandidacy(this, bean);
    }

    private void checkParameters(final DegreeTransferCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public DegreeTransferCandidacyProcess getCandidacyProcess() {
	return (DegreeTransferCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public DegreeTransferIndividualCandidacy getCandidacy() {
	return (DegreeTransferIndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public void editCandidacyInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    public void editCandidacyCurricularCoursesInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyCurricularCoursesInformation(bean);
    }

    public BigDecimal getCandidacyAffinity() {
	return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
	return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyApprovedEctsRate() {
	return getCandidacy().getApprovedEctsRate();
    }

    public BigDecimal getCandidacyGradeRate() {
	return getCandidacy().getGradeRate();
    }

    public BigDecimal getCandidacySeriesCandidacyGrade() {
	return getCandidacy().getSeriesCandidacyGrade();
    }
    
    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(final DegreeTransferIndividualCandidacyProcess dummy,
		final IUserView userView, final Object object) {
	    final DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) object;
	    return new DegreeTransferIndividualCandidacyProcess(bean);
	}
    }

    static private class CandidacyPayment extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return null; // nothing to be done, for now payment is being done by
	    // existing interfaces
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object).getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.isInStandBy() || process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyInformation((DegreeTransferIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class EditCandidacyCurricularCoursesInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyCurricularCoursesInformation((DegreeTransferIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || !process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((DegreeTransferIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE, Ingression.TRF);
	    return process;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final DegreeTransferIndividualCandidacyProcess process) {
	    return process.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }

}
