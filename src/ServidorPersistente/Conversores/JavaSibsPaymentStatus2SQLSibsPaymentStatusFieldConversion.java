/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.gratuity.SibsPaymentStatus;

public class JavaSibsPaymentStatus2SQLSibsPaymentStatusFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof SibsPaymentStatus) {
            SibsPaymentStatus sibsPaymentStatusType = (SibsPaymentStatus) source;
            return new Integer(sibsPaymentStatusType.getValue());
        }
        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        SibsPaymentStatus sibsPaymentStatusType = null;
        if (source instanceof Integer) {
            Integer sibsPaymentStatusTypeID = (Integer) source;

            sibsPaymentStatusType = SibsPaymentStatus.getEnum(sibsPaymentStatusTypeID.intValue());

            if (sibsPaymentStatusType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal SibsPaymentStatusType!(" + source + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal SibsPaymentStatusType!(" + source + ")");
        }
        return sibsPaymentStatusType;
    }
}

