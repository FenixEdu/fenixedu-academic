package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.Grade;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Grade2SqlGradeStringConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof Grade) {
            return ((Grade) source).exportAsString();
        }
        
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if(source == null) {
	    return Grade.createEmptyGrade();
	}
	if(source instanceof String) {
	    return Grade.importFromString((String) source);
	}
	
	return null;
    }

}
