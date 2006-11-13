package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class DebtDTO {

    private YearMonthDay limitDate;

    private String code;

    private Money amount;

    public DebtDTO(final YearMonthDay limitDate, String code, Money amount) {
	this.limitDate = limitDate;
	this.code = code;
	this.amount = amount;
    }

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public YearMonthDay getLimitDate() {
	return limitDate;
    }

    public void setLimitDate(YearMonthDay limitDate) {
	this.limitDate = limitDate;
    }

}
