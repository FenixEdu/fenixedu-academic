package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class JavaStudentStatutesType2SqlStudentStatutesTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof StudentStatuteType) ? ((StudentStatuteType) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? StudentStatuteType.valueOf((String) source) : null;
    }
}
