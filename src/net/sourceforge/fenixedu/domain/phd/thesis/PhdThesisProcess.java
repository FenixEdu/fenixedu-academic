package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;

public class PhdThesisProcess extends PhdThesisProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdThesisProcess> {

	@Override
	final public void checkPreConditions(final PhdThesisProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdThesisProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PhdThesisProcess process, final IUserView userView);
    }

    @StartActivity
    static public class RequestThesis extends PhdActivity {

	@Override
	public void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    // Activity on main process ensures access control
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcess result = new PhdThesisProcess();
	    final PhdThesisProcessBean phdThesisProcessBean = (PhdThesisProcessBean) object;

	    result.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION, userView.getPerson(),
		    phdThesisProcessBean.getRemarks());

	    return result;
	}
    }

    static public class SubmitThesis extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	    final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	    bean.getDocument().setType(
		    bean.getFinalThesis().booleanValue() ? PhdIndividualProgramDocumentType.FINAL_THESIS
			    : PhdIndividualProgramDocumentType.PROVISIONAL_THESIS);

	    process.addDocument(bean.getDocument(), userView.getPerson());

	    return process;

	}
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new RequestThesis());
	activities.add(new SubmitThesis());
    }

    private PhdThesisProcess() {
	super();
    }

    public void createState(PhdThesisProcessStateType type, Person person, String remarks) {
	new PhdThesisProcessState(this, type, person, remarks);
    }

    @Override
    protected Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    public PhdThesisProcessState getMostRecentState() {
	return hasAnyStates() ? Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public PhdThesisProcessStateType getActiveState() {
	final PhdThesisProcessState state = getMostRecentState();
	return state != null ? state.getType() : null;
    }

    public String getActiveStateRemarks() {
	return getMostRecentState().getRemarks();
    }

}
