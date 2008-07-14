package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeChangeIndividualCandidacyProcess extends DegreeChangeIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	// activities.add(new EditCandidacyPersonalInformation());
	// activities.add(new EditCandidacyInformation());
	// activities.add(new IntroduceCandidacyResult());
	// activities.add(new CancelCandidacy());
	// activities.add(new CreateRegistration());
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
	if (candidacyProcess == null || !candidacyProcess.hasOpenCandidacyPeriod()) {
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

}
