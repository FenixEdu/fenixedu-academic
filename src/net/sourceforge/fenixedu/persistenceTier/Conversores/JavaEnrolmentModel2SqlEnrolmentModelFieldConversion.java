package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.student.EnrolmentModel;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class JavaEnrolmentModel2SqlEnrolmentModelFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof EnrolmentModel) ? ((EnrolmentModel) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? EnrolmentModel.valueOf((String) source) : null;
    }

}
