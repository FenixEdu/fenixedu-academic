/*
 * Created on 7/Jun/2004
 *  
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.SmsDeliveryType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class JavaSmsDeliveryType2SqlSmsDeliveryTypeFieldConversion implements
        FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof SmsDeliveryType) {
            SmsDeliveryType smsDeliveryType = (SmsDeliveryType) source;
            return new Integer(smsDeliveryType.getValue());
        }

        return source;

    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return SmsDeliveryType.getEnum(src.intValue());
        }

        return source;

    }

}