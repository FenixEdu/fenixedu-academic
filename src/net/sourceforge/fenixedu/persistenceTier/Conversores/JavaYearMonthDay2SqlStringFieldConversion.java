package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.date.SerializationTool;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.YearMonthDay;

public class JavaYearMonthDay2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(Object object) {
	return SerializationTool.yearMonthDayDeserialize((YearMonthDay) object);
    }

    public Object sqlToJava(Object object) {
	return SerializationTool.yearMonthDaySerialize((String) object);
    }

}