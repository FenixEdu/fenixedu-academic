package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class Over23IndividualCandidacyProcess extends Over23IndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	//TODO:	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
    }

    protected Over23IndividualCandidacyProcess() {
	super();
    }

    protected Over23IndividualCandidacyProcess(final Over23IndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new Over23IndividualCandidacy(this, getPersonFromBean(bean), bean.getSelectedDegrees(), bean.getDisabilities(), bean
		.getEducation(), bean.getLanguages());
    }

    private Person getPersonFromBean(final Over23IndividualCandidacyProcessBean bean) {
	if (bean.getPersonBean().hasPerson()) {
	    return bean.getPersonBean().getPerson().edit(bean.getPersonBean());
	} else {
	    return new Person(bean.getPersonBean());
	}
    }

    private void checkParameters(final Over23CandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod() || !candidacyProcess.hasOpenCandidacyPeriod()) {
	    throw new DomainException("error.Over23IndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public Over23IndividualCandidacy getCandidacy() {
	return (Over23IndividualCandidacy) super.getCandidacy();
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
    public String getDisplayName() {
	String name = ResourceBundle.getBundle("resources/CaseHandlingResources").getString("label." + getClass().getName());
	name += " - " + getCandidacy().getPerson().getName() + " (" + getCandidacy().getPerson().getDocumentIdNumber() + "), ";
	name += ResourceBundle.getBundle("resources/EnumerationResources")
		.getString(getCandidacy().getState().getQualifiedName());
	return name;
    }

    private Over23IndividualCandidacyProcess editPersonalCandidacyInformation(final PersonBean personBean) {
	getCandidacy().editPersonalCandidacyInformation(personBean);
	return this;
    }

    private Over23IndividualCandidacyProcess editCandidacyInformation(final Over23IndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean.getSelectedDegrees(), bean.getDisabilities(), bean.getEducation(),
		bean.getLanguages());
	return this;
    }

    public List<Degree> getSelectedDegreesSortedByOrder() {
	return getCandidacy().getSelectedDegreesSortedByOrder();
    }

    public String getDisabilities() {
	return getCandidacy().getDisabilities();
    }

    public String getEducation() {
	return getCandidacy().getEducation();
    }

    public String getLanguages() {
	return getCandidacy().getLanguages();
    }

    boolean canBeSendToJury() {
	return isCandidacyInStandBy() && isCandidacyDebtPayed();
    }

    Degree getAcceptedDegree() {
	return getCandidacy().getAcceptedDegree();
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess dummy, IUserView userView,
		Object object) {
	    return new Over23IndividualCandidacyProcess((Over23IndividualCandidacyProcessBean) object);
	}
    }

    static private class CandidacyPayment extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; //nothing to be done, for now payment is being done by existing interface
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) object;
	    return process.editPersonalCandidacyInformation(bean.getPersonBean());
	}
    }

    static private class EditCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.isInStandBy() || process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    return process.editCandidacyInformation((Over23IndividualCandidacyProcessBean) object);
	}
    }

    static private class IntroduceCandidacyResult extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isCandidacyInStandBy() || !process.isSentToJury() || !process.isPublished()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyResultBean bean = (Over23IndividualCandidacyResultBean) object;
	    process.getCandidacy().setCandidacyResult(bean.getState(), bean.getAcceptedDegree());
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }
}
