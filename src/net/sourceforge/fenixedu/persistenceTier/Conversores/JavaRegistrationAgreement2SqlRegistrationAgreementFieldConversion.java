package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class JavaRegistrationAgreement2SqlRegistrationAgreementFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof RegistrationAgreement) ? ((RegistrationAgreement) source).name()
		: null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? RegistrationAgreement.valueOf((String) source) : null;
    }
}
