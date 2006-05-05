package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.MarkSheetType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaMarkSheetType2SqlMarkSheetTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof MarkSheetType) {
            MarkSheetType mst = (MarkSheetType) source;
            return mst.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return MarkSheetType.valueOf(src);
        }
        return source;
    }

}