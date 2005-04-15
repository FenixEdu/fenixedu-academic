/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaSibsPaymentStatus2SQLSibsPaymentStatusFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof SibsPaymentStatus) {
            SibsPaymentStatus sibsPaymentStatusType = (SibsPaymentStatus) source;
            return sibsPaymentStatusType.toString();
        }
        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String sibsPaymentStatusTypeString = (String) source;
            return SibsPaymentStatus.valueOf(sibsPaymentStatusTypeString);
        }
        return source;
    }

}

