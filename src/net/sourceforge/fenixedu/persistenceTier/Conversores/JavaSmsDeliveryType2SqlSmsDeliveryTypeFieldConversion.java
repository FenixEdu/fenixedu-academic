/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.sms.SmsDeliveryType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class JavaSmsDeliveryType2SqlSmsDeliveryTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof SmsDeliveryType) {
            SmsDeliveryType smsDeliveryType = (SmsDeliveryType) source;
            return smsDeliveryType.toString();
        }

        return source;

    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {
            String src = (String) source;
            return SmsDeliveryType.valueOf(src);
        }

        return source;

    }

}