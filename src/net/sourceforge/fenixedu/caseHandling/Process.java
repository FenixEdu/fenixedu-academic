package net.sourceforge.fenixedu.caseHandling;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.IUserView;

public abstract class Process {

    private Collection<Activity> activities = new ArrayList<Activity>();

    public Collection<? extends Activity> getActivities() {
	return activities;
    }

    protected void addActivity(Activity activity) {
	this.activities.add(activity);
    }

    public Collection<PreConditionResult> getUserActivitiesPreConditionsResult(IUserView userView) {
	final Collection<PreConditionResult> results = new ArrayList<PreConditionResult>();
	for (final Activity activity : getActivities()) {
	    PreConditionResult conditionResult = activity.checkPreConditions(userView);
	    if (!conditionResult.isGroupNotChecked()) {
		results.add(conditionResult);
	    }
	}
	return results;
    }

}
