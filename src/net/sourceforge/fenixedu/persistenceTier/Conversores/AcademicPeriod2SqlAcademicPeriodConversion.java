package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AcademicPeriod2SqlAcademicPeriodConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof AcademicPeriod) {
	    AcademicPeriod academicPeriod = (AcademicPeriod) source;
	    return academicPeriod.getRepresentationInStringFormat();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {
	    return AcademicPeriod.getAcademicPeriodFromString((String) source);
	}
	return source;
    }

}
