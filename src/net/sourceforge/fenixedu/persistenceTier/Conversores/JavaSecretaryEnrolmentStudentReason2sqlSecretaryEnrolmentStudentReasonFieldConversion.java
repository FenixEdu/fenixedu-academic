/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class JavaSecretaryEnrolmentStudentReason2sqlSecretaryEnrolmentStudentReasonFieldConversion
        implements FieldConversion {


    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof SecretaryEnrolmentStudentReason) {
            SecretaryEnrolmentStudentReason s = (SecretaryEnrolmentStudentReason) source;
            return new Integer(s.getValue());
        }

        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return SecretaryEnrolmentStudentReason.getEnum(src.intValue());
        }
        return source;
    }

}
