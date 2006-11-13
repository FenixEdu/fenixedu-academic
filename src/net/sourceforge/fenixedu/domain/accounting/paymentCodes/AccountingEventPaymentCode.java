package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class AccountingEventPaymentCode extends AccountingEventPaymentCode_Base {

    static {
	PaymentCodeAccountingEvent.addListener(new RelationAdapter<AccountingEventPaymentCode, Event>() {
	    @Override
	    public void beforeAdd(AccountingEventPaymentCode accountingEventPaymentCode, Event event) {
		if (event instanceof InsuranceEvent) {
		    if (event.hasAnyPaymentCodes()) {
			throw new DomainException(
				"error.accounting.paymentCodes.AccountingEventPaymentCode.InsuranceEvent.already.has.payment.code.associated");
		    }
		}
	    }
	});
    }

    protected AccountingEventPaymentCode() {
	super();
    }

    private AccountingEventPaymentCode(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Event event,
	    final Money minAmount, final Money maxAmount, final Student student) {
	this();
	init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student);
    }

    public static AccountingEventPaymentCode create(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Event event,
	    final Money minAmount, final Money maxAmount, final Student student) {
	return PaymentCodeGenerator.canGenerateNewCode(paymentCodeType, student) ? new AccountingEventPaymentCode(
		paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student)
		: findAndReuseExistingCode(paymentCodeType, startDate, endDate, event, minAmount,
			maxAmount, student);

    }

    protected static AccountingEventPaymentCode findAndReuseExistingCode(
	    final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount,
	    final Student student) {
	final AccountingEventPaymentCode accountingEventPaymentCode = (AccountingEventPaymentCode) student
		.getAvailablePaymentCodeBy(paymentCodeType);
	accountingEventPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event);

	return accountingEventPaymentCode;
    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate,
	    YearMonthDay endDate, Event event, Money minAmount, Money maxAmount, Student student) {
	super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student);
	checkParameters(event);
	super.setAccountingEvent(event);
    }

    private void checkParameters(Event event) {
	if (event == null) {
	    throw new DomainException(
		    "error.accounting.paymentCodes.AccountingEventPaymentCode.event.cannot.be.null");
	}
    }

    @Override
    public void setAccountingEvent(Event accountingEvent) {
	throw new DomainException(
		"error.accounting.paymentCodes.AccountingEventPaymentCode.cannot.modify.accountingEvent");
    }

    @Override
    public Money getAmount(DateTime when) {
	return getAccountingEvent().calculateAmountToPay(when);
    }

    public void cancel() {
	super.setState(PaymentCodeState.CANCELLED);
    }

    public void update(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount,
	    Event event) {
	super.update(startDate, endDate, minAmount, maxAmount);
	super.setAccountingEvent(event);
    }

    public void reuse(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount, final Event event) {

	reuseCode();
	update(startDate, endDate, minAmount, maxAmount, event);
    }

}
