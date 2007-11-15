/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AcademicInterval2SqlAcademicIntervalConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof AcademicInterval) {
	    AcademicInterval academicInterval = (AcademicInterval) source;	    
	    return academicInterval.getEntryClassName() + ":" + academicInterval.getEntryIdInternal();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {	    	    
	    String src = (String) source;	    
	    String[] split = src.split(":");	  
	    String entryClassName = split[0];
	    Integer entryIdInternal = Integer.valueOf(split[1]);
	    Integer academicCalendarIdInternal = Integer.valueOf(split[2]);
	    return new AcademicInterval(entryIdInternal, entryClassName, academicCalendarIdInternal);	    	   
	}
	return source;
    }
}
