/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class SecretaryEnrolmentStudentReason extends FenixValuedEnum {

    public static final int GENERIC_TYPE = 1;
    
    public static final int LEIC_OLD_TYPE = 2;
    
    public static final SecretaryEnrolmentStudentReason GENERIC = new SecretaryEnrolmentStudentReason(
            "generic.secretary.enrolment.student.reason", SecretaryEnrolmentStudentReason.GENERIC_TYPE);

    public static final SecretaryEnrolmentStudentReason LEIC = new SecretaryEnrolmentStudentReason(
            "leic.old.secretary.enrolment.student.reason", SecretaryEnrolmentStudentReason.LEIC_OLD_TYPE);

    
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
        String result = "Secretary Enrolment Registration Reason:\n";
        result += "\n  - Secretary Enrolment Registration Reason : " + this.getName();

        return result;
    }

}
