package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JustificationGroup2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof JustificationGroup) {
            JustificationGroup justificationGroup = (JustificationGroup) source;
            return justificationGroup.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return JustificationGroup.valueOf((String) source);
        }
        return source;
    }

}
