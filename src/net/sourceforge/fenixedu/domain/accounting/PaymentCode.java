package net.sourceforge.fenixedu.domain.accounting;

import java.util.Comparator;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class PaymentCode extends PaymentCode_Base {

    private static final String ENTITY_CODE = PropertiesManager.getProperty("sibs.entityCode");

    public static Comparator<PaymentCode> COMPARATOR_BY_CODE = new Comparator<PaymentCode>() {
	public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
	    int comparationResult = leftPaymentCode.getCode().compareTo(rightPaymentCode.getCode());
	    if (comparationResult == 0) {
		throw new DomainException(
			"error.accounting.PaymentCode.data.is.corrupted.because.found.duplicate.codes");
	    }
	    return comparationResult;
	}
    };

    public static Comparator<PaymentCode> COMPARATOR_BY_END_DATE = new Comparator<PaymentCode>() {
	public int compare(PaymentCode leftPaymentCode, PaymentCode rightPaymentCode) {
	    int comparationResult = leftPaymentCode.getEndDate()
		    .compareTo(rightPaymentCode.getEndDate());
	    return (comparationResult == 0) ? leftPaymentCode.getIdInternal().compareTo(
		    rightPaymentCode.getIdInternal()) : comparationResult;
	}
    };

    protected PaymentCode() {
	super();
	super.setOjbConcreteClass(getClass().getName());
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
	super.setWhenUpdated(new DateTime());
	super.setState(PaymentCodeState.NEW);
	super.setEntityCode(ENTITY_CODE);

    }

    protected void init(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Money minAmount, final Money maxAmount,
	    final Student student) {

	checkParameters(paymentCodeType, startDate, endDate, maxAmount, maxAmount, student);

	super.setCode(PaymentCodeGenerator.generateNewCodeFor(paymentCodeType, student));

	super.setType(paymentCodeType);
	super.setStartDate(startDate);
	super.setEndDate(endDate);
	super.setMinAmount(minAmount);
	super.setMaxAmount(maxAmount);
	super.setStudent(student);
    }

    private void checkParameters(PaymentCodeType paymentCodeType, YearMonthDay startDate,
	    YearMonthDay endDate, Money minAmount, Money maxAmount, final Student student) {

	if (paymentCodeType == null) {
	    throw new DomainException("error.accounting.PaymentCode.paymentCodeType.cannot.be.null");
	}

	if (student == null) {
	    throw new DomainException("error.accounting.PaymentCode.student.cannot.be.null");
	}

	checkParameters(startDate, endDate, minAmount, maxAmount);

    }

    private void checkParameters(YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
	    Money maxAmount) {
	if (startDate == null) {
	    throw new DomainException("error.accounting.PaymentCode.startDate.cannot.be.null");
	}

	if (endDate == null) {
	    throw new DomainException("error.accounting.PaymentCode.endDate.cannot.be.null");
	}

	if (minAmount == null) {
	    throw new DomainException("error.accounting.PaymentCode.minAmount.cannot.be.null");
	}

	if (maxAmount == null) {
	    throw new DomainException("error.accounting.PaymentCode.maxAmount.cannot.be.null");
	}

    }

    public String getFormattedCode() {
	final StringBuilder result = new StringBuilder();
	int i = 1;
	for (char character : getCode().toCharArray()) {
	    result.append(character);
	    if (i % 3 == 0) {
		result.append(" ");
	    }
	    i++;
	}

	return result.charAt(result.length() - 1) == ' ' ? result.deleteCharAt(result.length() - 1)
		.toString() : result.toString();
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.accounting.PaymentCode.cannot.modify.whenCreated");
    }

    @Override
    public void setCode(String code) {
	throw new DomainException("error.accounting.PaymentCode.cannot.modify.code");
    }

    @Override
    public void setStartDate(YearMonthDay startDate) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.startDate");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.endDate");
    }

    @Override
    public void setMinAmount(Money minAmount) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.minAmount");
    }

    @Override
    public void setMaxAmount(Money maxAmount) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.maxAmount");
    }

    @Override
    public void setWhenUpdated(DateTime whenUpdated) {
	throw new DomainException("error.accounting.PaymentCode.cannot.modify.whenUpdated");
    }

    @Override
    public void setState(PaymentCodeState state) {
	super.setWhenUpdated(new DateTime());
	super.setState(state);
    }

    @Override
    public void setEntityCode(String entityCode) {
	throw new DomainException("error.accounting.PaymentCode.cannot.modify.entityCode");
    }

    public boolean isNew() {
	return getState() == PaymentCodeState.NEW;
    }

    protected void reuseCode() {
	setState(PaymentCodeState.NEW);
    }

    public boolean isProcessed() {
	return getState() == PaymentCodeState.PROCESSED;
    }

    public boolean isCancelled() {
	return getState() == PaymentCodeState.CANCELLED;
    }

    public boolean isAvailableForReuse() {
	return !isNew();
    }

    public void update(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount) {
	checkParameters(startDate, endDate, maxAmount, maxAmount);
	super.setStartDate(startDate);
	super.setEndDate(endDate);
	super.setMinAmount(minAmount);
	super.setMaxAmount(maxAmount);
	super.setWhenUpdated(new DateTime());
    }

    public void process(Person responsiblePerson, Money amount, DateTime whenRegistered,
	    String sibsTransactionId) {
	
	if (isProcessed()) {
	    return;
	}
	
	internalProcess(responsiblePerson, amount, whenRegistered, sibsTransactionId);
	setState(PaymentCodeState.PROCESSED);	
    }

    abstract protected void internalProcess(final Person person, final Money amount,
	    final DateTime whenRegistered, final String sibsTransactionId);

}
