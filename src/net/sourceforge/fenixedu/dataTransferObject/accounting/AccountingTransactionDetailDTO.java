package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

import org.joda.time.DateTime;

public class AccountingTransactionDetailDTO implements Serializable {

    private DateTime whenRegistered;

    private PaymentMode paymentMode;

    private String comments;

    public AccountingTransactionDetailDTO(DateTime whenRegistered, PaymentMode paymentMode) {
	this(whenRegistered, paymentMode, null);

    }

    public AccountingTransactionDetailDTO(DateTime whenRegistered, PaymentMode paymentMode,
	    String comments) {
	this.comments = comments;
	this.paymentMode = paymentMode;
	this.whenRegistered = whenRegistered;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public PaymentMode getPaymentMode() {
	return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
	this.paymentMode = paymentMode;
    }

    public DateTime getWhenRegistered() {
	return whenRegistered;
    }

    public void setWhenRegistered(DateTime whenRegistered) {
	this.whenRegistered = whenRegistered;
    }

}
