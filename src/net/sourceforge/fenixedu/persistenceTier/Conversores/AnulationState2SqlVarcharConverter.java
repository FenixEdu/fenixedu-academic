package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AnulationState2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof AnulationState) {
            AnulationState assiduousnessRecordState = (AnulationState) source;
            return assiduousnessRecordState.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return AnulationState.valueOf((String) source);
        }
        return source;
    }

}
