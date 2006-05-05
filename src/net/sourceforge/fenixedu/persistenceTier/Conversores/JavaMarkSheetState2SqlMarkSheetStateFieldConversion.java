package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.MarkSheetState;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaMarkSheetState2SqlMarkSheetStateFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof MarkSheetState) {
            MarkSheetState mss = (MarkSheetState) source;
            return mss.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return MarkSheetState.valueOf(src);
        }
        return source;
    }

}