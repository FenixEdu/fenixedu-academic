/*
 * Created on Nov 5, 2004
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class JavaEnrollmentLogAction2SqlEnrollmentLogActionFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */

    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof EnrolmentAction) {
            EnrolmentAction enrollmentAction = (EnrolmentAction) obj;
            return new Integer(enrollmentAction.getValue());
        }
        return obj;
    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */

    public Object sqlToJava(Object obj) throws ConversionException {
        EnrolmentAction enrollmentAction = null;
        if (obj instanceof Integer) {
            Integer enrollmentStateId = (Integer) obj;

            enrollmentAction = EnrolmentAction.getEnum(enrollmentStateId.intValue());
            if (enrollmentAction == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal EnrolmentAction type!(" + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal EnrolmentAction type!(" + obj + ")");
        }
        return enrollmentAction;

    }

}
