/**
 * Project Sop 
 * 
 * Package DataBeans
 * 
 * Created on 19/Dez/2003
 *
 */
package DataBeans;

/**
 * @author jpvl
 * 
 *  
 */
public final class InfoEnrolmentServiceResult extends InfoObject {
    public static final int NOT_ENROLED_INTO_EXECUTION_COURSE = 0;

    public static final int SHIFT_FULL = 1;

    public static final int ENROLMENT_SUCESS = 2;

    public static final int NON_EXISTING_STUDENT = 3;

    public static final int NON_EXISTING_SHIFT = 4;

    private int _messageType;

    public InfoEnrolmentServiceResult() {
    }

    public InfoEnrolmentServiceResult(int messageType) {
        switch (messageType) {
        case NOT_ENROLED_INTO_EXECUTION_COURSE:
        case SHIFT_FULL:
        case ENROLMENT_SUCESS:
        case NON_EXISTING_STUDENT:
        case NON_EXISTING_SHIFT:
            _messageType = messageType;
            break;
        default:
            throw new IllegalArgumentException("Message type not recognized!");
        }
    }

    /**
     * Returns the messageType.
     * 
     * @return int
     */
    public int getMessageType() {
        return _messageType;
    }

    /**
     * Sets the messageType.
     * 
     * @param messageType
     *            The messageType to set
     */
    public void setMessageType(int messageType) {
        _messageType = messageType;
    }

}