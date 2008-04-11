package net.sourceforge.fenixedu.caseHandling;

import java.util.Collection;

public class PreConditionResult {

    private Activity activity;

    private PreConditionResultType resultType;

    private String resultMessage;

    private Collection<String> errorMessages;

    private PreConditionResult(Activity activity, PreConditionResultType resultType, String resultMessage,
	    Collection<String> errorMessages) {
	super();
	this.activity = activity;
	this.resultType = resultType;
	this.resultMessage = resultMessage;
	this.errorMessages = errorMessages;
    }

    static public PreConditionResult checked(Activity activity) {
	return new PreConditionResult(activity, PreConditionResultType.CHECKED, null, null);
    }

    static public PreConditionResult notChecked(Activity activity, String message) {
	return new PreConditionResult(activity, PreConditionResultType.NOT_CHECKED, message, null);
    }

    static public PreConditionResult notChecked(Activity activity, Collection<String> errors) {
	return new PreConditionResult(activity, PreConditionResultType.NOT_CHECKED, null, errors);
    }

    static public PreConditionResult groupNotChecked(Activity activity) {
	return new PreConditionResult(activity, PreConditionResultType.GROUP_NOT_CHECKED, null, null);
    }

    public PreConditionResultType getResultType() {
	return resultType;
    }

    public String getResultMessage() {
	return resultMessage;
    }

    public Collection<String> getErrorMessages() {
	return errorMessages;
    }

    public Activity getActivity() {
	return activity;
    }

    public boolean isChecked() {
	return resultType == PreConditionResultType.CHECKED;
    }

    public boolean isNotChecked() {
	return resultType == PreConditionResultType.NOT_CHECKED;
    }

    public boolean isGroupNotChecked() {
	return resultType == PreConditionResultType.GROUP_NOT_CHECKED;
    }

}
