package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class JavaYearMonth2SqlStringFieldConversion implements FieldConversion {

    private static final DateTimeFieldType [] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};

    public Object javaToSql(Object object) {
        if (object != null) {
            Partial yearMonth = (Partial) object;
            
            return String.format("%d-%02d", yearMonth.get(DateTimeFieldType.year()), yearMonth.get(DateTimeFieldType.monthOfYear()));
        }
        return null;
    }

    public Object sqlToJava(Object object) {
        if (object != null && object instanceof String) {
            final String string = (String) object;
            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));

            int [] values = { year, month };
            return new Partial(types, values); 
        }
        return object;
    }

}