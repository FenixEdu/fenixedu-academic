/*
 * Created on 7/Jun/2004
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsDeliveryType extends FenixValuedEnum {

    public static final int NOT_SENT_TYPE = 0;

    public static final int DELIVERY_SUCCESS_TYPE = 1;

    public static final int DELIVERY_FAILURE_TYPE = 2;

    public static final int MESSAGE_BUFFERED_TYPE = 4;

    public static final int SMSC_SUBMIT_TYPE = 8;

    public static final int SMSC_REJECT_TYPE = 16;

    public static final SmsDeliveryType NOT_SENT = new SmsDeliveryType("notSent",
            SmsDeliveryType.NOT_SENT_TYPE);

    public static final SmsDeliveryType DELIVERY_SUCCESS = new SmsDeliveryType("deliverySuccess",
            SmsDeliveryType.DELIVERY_SUCCESS_TYPE);

    public static final SmsDeliveryType DELIVERY_FAILURE = new SmsDeliveryType("deliveryFailure",
            SmsDeliveryType.DELIVERY_FAILURE_TYPE);

    public static final SmsDeliveryType MESSAGE_BUFFERED = new SmsDeliveryType("messageBuffered",
            SmsDeliveryType.MESSAGE_BUFFERED_TYPE);

    public static final SmsDeliveryType SMSC_SUBMIT = new SmsDeliveryType("smscSubmit",
            SmsDeliveryType.SMSC_SUBMIT_TYPE);

    public static final SmsDeliveryType SMSC_REJECT = new SmsDeliveryType("smscReject",
            SmsDeliveryType.SMSC_REJECT_TYPE);

    public SmsDeliveryType(String name, int value) {
        super(name, value);
    }

    public static SmsDeliveryType getEnum(String smsDeliveryType) {
        return (SmsDeliveryType) getEnum(SmsDeliveryType.class, smsDeliveryType);
    }

    public static SmsDeliveryType getEnum(int smsDeliveryType) {
        return (SmsDeliveryType) getEnum(SmsDeliveryType.class, smsDeliveryType);
    }

    public static Map getEnumMap() {
        return getEnumMap(SmsDeliveryType.class);
    }

    public static List getEnumList() {
        return getEnumList(SmsDeliveryType.class);
    }

    public static Iterator iterator() {
        return iterator(SmsDeliveryType.class);
    }

    public String toString() {
        String smsDeliveryString = "\nSms Delivery String : " + this.getName();
        smsDeliveryString += "\nSms Delivery String(value): " + this.getValue();
        return smsDeliveryString;
    }

}