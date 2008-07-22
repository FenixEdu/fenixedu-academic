package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

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

public class DegreeChangeIndividualCandidacyProcess extends DegreeChangeIndividualCandidacyProcess_Base {

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

    private DegreeChangeIndividualCandidacyProcess() {
	super();
    }

    private DegreeChangeIndividualCandidacyProcess(final DegreeChangeIndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new DegreeChangeIndividualCandidacy(this, bean);
    }

    private void checkParameters(final DegreeChangeCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public DegreeChangeCandidacyProcess getCandidacyProcess() {
	return (DegreeChangeCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public DegreeChangeIndividualCandidacy getCandidacy() {
	return (DegreeChangeIndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(final IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    private void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }
    
    private void editCandidacyCurricularCoursesInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
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
    static public class IndividualCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess dummy,
		IUserView userView, Object object) {
	    final DegreeChangeIndividualCandidacyProcessBean bean = (DegreeChangeIndividualCandidacyProcessBean) object;
	    return new DegreeChangeIndividualCandidacyProcess(bean);
	}
    }

    static private class CandidacyPayment extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interfaces
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object).getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.isInStandBy() || process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyInformation((DegreeChangeIndividualCandidacyProcessBean) object);
	    return process;
	}
    }
    
    static private class EditCandidacyCurricularCoursesInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyCurricularCoursesInformation((DegreeChangeIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((DegreeChangeIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE,
		    getIngression(process));
	    return process;
	}

	private Ingression getIngression(final DegreeChangeIndividualCandidacyProcess process) {
	    return process.getCandidacyPrecedentDegreeInformation().isExternal() ? Ingression.MCE : Ingression.MCI;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final DegreeChangeIndividualCandidacyProcess process) {
	    return process.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }

}
