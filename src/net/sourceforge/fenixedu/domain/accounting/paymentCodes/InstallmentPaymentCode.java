package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class InstallmentPaymentCode extends InstallmentPaymentCode_Base {

    private InstallmentPaymentCode() {
	super();
    }

    private InstallmentPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Event event, final Installment installment,
	    final Money minAmount, final Money maxAmount, final Student student) {
	this();
	init(paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student);
    }

    public static InstallmentPaymentCode create(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Event event,
	    final Installment installment, final Money minAmount, final Money maxAmount,
	    final Student student) {
	return PaymentCodeGenerator.canGenerateNewCode(paymentCodeType, student) ? new InstallmentPaymentCode(
		paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student)
		: findAndReuseExistingCode(paymentCodeType, startDate, endDate, event, minAmount,
			maxAmount, student, installment);

    }

    private static InstallmentPaymentCode findAndReuseExistingCode(
	    final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
	    final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount,
	    final Student student, final Installment installment) {

	final InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) student
		.getAvailablePaymentCodeBy(paymentCodeType);

	installmentPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event, installment);

	return installmentPaymentCode;

    }

    private void reuse(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount,
	    Event event, Installment installment) {
	super.reuse(startDate, endDate, minAmount, maxAmount, event);
	super.setInstallment(installment);

    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate,
	    YearMonthDay endDate, final Event event, Installment installment, final Money minAmount,
	    final Money maxAmount, final Student student) {
	super.init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student);
	checkParameters(installment);
	super.setInstallment(installment);

    }

    private void checkParameters(Installment installment) {
	if (installment == null) {
	    throw new DomainException(
		    "error.accounting.paymentCodes.InstallmentPaymentCode.installment.cannot.be.null");
	}

    }

    @Override
    public void setInstallment(Installment installment) {
	throw new DomainException(
		"error.accounting.paymentCodes.InstallmentPaymentCode.cannot.modify.installment");
    }

    @Override
    public Money getAmount(DateTime when) {
	return getInstallment().calculateAmount(when);
    }

}
