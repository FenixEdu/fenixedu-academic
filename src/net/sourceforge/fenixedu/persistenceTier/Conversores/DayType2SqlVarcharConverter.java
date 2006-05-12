package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class DayType2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof DayType) {
            DayType dayType = (DayType) source;
            return dayType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return DayType.valueOf((String) source);
        }
        return source;
    }

}
