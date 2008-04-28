package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class Over23IndividualCandidacyProcess extends Over23IndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
    }

    protected Over23IndividualCandidacyProcess() {
	super();
    }

    protected Over23IndividualCandidacyProcess(final Over23IndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new Over23IndividualCandidacy(this, getPersonFromBean(bean), bean.getSelectedDegrees());
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

    private void editPersonalCandidacyInformation(final PersonBean personBean) {
	getCandidacy().editPersonalCandidacyInformation(personBean);
    }

    private boolean hasAnyPaymentForCandidacy() {
	return getCandidacy().hasAnyPayment();
    }

    private void cancelCandidacy() {
	getCandidacy().cancel();
    }

    private boolean isCandidacyProcessInStandBy() {
	return getCandidacyProcess().isInStandBy();
    }

    private boolean isCandidacyAccepted() {
	return getCandidacy().isAccepted();
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
	name += " - " + getCandidacy().getPerson().getName() + " (" + getCandidacy().getPerson().getDocumentIdNumber() + ")";
	return name;
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

    static public class CandidacyPayment extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; //nothing to be done, for now payment is being done by existing interface
	}
    }

    static public class EditCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static public class EditCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!(isDegreeAdministrativeOfficeEmployee(userView) && process.isCandidacyProcessInStandBy() && !process
		    .isCandidacyAccepted())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) object;
	    process.getCandidacy().modifyChoosenDegrees(bean.getSelectedDegrees());
	    return process;
	}
    }

    static public class IntroduceCandidacyResult extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static public class CancelCandidacy extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!(isDegreeAdministrativeOfficeEmployee(userView) && !process.hasAnyPaymentForCandidacy() && process
		    .isCandidacyProcessInStandBy())) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {

	    //TODO: add responsible user?
	    process.cancelCandidacy();
	    return process;
	}
    }

}
