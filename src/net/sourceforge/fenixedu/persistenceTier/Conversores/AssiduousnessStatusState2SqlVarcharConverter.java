package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AssiduousnessStatusState2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof AssiduousnessState) {
            AssiduousnessState assiduousnessState = (AssiduousnessState) source;
            return assiduousnessState.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return AssiduousnessState.valueOf((String) source);
        }
        return source;
    }

}
