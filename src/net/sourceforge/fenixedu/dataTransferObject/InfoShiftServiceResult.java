/**
 * Project Sop 
 * 
 * Package DataBeans
 * 
 * Created on 16/Jan/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 * 
 * 
 */
public final class InfoShiftServiceResult extends InfoObject {
    public static final int SUCCESS = 0;

    public static final int THEORETICAL_HOURS_LIMIT_EXCEEDED = 1;

    public static final int THEORETICAL_HOURS_LIMIT_REACHED = 2;

    public static final int PRATICAL_HOURS_LIMIT_EXCEEDED = 3;

    public static final int PRATICAL_HOURS_LIMIT_REACHED = 4;

    public static final int THEO_PRAT_HOURS_LIMIT_EXCEEDED = 5;

    public static final int THEO_PRAT_HOURS_LIMIT_REACHED = 6;

    public static final int LAB_HOURS_LIMIT_EXCEEDED = 7;

    public static final int LAB_HOURS_LIMIT_REACHED = 8;

    public static final int SEMINARY_LIMIT_EXCEEDED = 9;

    public static final int SEMINARY_LIMIT_REACHED = 10;

    public static final int PROBLEMS_LIMIT_EXCEEDED = 11;

    public static final int PROBLEMS_LIMIT_REACHED = 12;

    public static final int FIELD_WORK_LIMIT_EXCEEDED = 13;

    public static final int FIELD_WORK_LIMIT_REACHED = 14;

    public static final int TRAINING_PERIOD_LIMIT_EXCEEDED = 15;

    public static final int TRAINING_PERIOD_LIMIT_REACHED = 16;

    public static final int TUTORIAL_ORIENTATION_LIMIT_EXCEEDED = 17;

    public static final int TUTORIAL_ORIENTATION_LIMIT_REACHED = 18;

    private int _messageType;

    public InfoShiftServiceResult() {
    }

    public InfoShiftServiceResult(int messageType) {
	switch (messageType) {
	case SUCCESS:
	case THEORETICAL_HOURS_LIMIT_EXCEEDED:
	case PRATICAL_HOURS_LIMIT_EXCEEDED:
	case THEO_PRAT_HOURS_LIMIT_EXCEEDED:
	case LAB_HOURS_LIMIT_EXCEEDED:
	case THEORETICAL_HOURS_LIMIT_REACHED:
	case PRATICAL_HOURS_LIMIT_REACHED:
	case THEO_PRAT_HOURS_LIMIT_REACHED:
	case LAB_HOURS_LIMIT_REACHED:
	case SEMINARY_LIMIT_EXCEEDED:
	case SEMINARY_LIMIT_REACHED:
	case PROBLEMS_LIMIT_EXCEEDED:
	case PROBLEMS_LIMIT_REACHED:
	case FIELD_WORK_LIMIT_EXCEEDED:
	case FIELD_WORK_LIMIT_REACHED:
	case TRAINING_PERIOD_LIMIT_EXCEEDED:
	case TRAINING_PERIOD_LIMIT_REACHED:
	case TUTORIAL_ORIENTATION_LIMIT_EXCEEDED:
	case TUTORIAL_ORIENTATION_LIMIT_REACHED:
	    _messageType = messageType;
	    break;
	default:
	    throw new IllegalArgumentException("Message type not recognized!");
	}
    }

    public int getMessageType() {
	return _messageType;
    }

    public void setMessageType(int messageType) {
	_messageType = messageType;
    }

    public boolean isSUCESS() {
	return (_messageType == SUCCESS);
    }

    public String toString() {
	switch (this._messageType) {
	case SUCCESS:
	    return "SUCCESS";
	case THEORETICAL_HOURS_LIMIT_EXCEEDED:
	    return "THEORETICAL_HOURS_LIMIT_EXCEEDED";
	case PRATICAL_HOURS_LIMIT_EXCEEDED:
	    return "PRATICAL_HOURS_LIMIT_EXCEEDED";
	case THEO_PRAT_HOURS_LIMIT_EXCEEDED:
	    return "THEO_PRAT_HOURS_LIMIT_EXCEEDED";
	case LAB_HOURS_LIMIT_EXCEEDED:
	    return "LAB_HOURS_LIMIT_EXCEEDED";
	case THEORETICAL_HOURS_LIMIT_REACHED:
	    return "THEORETICAL_HOURS_LIMIT_REACHED";
	case PRATICAL_HOURS_LIMIT_REACHED:
	    return "PRATICAL_HOURS_LIMIT_REACHED";
	case THEO_PRAT_HOURS_LIMIT_REACHED:
	    return "THEO_PRAT_HOURS_LIMIT_REACHED";
	case LAB_HOURS_LIMIT_REACHED:
	    return "LAB_HOURS_LIMIT_REACHED";
	case SEMINARY_LIMIT_EXCEEDED:
	    return "SEMINARY_LIMIT_EXCEEDED";
	case SEMINARY_LIMIT_REACHED:
	    return "SEMINARY_LIMIT_REACHED";
	case PROBLEMS_LIMIT_EXCEEDED:
	    return "PROBLEMS_LIMIT_EXCEEDED";
	case PROBLEMS_LIMIT_REACHED:
	    return "PROBLEMS_LIMIT_REACHED";
	case FIELD_WORK_LIMIT_EXCEEDED:
	    return "FIELD_WORK_LIMIT_EXCEEDED";
	case FIELD_WORK_LIMIT_REACHED:
	    return "FIELD_WORK_LIMIT_REACHED";
	case TRAINING_PERIOD_LIMIT_EXCEEDED:
	    return "TRAINING_PERIOD_LIMIT_EXCEEDED";
	case TRAINING_PERIOD_LIMIT_REACHED:
	    return "TRAINING_PERIOD_LIMIT_REACHED";
	case TUTORIAL_ORIENTATION_LIMIT_EXCEEDED:
	    return "TUTORIAL_ORIENTATION_LIMIT_EXCEEDED";
	case TUTORIAL_ORIENTATION_LIMIT_REACHED:
	    return "TUTORIAL_ORIENTATION_LIMIT_REACHED";
	default:
	    return "UNKNOWN";
	}
    }

}