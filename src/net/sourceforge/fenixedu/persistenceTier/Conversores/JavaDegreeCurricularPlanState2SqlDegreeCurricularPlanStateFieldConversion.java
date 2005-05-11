package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class JavaDegreeCurricularPlanState2SqlDegreeCurricularPlanStateFieldConversion implements
        FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof DegreeCurricularPlanState) {
            DegreeCurricularPlanState s = (DegreeCurricularPlanState) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return DegreeCurricularPlanState.valueOf(src);
        }
        return source;

    }

}