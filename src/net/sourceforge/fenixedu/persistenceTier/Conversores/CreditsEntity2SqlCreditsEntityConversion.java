package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.time.calendarStructure.CreditsEntity;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class CreditsEntity2SqlCreditsEntityConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof CreditsEntity) {
	    CreditsEntity s = (CreditsEntity) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {
	    String src = (String) source;
	    return CreditsEntity.valueOf(src);
	}
	return source;
    }
}
