package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {

    }

    @StartActivity
    public static class CreateCandidacy extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, IUserView userView,
		Object object) {

	    final PhdProgramCandidacyProcessBean individualProgramBean = (PhdProgramCandidacyProcessBean) object;
	    final PhdIndividualProgramProcess createdProcess = new PhdIndividualProgramProcess(individualProgramBean
		    .getOrCreatePersonFromBean(), individualProgramBean.getExecutionYear());
	    final PhdProgramCandidacyProcess candidacyProcess = Process.createNewProcess(userView,
		    PhdProgramCandidacyProcess.class.getSimpleName(), object);

	    candidacyProcess.setIndividualProgramProcess(createdProcess);

	    return createdProcess;

	}

    }

    private PhdIndividualProgramProcess(final Person person, final ExecutionYear executionYear) {
	super();
	setPerson(person);
	setExecutionYear(executionYear);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return true;
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
