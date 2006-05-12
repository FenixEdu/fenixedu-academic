package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JustificationType2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof JustificationType) {
            JustificationType justificationType = (JustificationType) source;
            return justificationType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return JustificationType.valueOf((String) source);
        }
        return source;
    }

}
