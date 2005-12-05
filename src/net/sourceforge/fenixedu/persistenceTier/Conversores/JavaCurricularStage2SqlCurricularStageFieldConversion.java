package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCurricularStage2SqlCurricularStageFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CurricularStage) {
            CurricularStage s = (CurricularStage) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return CurricularStage.valueOf(src);
        }
        return source;

    }

}