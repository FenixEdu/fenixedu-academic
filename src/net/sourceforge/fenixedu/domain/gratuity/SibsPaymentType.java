package net.sourceforge.fenixedu.domain.gratuity;

import net.sourceforge.fenixedu.domain.transactions.TransactionType;

public enum SibsPaymentType {

    SPECIALIZATION_GRATUTITY_TOTAL(TransactionType.GRATUITY_FULL_PAYMENT, 30),

    SPECIALIZATION_GRATUTITY_FIRST_PHASE(TransactionType.GRATUITY_FIRST_PHASE_PAYMENT, 31),

    SPECIALIZATION_GRATUTITY_SECOND_PHASE(TransactionType.GRATUITY_SECOND_PHASE_PAYMENT, 32),

    MASTER_DEGREE_GRATUTITY_TOTAL(TransactionType.GRATUITY_FULL_PAYMENT, 40),

    MASTER_DEGREE_GRATUTITY_FIRST_PHASE(TransactionType.GRATUITY_FIRST_PHASE_PAYMENT, 41),

    MASTER_DEGREE_GRATUTITY_SECOND_PHASE(TransactionType.GRATUITY_SECOND_PHASE_PAYMENT, 42),

    INSURANCE(TransactionType.INSURANCE_PAYMENT, 60),

    MICROPAYMENTS_CREDIT(TransactionType.MICROPAYMENTS_CREDIT, 61);

    private TransactionType transactionType;

    private int code;

    private SibsPaymentType(TransactionType transactionType, int code) {
	this.transactionType = transactionType;
	this.code = code;
    }

    public String getName() {
	return name();
    }

    public TransactionType getTransactionType() {
	return transactionType;
    }

    public int getCode() {
	return code;
    }

    public static SibsPaymentType fromCode(int sibsPaymentCode) {
	for (SibsPaymentType type : values()) {
	    if (type.getCode() == sibsPaymentCode) {
		return type;
	    }
	}
	return null;
    }
}
