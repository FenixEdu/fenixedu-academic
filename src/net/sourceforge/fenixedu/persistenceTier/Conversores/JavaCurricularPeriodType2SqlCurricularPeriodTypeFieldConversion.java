package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCurricularPeriodType2SqlCurricularPeriodTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CurricularPeriodType) {
            CurricularPeriodType s = (CurricularPeriodType) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return CurricularPeriodType.valueOf(src);
        }
        return source;
    }

}