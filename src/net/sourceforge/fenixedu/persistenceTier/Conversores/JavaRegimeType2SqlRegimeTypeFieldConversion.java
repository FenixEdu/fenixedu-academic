package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaRegimeType2SqlRegimeTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof RegimeType) {
            RegimeType s = (RegimeType) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return RegimeType.valueOf(src);
        }
        return source;
    }

}