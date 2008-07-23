package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeTransferIndividualCandidacyProcess extends DegreeTransferIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	// activities.add(new CandidacyPayment());
	// activities.add(new EditCandidacyPersonalInformation());
	// activities.add(new EditCandidacyInformation());
	// activities.add(new EditCandidacyCurricularCoursesInformation());
	// activities.add(new IntroduceCandidacyResult());
	// activities.add(new CancelCandidacy());
	// activities.add(new CreateRegistration());
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
}
