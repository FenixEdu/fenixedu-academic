package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class GratuitySituationPaymentCode extends GratuitySituationPaymentCode_Base {

    protected GratuitySituationPaymentCode() {
	super();
    }

    private GratuitySituationPaymentCode(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount, final Student student, final GratuitySituation gratuitySituation) {
	this();
	init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student, gratuitySituation);
    }

    private void init(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate,
	    Money minAmount, Money maxAmount, Student student, GratuitySituation gratuitySituation) {

	super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student);

	checkParameters(gratuitySituation);
	super.setGratuitySituation(gratuitySituation);

    }

    private void checkParameters(GratuitySituation gratuitySituation) {
	if (gratuitySituation == null) {
	    throw new DomainException(
		    "error.accounting.paymentCodes.GratuitySituationPaymentCode.gratuitySituation.cannot.be.null");
	}
    }

    public static GratuitySituationPaymentCode create(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount, final Student student, final GratuitySituation gratuitySituation) {

	if (PaymentCodeGenerator.canGenerateNewCode(paymentCodeType, student)) {
	    return new GratuitySituationPaymentCode(paymentCodeType, startDate, endDate, minAmount,
		    maxAmount, student, gratuitySituation);
	}

	throw new DomainException(
		"error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.could.not.generate.new.code");
    }

    @Override
    public void setGratuitySituation(GratuitySituation gratuitySituation) {
	throw new DomainException(
		"error.accounting.paymentCodes.GratuitySituationPaymentCode.cannot.modify.gratuitySituation");
    }

    @Override
    protected void internalProcess(Person responsiblePerson, Money amount, DateTime whenRegistered,
	    String sibsTransactionId, String comments) {
	getGratuitySituation()
		.processAmount(responsiblePerson, amount, whenRegistered, PaymentType.SIBS);
    }
    
    @Override
    public void delete() {
	super.setGratuitySituation(null);
	
        super.delete();
    }

}
