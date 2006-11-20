package net.sourceforge.fenixedu.dataTransferObject.accounting;

import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

import org.joda.time.DateTime;

public class SibsTransactionDetailDTO extends AccountingTransactionDetailDTO {

    private String sibsTransactionId;

    private String sibsCode;

    public SibsTransactionDetailDTO(DateTime whenRegistered, String sibsTransactionId, String sibsCode) {
	this(null, whenRegistered, sibsTransactionId, sibsCode);
    }

    public SibsTransactionDetailDTO(String comments, DateTime whenRegistered, String sibsTransactionId,
	    String sibsCode) {
	super(comments, whenRegistered, PaymentMode.ATM);
	this.sibsTransactionId = sibsTransactionId;
	this.sibsCode = sibsCode;

    }

    public String getSibsTransactionId() {
	return sibsTransactionId;
    }

    public void setSibsTransactionId(String sibsTransactionId) {
	this.sibsTransactionId = sibsTransactionId;
    }

    public String getSibsCode() {
	return sibsCode;
    }

    public void setSibsCode(String sibsCode) {
	this.sibsCode = sibsCode;
    }

}
