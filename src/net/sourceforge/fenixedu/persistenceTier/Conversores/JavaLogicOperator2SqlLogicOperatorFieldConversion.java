package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.util.LogicOperator;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaLogicOperator2SqlLogicOperatorFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof LogicOperator) {
            LogicOperator crt = (LogicOperator) source;
            return crt.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return LogicOperator.valueOf(src);
        }
        return source;
    }
}