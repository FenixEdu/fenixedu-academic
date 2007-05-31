package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.student.MobilityProgram;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

@SuppressWarnings("serial")
public class JavaMobilityProgram2SqlMobilityProgramFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof MobilityProgram) ? ((MobilityProgram) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? MobilityProgram.valueOf((String) source) : null;
    }

}
