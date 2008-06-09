package net.sourceforge.fenixedu.persistenceTier.Conversores;


import net.sourceforge.fenixedu.domain.util.EmailAddressList;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class EmailAddressList2SqlEmailAddressListConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return source instanceof EmailAddressList ? ((EmailAddressList) source).toString() : source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return source instanceof String ? new EmailAddressList((String) source) : null;
    }

}
