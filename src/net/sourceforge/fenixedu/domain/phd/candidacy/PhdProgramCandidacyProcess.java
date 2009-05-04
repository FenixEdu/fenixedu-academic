package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    @StartActivity
    public static class CreateCandidacy extends Activity<PhdProgramCandidacyProcess> {

	@Override
	public void checkPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return new PhdProgramCandidacyProcess((PhdProgramCandidacyProcessBean) object);
	}

    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {

    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean candidacyProcessBean) {
	setCandidacy(new PHDProgramCandidacy(candidacyProcessBean.getOrCreatePersonFromBean()));

	if (candidacyProcessBean.hasDegree()) {
	    getCandidacy().setExecutionDegree(candidacyProcessBean.getExecutionDegree());
	}
    }

    public void insertCandidacy() {
	setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);
    }

    public void fillMissingInformation() {
	setState(PhdProgramCandidacyProcessState.STAND_BY_COMPLETE_INFORMATION);
    }

    public void validateCandidacy() {
	setState(PhdProgramCandidacyProcessState.VALIDATED);
    }

    public void sentToApproval() {
	setState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
    }

    public void issueCoordinatorOpinion() {
	setState(PhdProgramCandidacyProcessState.WAITING_FOR_CIENTIFIC_COUNCIL_RATIFICATION);
    }

    public void ratifyCandidacy() {
	setState(PhdProgramCandidacyProcessState.RATIFIED_BY_CIENTIFIC_COUNCIL);
    }

    public void formalizeRegistration() {
	setState(PhdProgramCandidacyProcessState.CONCLUDED);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	// TODO Auto-generated method stub
	return null;
    }

}
