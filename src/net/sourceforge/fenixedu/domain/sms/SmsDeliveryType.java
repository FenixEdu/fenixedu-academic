package net.sourceforge.fenixedu.domain.sms;

public enum SmsDeliveryType {

    NOT_SENT_TYPE,

    DELIVERY_SUCCESS_TYPE,

    DELIVERY_FAILURE_TYPE,

    MESSAGE_BUFFERED_TYPE,

    SMSC_SUBMIT_TYPE,

    SMSC_REJECT_TYPE

}