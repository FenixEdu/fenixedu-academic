/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.gratuity.SibsPaymentType;

public class JavaSibsPaymentType2SQLSibsPaymentTypeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof SibsPaymentType) {
            SibsPaymentType sibsPaymentType = (SibsPaymentType) source;
            return new Integer(sibsPaymentType.getValue());
        }
        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        SibsPaymentType sibsPaymentType = null;
        if (source instanceof Integer) {
            Integer sibsPaymentTypeID = (Integer) source;

            sibsPaymentType = SibsPaymentType.getEnum(sibsPaymentTypeID.intValue());

            if (sibsPaymentType == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal SibsPaymentType!(" + source + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal SibsPaymentType!(" + source + ")");
        }
        return sibsPaymentType;
    }
}

