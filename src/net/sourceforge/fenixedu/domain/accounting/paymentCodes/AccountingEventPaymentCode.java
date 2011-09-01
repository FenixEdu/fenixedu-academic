package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

    protected AccountingEventPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount, final Person person) {
	this();
	init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person);
    }

    public static AccountingEventPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount, final Person person) {
	return PaymentCode.canGenerateNewCode(AccountingEventPaymentCode.class, paymentCodeType, person) ? new AccountingEventPaymentCode(
		paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person) : findAndReuseExistingCode(
			paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person);
    }

    protected static AccountingEventPaymentCode findAndReuseExistingCode(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Event event, final Money minAmount,
	    final Money maxAmount, final Person person) {
	for (PaymentCode code : person.getPaymentCodesBy(paymentCodeType)) {
	    if (code.isAvailableForReuse() && getPaymentCodeGenerator(paymentCodeType).isCodeMadeByThisFactory(code)) {
		AccountingEventPaymentCode accountingEventPaymentCode = ((AccountingEventPaymentCode) code);
		accountingEventPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event);
		return accountingEventPaymentCode;
	    }
	}
	return null;
    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Event event,
	    Money minAmount, Money maxAmount, Person person) {
	super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, person);
	checkParameters(event, person);
	super.setAccountingEvent(event);
    }

    protected void checkParameters(Event event, final Person person) {
	if (person == null || person.getStudent() == null) {
	    throw new DomainException("error.accounting.paymentCodes.AccountingEventPaymentCode.student.cannot.be.null");
	}
    }

    @Override
    public void setAccountingEvent(Event accountingEvent) {
	if (this.getAccountingEvent() != null || !this.isNew())
	    throw new DomainException("error.accounting.paymentCodes.AccountingEventPaymentCode.cannot.modify.accountingEvent");

	_setAccountingEvent(accountingEvent);
    }

    protected void _setAccountingEvent(Event accountingEvent) {
	super.setAccountingEvent(accountingEvent);
    }

    public void reuse(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount, final Money maxAmount,
	    final Event event) {

	reuseCode();
	update(startDate, endDate, minAmount, maxAmount);
	super.setAccountingEvent(event);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {
	final Event event = getAccountingEvent();
	if (LogLevel.WARN) {
	    if (event.isCancelled()) {
		logger.warn("############################ PROCESSING CODE FOR CANCELLED EVENT ###############################");
		logger.warn("Event " + event.getIdInternal() + " for person " + event.getPerson().getIdInternal()
			+ " is cancelled");
		logger.warn("Code Number: " + getCode());
		logger.warn("################################################################################################");
	    }
	}

	event.process(person.getUser(), this, amount, new SibsTransactionDetailDTO(whenRegistered, sibsTransactionId, getCode(),
		comments));
    }

    @Override
    public void delete() {

	super.setAccountingEvent(null);

	super.delete();
    }

    @Override
    public void setPerson(Person student) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.person");
    }

    protected void _setPerson(Person person) {
	super.setPerson(person);
    }

    @Override
    public boolean isAccountingEventPaymentCode() {
	return true;
    }
}
