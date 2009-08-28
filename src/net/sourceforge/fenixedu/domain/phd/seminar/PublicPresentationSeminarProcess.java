package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;

public class PublicPresentationSeminarProcess extends PublicPresentationSeminarProcess_Base {

    static abstract private class PhdActivity extends Activity<PublicPresentationSeminarProcess> {

	@Override
	public void checkPreConditions(PublicPresentationSeminarProcess process, IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PublicPresentationSeminarProcess process, final IUserView userView) {
	}

	abstract protected void activityPreConditions(final PublicPresentationSeminarProcess process, final IUserView userView);
    }

    static private List<Activity> activities = new ArrayList<Activity>();

    private PublicPresentationSeminarProcess() {
	super();
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public PublicPresentationSeminarState getActiveState() {
	return hasAnyStates() ? Collections.max(getStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public PublicPresentationSeminarProcessStateType getActiveStateType() {
	final PublicPresentationSeminarState state = getActiveState();
	return state != null ? state.getType() : null;
    }

    private void createState(final PublicPresentationSeminarProcessStateType type, final Person person) {
	createState(type, person, null);
    }

    private void createState(final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {
	new PublicPresentationSeminarState(this, type, person, remarks);
    }
}
