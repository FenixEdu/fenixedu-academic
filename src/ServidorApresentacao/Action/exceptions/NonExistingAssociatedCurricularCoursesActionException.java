package ServidorApresentacao.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NonExistingAssociatedCurricularCoursesActionException extends FenixActionException {

    public static String key = "error.nonExisting.AssociatedCurricularCourses";

    public NonExistingAssociatedCurricularCoursesActionException() {
        super(key);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Throwable cause) {
        super(key, cause);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Object[] values, Throwable cause) {
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
        NonExistingAssociatedCurricularCoursesActionException.key = key;
    }

    public String toString() {
        String result = "[NonExistingAssociatedCurricularCoursesActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}