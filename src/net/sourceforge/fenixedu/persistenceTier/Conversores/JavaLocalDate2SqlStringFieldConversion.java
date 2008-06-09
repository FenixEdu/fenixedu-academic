package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.date.SerializationTool;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.LocalDate;

public class JavaLocalDate2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(Object object) {
	return SerializationTool.localDateSerialize((LocalDate) object);
    }

    public Object sqlToJava(Object object) {
	return SerializationTool.localDateDeserialize((String) object);
    }

}