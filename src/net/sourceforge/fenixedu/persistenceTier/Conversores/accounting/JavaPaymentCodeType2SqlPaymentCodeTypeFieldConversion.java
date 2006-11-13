/*
 * Created on Jun 22, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaPaymentCodeType2SqlPaymentCodeTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof PaymentCodeType) ? ((PaymentCodeType) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? PaymentCodeType.valueOf((String) source) : null;
    }
}
