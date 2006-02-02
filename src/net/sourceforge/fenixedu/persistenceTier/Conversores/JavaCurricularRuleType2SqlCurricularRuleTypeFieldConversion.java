package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCurricularRuleType2SqlCurricularRuleTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CurricularRuleType) {
            CurricularRuleType crt = (CurricularRuleType) source;
            return crt.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return CurricularRuleType.valueOf(src);
        }
        return source;
    }

}