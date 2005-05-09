/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaSibsPaymentType2SQLSibsPaymentTypeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof SibsPaymentType) {
            SibsPaymentType sibsPaymentType = (SibsPaymentType) source;
            return sibsPaymentType.name();
        }
        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        SibsPaymentType sibsPaymentType = null;
        if (source instanceof String) {
            String sibsPaymentTypeString = (String) source;

            sibsPaymentType = SibsPaymentType.valueOf(sibsPaymentTypeString);

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
