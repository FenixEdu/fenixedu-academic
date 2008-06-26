package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeCandidacyForGraduatedPersonIndividualProcess extends DegreeCandidacyForGraduatedPersonIndividualProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new CancelCandidacy());
    }

    private DegreeCandidacyForGraduatedPersonIndividualProcess() {
	super();
    }

    protected DegreeCandidacyForGraduatedPersonIndividualProcess(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new DegreeCandidacyForGraduatedPerson(this, bean);
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasOpenCandidacyPeriod()) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonIndividualProcess.invalid.candidacy.process");
	}
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

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
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

    static private class CancelCandidacy extends Activity<DegreeCandidacyForGraduatedPersonIndividualProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonIndividualProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isInStandBy()) {
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

}
