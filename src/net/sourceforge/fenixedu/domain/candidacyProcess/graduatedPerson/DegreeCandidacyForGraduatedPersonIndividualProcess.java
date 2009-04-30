package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.CancelCandidacy;

public class DegreeCandidacyForGraduatedPersonIndividualProcess extends DegreeCandidacyForGraduatedPersonIndividualProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCommonCandidacyInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
	activities.add(new EditPublicCandidacyPersonalInformation());
	activities.add(new EditPublicCandidacyDocumentFile());
	
    }

    private DegreeCandidacyForGraduatedPersonIndividualProcess() {
	super();
    }

    private DegreeCandidacyForGraduatedPersonIndividualProcess(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);
    }

    protected void checkParameters(final CandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonIndividualProcess.invalid.candidacy.process");
	}
    }

    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
	new DegreeCandidacyForGraduatedPerson(this, (DegreeCandidacyForGraduatedPersonIndividualProcessBean) bean);
    }

    @Override
    public DegreeCandidacyForGraduatedPerson getCandidacy() {
	return (DegreeCandidacyForGraduatedPerson) super.getCandidacy();
    }

    @Override
    public DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess() {
	return (DegreeCandidacyForGraduatedPersonProcess) super.getCandidacyProcess();
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

    public BigDecimal getCandidacyAffinity() {
	return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
	return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyGrade() {
	return getCandidacy().getCandidacyGrade();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    private void editCandidacyInformation(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
		/* 
		 * 06/04/2009
		 * The candidacy may be submited by someone who's not authenticated in the system
		 *
	     *if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		 *throw new PreConditionNotValidException();
	     *}
		 */
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess dummy, IUserView userView, Object object) {
	    return new DegreeCandidacyForGraduatedPersonIndividualProcess(
		    (DegreeCandidacyForGraduatedPersonIndividualProcessBean) object);
	}
    }

    static private class CandidacyPayment extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interface
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {

	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object)
		    .getPersonBean());
	    return process;
	}
    }

    static private class EditCommonCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {

	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.editCommonCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object)
		    .getCandidacyInformationBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {

	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.editCandidacyInformation((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object);
	    return process;
	}
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
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
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
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
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(
		DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView, Object object) {
	    process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE, Ingression.CEA02);
	    return process;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(
		final DegreeCandidacyForGraduatedPersonIndividualProcess candidacyProcess) {
	    return candidacyProcess.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }	    
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(DegreeCandidacyForGraduatedPersonIndividualProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object).getPersonBean());
	    process.editCommonCandidacyInformation(((DegreeCandidacyForGraduatedPersonIndividualProcessBean) object).getCandidacyInformationBean());
	    return process;
	}
	
    }
    
    static private class EditPublicCandidacyDocumentFile extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }	    
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonIndividualProcess executeActivity(DegreeCandidacyForGraduatedPersonIndividualProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object; 
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}
    }

}
