package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ErasmusIndividualCandidacyProcess extends ErasmusIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CancelCandidacy());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new EditDegreeAndCoursesInformation());

    }

    public ErasmusIndividualCandidacyProcess() {
	super();
    }

    public ErasmusIndividualCandidacyProcess(final ErasmusIndividualCandidacyProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);

	/*
	 * 27/04/2009 - New document files specific to Erasmus candidacies
	 */
	setSpecificIndividualCandidacyDocumentFiles(bean);

    }

    @Override
    protected void init(IndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyProcess());

	if (bean.getPublicCandidacyHashCode() == null) {
	    throw new DomainException("error.IndividualCandidacy.hash.code.is.null");
	}

	if (existsIndividualCandidacyProcessForDocumentId(bean.getCandidacyProcess(), bean.getPersonBean().getIdDocumentType(),
		bean.getPersonBean().getDocumentIdNumber())) {
	    throw new DomainException("error.IndividualCandidacy.exists.for.same.document.id");
	}

	setCandidacyProcess(bean.getCandidacyProcess());
	createIndividualCandidacy(bean);

	/*
	 * 11/04/2009 - An external candidacy submission requires documents like
	 * identification and habilitation certificate documents
	 */
	setCandidacyHashCode(bean.getPublicCandidacyHashCode());

	setCandidacyDocumentFiles(bean);

	setProcessCodeForThisIndividualCandidacy(bean.getCandidacyProcess());
    }

    private void setSpecificIndividualCandidacyDocumentFiles(ErasmusIndividualCandidacyProcessBean bean) {

    }

    @Override
    protected void checkParameters(final CandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    protected void createIndividualCandidacy(final IndividualCandidacyProcessBean bean) {
	new ErasmusIndividualCandidacy(this, (ErasmusIndividualCandidacyProcessBean) bean);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)
		|| userView.hasRoleType(RoleType.COORDINATOR);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @Override
    public ErasmusIndividualCandidacy getCandidacy() {
	return (ErasmusIndividualCandidacy) super.getCandidacy();
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    public String getCandidacyNotes() {
	return getCandidacy().getNotes();
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
	List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

	return missingDocumentFiles;
    }

    private ErasmusIndividualCandidacyProcess editCandidacyInformation(final ErasmusIndividualCandidacyProcessBean bean) {
	getCandidacy().getErasmusStudentData().edit(bean.getErasmusStudentDataBean());
	return this;
    }

    private ErasmusIndividualCandidacyProcess editDegreeAndCoursesInformation(final ErasmusIndividualCandidacyProcessBean bean) {
	getCandidacy().editDegreeAndCoursesInformation(bean);

	return this;
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    /*
	     * 06/04/2009 The candidacy may be submited by someone who's not
	     * authenticated in the system
	     * 
	     * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
	     * PreConditionNotValidException();}
	     */
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess dummy, IUserView userView,
		Object object) {
	    return new ErasmusIndividualCandidacyProcess((ErasmusIndividualCandidacyProcessBean) object);
	}
    }

    static private class CancelCandidacy extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || !process.isCandidacyInStandBy() || process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    final ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((ErasmusIndividualCandidacyProcessBean) object);
	}
    }

    static private class EditDegreeAndCoursesInformation extends Activity<ErasmusIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(ErasmusIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected ErasmusIndividualCandidacyProcess executeActivity(ErasmusIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editDegreeAndCoursesInformation((ErasmusIndividualCandidacyProcessBean) object);
	}

    }

}
