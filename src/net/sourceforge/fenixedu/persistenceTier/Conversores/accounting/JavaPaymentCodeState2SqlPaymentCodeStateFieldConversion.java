/*
 * Created on Jun 22, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaPaymentCodeState2SqlPaymentCodeStateFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof PaymentCodeState) ? ((PaymentCodeState) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? PaymentCodeState.valueOf((String) source) : null;
    }
}
