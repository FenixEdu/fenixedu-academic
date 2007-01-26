package net.sourceforge.fenixedu.util.sibs.incomming;

import net.sourceforge.fenixedu.util.Money;

public class SibsIncommingPaymentFileFooter {

    private static final int[] FIELD_SIZES = new int[] { 1, 8, 17, 12, 12, 50 };

    private Money transactionsTotalAmount;

    private Money totalCost;

    private SibsIncommingPaymentFileFooter(Money transactionsTotalAmount, Money totalCost) {
	this.transactionsTotalAmount = transactionsTotalAmount;
	this.totalCost = totalCost;
    }

    public static SibsIncommingPaymentFileFooter buildFrom(String rawLine) {
	final String[] fields = splitLine(rawLine);
	return new SibsIncommingPaymentFileFooter(getTransactionsTotalAmountFrom(fields),
		getCostFrom(fields));
    }

    private static Money getCostFrom(String[] fields) {
	return new Money(fields[3].substring(0, 10) + "." + fields[3].substring(10));
    }

    private static Money getTransactionsTotalAmountFrom(String[] fields) {
	return new Money(fields[2].substring(0, 15) + "." + fields[2].substring(15));
    }

    private final static String[] splitLine(final String line) {
	int lastIndex = 0;
	final String[] result = new String[FIELD_SIZES.length];
	for (int i = 0; i < FIELD_SIZES.length; i++) {
	    result[i] = line.substring(lastIndex, lastIndex + FIELD_SIZES[i]);
	    lastIndex += FIELD_SIZES[i];
	}
	return result;
    }

    public Money getTotalCost() {
	return totalCost;
    }

    public Money getTransactionsTotalAmount() {
	return transactionsTotalAmount;
    }

}
