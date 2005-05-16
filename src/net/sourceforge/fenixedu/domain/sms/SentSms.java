/*
 * Created on 7/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.domain.sms;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class SentSms extends SentSms_Base {
    protected SmsDeliveryType deliveryType;

    public SentSms() {
        super();
    }

    /**
     * @return Returns the deliveryType.
     */
    public SmsDeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * @param deliveryType
     *            The deliveryType to set.
     */
    public void setDeliveryType(SmsDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

}