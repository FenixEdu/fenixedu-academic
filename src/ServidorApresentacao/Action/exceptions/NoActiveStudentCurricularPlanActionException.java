package ServidorApresentacao.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NoActiveStudentCurricularPlanActionException extends FenixActionException {

    public static String key = "error.exception.noActiveStudentCurricularPlan";

    public NoActiveStudentCurricularPlanActionException(Throwable cause) {
        super(key, cause);
    }

    public NoActiveStudentCurricularPlanActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NoActiveStudentCurricularPlanActionException(String key, Throwable cause) {
        super(key, cause);
    }

    public NoActiveStudentCurricularPlanActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @return String
     */
    public static String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            The key to set
     */
    public static void setKey(String key) {
        NoActiveStudentCurricularPlanActionException.key = key;
    }

    public String toString() {
        String result = "[NoActiveStudentCurricularPlanActionException\n";
        result += "property [" + this.getProperty() + "]\n";
        result += "error [" + this.getError() + "]\n";
        result += "cause [" + this.getCause() + "]\n";
        result += "]";
        return result;
    }
    //TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}