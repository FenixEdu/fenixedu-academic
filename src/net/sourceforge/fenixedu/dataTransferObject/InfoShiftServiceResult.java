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
    public static final int SUCESS = 0;

    public static final int THEORETICAL_HOURS_LIMIT_EXCEEDED = 1;

    public static final int THEORETICAL_HOURS_LIMIT_REACHED = 2;

    public static final int PRATICAL_HOURS_LIMIT_EXCEEDED = 3;

    public static final int PRATICAL_HOURS_LIMIT_REACHED = 4;

    public static final int THEO_PRAT_HOURS_LIMIT_EXCEEDED = 5;

    public static final int THEO_PRAT_HOURS_LIMIT_REACHED = 6;

    public static final int LAB_HOURS_LIMIT_EXCEEDED = 7;

    public static final int LAB_HOURS_LIMIT_REACHED = 8;

    private int _messageType;

    public InfoShiftServiceResult() {
    }

    public InfoShiftServiceResult(int messageType) {
        switch (messageType) {
        case SUCESS:
        case THEORETICAL_HOURS_LIMIT_EXCEEDED:
        case PRATICAL_HOURS_LIMIT_EXCEEDED:
        case THEO_PRAT_HOURS_LIMIT_EXCEEDED:
        case LAB_HOURS_LIMIT_EXCEEDED:
        case THEORETICAL_HOURS_LIMIT_REACHED:
        case PRATICAL_HOURS_LIMIT_REACHED:
        case THEO_PRAT_HOURS_LIMIT_REACHED:
        case LAB_HOURS_LIMIT_REACHED:
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
        return (_messageType == SUCESS);
    }

    public String toString() {
        switch (this._messageType) {
        case SUCESS:
            return "SUCESS";
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
        default:
            return "UNKNOWN";
        }
    }

}