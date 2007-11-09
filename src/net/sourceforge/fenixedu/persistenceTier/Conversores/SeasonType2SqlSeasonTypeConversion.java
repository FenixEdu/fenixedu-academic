/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.time.calendarStructure.SeasonType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class SeasonType2SqlSeasonTypeConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof SeasonType) {
	    SeasonType s = (SeasonType) source;
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
	    return SeasonType.valueOf(src);
	}
	return source;
    }
}
