/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.StudentCurricularPlanState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof StudentCurricularPlanState) {
            StudentCurricularPlanState s = (StudentCurricularPlanState) source;
            return s.getState();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new StudentCurricularPlanState(src);
        }

        return source;

    }
}