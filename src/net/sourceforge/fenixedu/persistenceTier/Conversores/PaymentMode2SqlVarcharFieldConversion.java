/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PaymentMode2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof PaymentMode) ? ((PaymentMode) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? PaymentMode.valueOf((String) source) : null;
    }

}
