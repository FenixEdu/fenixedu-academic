package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.util.LogicOperators;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaLogicOperators2SqlLogicOperatorsFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof LogicOperators) {
            LogicOperators crt = (LogicOperators) source;
            return crt.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return LogicOperators.valueOf(src);
        }
        return source;
    }
}