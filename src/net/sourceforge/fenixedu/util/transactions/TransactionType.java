package net.sourceforge.fenixedu.util.transactions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.util.FenixValuedEnum;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class TransactionType extends FenixValuedEnum {

    public static final int GRATUITY_FULL_PAYMENT_TYPE = 1;

    public static final int GRATUITY_FIRST_PHASE_PAYMENT_TYPE = 2;

    public static final int GRATUITY_SECOND_PHASE_PAYMENT_TYPE = 3;

    public static final int GRATUITY_THIRD_PHASE_PAYMENT_TYPE = 4;

    public static final int GRATUITY_FOURTH_PHASE_PAYMENT_TYPE = 5;

    public static final int GRATUITY_FIFTH_PHASE_PAYMENT_TYPE = 6;

    public static final int GRATUITY_ADHOC_PAYMENT_TYPE = 7;

    public static final int USER_SENT_SMS_PAYMENT_TYPE = 8;

    public static final int SYSTEM_SENT_SMS_TO_USER_PAYMENT_TYPE = 9;

    public static final int GRATUITY_REIMBURSEMENT_TYPE = 10;

    public static final int INSURANCE_PAYMENT_TYPE = 11;

    public static final int INSURANCE_REIMBURSEMENT_TYPE = 12;

    public static final TransactionType GRATUITY_FULL_PAYMENT = new TransactionType(
            "gratuityFullPayment", TransactionType.GRATUITY_FULL_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_FIRST_PHASE_PAYMENT = new TransactionType(
            "gratuityFirstPhasePayment", TransactionType.GRATUITY_FIRST_PHASE_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_SECOND_PHASE_PAYMENT = new TransactionType(
            "gratuitySecondPhasePayment", TransactionType.GRATUITY_SECOND_PHASE_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_THIRD_PHASE_PAYMENT = new TransactionType(
            "gratuityThirdPhasePayment", TransactionType.GRATUITY_THIRD_PHASE_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_FOURTH_PHASE_PAYMENT = new TransactionType(
            "gratuityFourthPhasePayment", TransactionType.GRATUITY_FOURTH_PHASE_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_FIFTH_PHASE_PAYMENT = new TransactionType(
            "gratuityFifthPhasePayment", TransactionType.GRATUITY_FIFTH_PHASE_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_ADHOC_PAYMENT = new TransactionType(
            "gratuityAdhocPayment", TransactionType.GRATUITY_ADHOC_PAYMENT_TYPE);

    public static final TransactionType USER_SENT_SMS_PAYMENT = new TransactionType(
            "userSentSmsPayment", TransactionType.USER_SENT_SMS_PAYMENT_TYPE);

    public static final TransactionType SYSTEM_SENT_SMS_TO_USER_PAYMENT = new TransactionType(
            "systemSentSmsToUserPayment", TransactionType.SYSTEM_SENT_SMS_TO_USER_PAYMENT_TYPE);

    public static final TransactionType GRATUITY_REIMBURSEMENT = new TransactionType(
            "gratuityReimbursement", TransactionType.GRATUITY_REIMBURSEMENT_TYPE);

    public static final TransactionType INSURANCE_PAYMENT = new TransactionType("insurancePaymentType",
            TransactionType.INSURANCE_PAYMENT_TYPE);

    public static final TransactionType INSURANCE_REIMBURSEMENT = new TransactionType(
            "insuranceReimbursementType", TransactionType.INSURANCE_REIMBURSEMENT_TYPE);

    public TransactionType(String name, int value) {
        super(name, value);
    }

    public static TransactionType getEnum(String transactionType) {
        return (TransactionType) getEnum(TransactionType.class, transactionType);
    }

    public static TransactionType getEnum(int transactionType) {
        return (TransactionType) getEnum(TransactionType.class, transactionType);
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
        String transactionTypeString = "\nTransaction Type String : " + this.getName();
        transactionTypeString += "\nTransaction Type String(value): " + this.getValue();
        return transactionTypeString;
    }

}