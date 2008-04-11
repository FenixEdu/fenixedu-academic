package net.sourceforge.fenixedu.caseHandling;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public abstract class PreCondition {

    private Group group;

    private String errorMessage;

    public PreCondition(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public PreCondition(Group group, String errorMessage) {
	this.group = group;
	this.errorMessage = errorMessage;
    }

    abstract public boolean execute();

    public PreConditionResult check(IUserView userView, Activity activity) {
	if (isMember(userView)) {
	    return execute() ? PreConditionResult.checked(activity) : PreConditionResult.notChecked(activity, getErrorMessage());
	}
	return PreConditionResult.groupNotChecked(activity);
    }

    public boolean isMember(IUserView userView) {
	return getGroup() == null || getGroup().allows(userView);
    }

    public Group getGroup() {
	return group;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

}
