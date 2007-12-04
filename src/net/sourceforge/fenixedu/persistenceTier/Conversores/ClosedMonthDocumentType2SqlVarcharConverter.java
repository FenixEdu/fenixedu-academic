package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ClosedMonthDocumentType2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof ClosedMonthDocumentType) {
            ClosedMonthDocumentType closedMonthDocumentType = (ClosedMonthDocumentType) source;
            return closedMonthDocumentType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return ClosedMonthDocumentType.valueOf((String) source);
        }
        return source;
    }
}
