/*
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author asnr and scpo
 *  
 */
public class CourseType extends FenixUtil {

    public static final int NORMAL = 0;

    public static final int EXTRA_CURRICULAR = 1;

    public static final CourseType NORMAL_TYPE = new CourseType(NORMAL);

    public static final CourseType EXTRA_CURRICULAR_TYPE = new CourseType(EXTRA_CURRICULAR);

    public static final String NORMAL_STRING = "Curricular";

    public static final String EXTRA_CURRICULAR_STRING = "Extra Curricular";

    private Integer type;

    /** Creates a new instance of EnrolmentCourseType */
    public CourseType() {
    }

    public CourseType(int type) {
        this.type = new Integer(type);
    }

    public CourseType(Integer type) {
        this.type = type;
    }

    /**
     * Getter for property type.
     * 
     * @return Value of property type.
     *  
     */

    public java.lang.Integer getType() {
        return type;
    }

    /**
     * Setter for property type.
     * 
     * @param type
     *            New value of property type.
     *  
     */

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o instanceof CourseType) {
            CourseType aux = (CourseType) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    public String toString() {

        int value = this.type.intValue();
        String stringValue = null;

        switch (value) {
        case NORMAL:
            stringValue = "NORMAL";
            break;
        case EXTRA_CURRICULAR:
            stringValue = "EXTRA CURRICULAR";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + stringValue + "]";
    }
}