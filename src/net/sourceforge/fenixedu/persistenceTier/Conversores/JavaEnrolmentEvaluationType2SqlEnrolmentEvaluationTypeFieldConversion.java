/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 * 
 * Created on 21 de Novembro de 2002, 15:57
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaEnrolmentEvaluationType2SqlEnrolmentEvaluationTypeFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof EnrolmentEvaluationType) {
            EnrolmentEvaluationType s = (EnrolmentEvaluationType) source;
            return s.toString();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return EnrolmentEvaluationType.valueOf(src);
        }

        return source;

    }
}