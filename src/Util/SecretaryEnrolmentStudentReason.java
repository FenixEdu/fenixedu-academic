/*
 * Created on Feb 3, 2005
 *
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class SecretaryEnrolmentStudentReason extends FenixValuedEnum {

    public static final int LEIC_5TH_YEAR_TYPE = 1;

    public static final int GAY_TYPE = 2;
    
    public static final SecretaryEnrolmentStudentReason LEIC_5TH_YEAR = new SecretaryEnrolmentStudentReason(
            "leic.5th.year", SecretaryEnrolmentStudentReason.LEIC_5TH_YEAR_TYPE);

    public static final SecretaryEnrolmentStudentReason GAY = new SecretaryEnrolmentStudentReason("gay",
            SecretaryEnrolmentStudentReason.GAY_TYPE);

    public SecretaryEnrolmentStudentReason(String name, int value) {
        super(name, value);
    }

    public static SecretaryEnrolmentStudentReason getEnum(String secretaryEnrolmentStudentReason) {
        return (SecretaryEnrolmentStudentReason) getEnum(SecretaryEnrolmentStudentReason.class,
                secretaryEnrolmentStudentReason);
    }

    public static SecretaryEnrolmentStudentReason getEnum(int secretaryEnrolmentStudentReason) {
        return (SecretaryEnrolmentStudentReason) getEnum(SecretaryEnrolmentStudentReason.class,
                secretaryEnrolmentStudentReason);
    }

    public static Map getEnumMap() {
        return getEnumMap(SecretaryEnrolmentStudentReason.class);
    }

    public static List getEnumList() {
        return getEnumList(SecretaryEnrolmentStudentReason.class);
    }

    public static Iterator iterator() {
        return iterator(SecretaryEnrolmentStudentReason.class);
    }

    public String toString() {
        String result = "Secretary Enrolment Student Reason:\n";
        result += "\n  - Secretary Enrolment Student Reason : " + this.getName();

        return result;
    }

}
