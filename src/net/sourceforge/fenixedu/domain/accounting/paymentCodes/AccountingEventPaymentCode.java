package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class AccountingEventPaymentCode extends AccountingEventPaymentCode_Base {

    private static final Logger logger = Logger.getLogger(AccountingEventPaymentCode.class);

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

    private static AccountingEventPaymentCode findAndReuseExistingCode(
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

    public void reuse(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount, final Event event) {

	reuseCode();
	update(startDate, endDate, minAmount, maxAmount);
	super.setAccountingEvent(event);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered,
	    String sibsTransactionId, String comments) {
	final Event event = getAccountingEvent();
	if (event.isCancelled()) {
	    logger.warn("############################ PROCESSING CODE FOR CANCELLED EVENT ###############################");
	    logger.warn("Event " + event.getIdInternal() + " for person " + event.getPerson().getIdInternal() + " is cancelled");
	    logger.warn("Code Number: " + getCode());
	    logger.warn("################################################################################################");
	}

	event.process(person.getUser(), this, amount, new SibsTransactionDetailDTO(whenRegistered,
		sibsTransactionId, getCode(), comments));
    }

    @Override
    public void delete() {

	super.setAccountingEvent(null);

	super.delete();
    }

}
