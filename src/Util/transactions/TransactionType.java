package Util.transactions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Util.FenixValuedEnum;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class TransactionType extends FenixValuedEnum {

    public static final int FULL_PAYMENT_TYPE = 1;

    public static final int FIRST_PHASE_PAYMENT_TYPE = 2;

    public static final int SECOND_PHASE_PAYMENT_TYPE = 3;

    public static final int THIRD_PHASE_PAYMENT_TYPE = 4;

    public static final int FOURTH_PHASE_PAYMENT_TYPE = 5;

    public static final int FIFTH_PHASE_PAYMENT_TYPE = 6;

    public static final int ADHOC_PAYMENT_TYPE = 7;

    public static final int USER_SENT_SMS_PAYMENT_TYPE = 8;

    public static final int SYSTEM_SENT_SMS_TO_USER_PAYMENT_TYPE = 9;

    public static final TransactionType FULL_PAYMENT = new TransactionType(
            "fullPayment", TransactionType.FULL_PAYMENT_TYPE);

    public static final TransactionType FIRST_PHASE_PAYMENT = new TransactionType(
            "firstPhasePayment", TransactionType.FIRST_PHASE_PAYMENT_TYPE);

    public static final TransactionType SECOND_PHASE_PAYMENT = new TransactionType(
            "secondPhasePayment", TransactionType.SECOND_PHASE_PAYMENT_TYPE);

    public static final TransactionType THIRD_PHASE_PAYMENT = new TransactionType(
            "thirdPhasePayment", TransactionType.THIRD_PHASE_PAYMENT_TYPE);

    public static final TransactionType FOURTH_PHASE_PAYMENT = new TransactionType(
            "fourthPhasePayment", TransactionType.FOURTH_PHASE_PAYMENT_TYPE);

    public static final TransactionType FIFTH_PHASE_PAYMENT = new TransactionType(
            "fifthPhasePayment", TransactionType.FIFTH_PHASE_PAYMENT_TYPE);

    public static final TransactionType ADHOC_PAYMENT = new TransactionType(
            "adhocPayment", TransactionType.ADHOC_PAYMENT_TYPE);

    public static final TransactionType USER_SENT_SMS_PAYMENT = new TransactionType(
            "userSentSmsPayment", TransactionType.USER_SENT_SMS_PAYMENT_TYPE);

    public static final TransactionType SYSTEM_SENT_SMS_TO_USER_PAYMENT = new TransactionType(
            "systemSentSmsToUserPayment", TransactionType.SYSTEM_SENT_SMS_TO_USER_PAYMENT_TYPE);

    public TransactionType(String name, int value) {
        super(name, value);
    }

    public static TransactionType getEnum(String SmsTransactionType) {
        return (TransactionType) getEnum(TransactionType.class, SmsTransactionType);
    }

    public static TransactionType getEnum(int SmsTransactionType) {
        return (TransactionType) getEnum(TransactionType.class, SmsTransactionType);
    }

    public static Map getEnumMap() {
        return getEnumMap(TransactionType.class);
    }

    public static List getEnumList() {
        return getEnumList(TransactionType.class);
    }

    public static Iterator iterator() {
        return iterator(TransactionType.class);
    }

    public String toString() {
        String smsTransactionTypeString = "\nSms Transaction Type String : " + this.getName();
        smsTransactionTypeString += "\nSms Transaction Type String(value): " + this.getValue();
        return smsTransactionTypeString;
    }

}