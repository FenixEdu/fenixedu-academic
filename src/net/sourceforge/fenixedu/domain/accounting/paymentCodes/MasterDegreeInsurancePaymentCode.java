package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class MasterDegreeInsurancePaymentCode extends MasterDegreeInsurancePaymentCode_Base {

    protected MasterDegreeInsurancePaymentCode() {
	super();
    }

    private MasterDegreeInsurancePaymentCode(final PaymentCodeType paymentCodeType,
	    final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount,
	    final Money maxAmount, final Student student, final ExecutionYear executionYear) {
	this();
	init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student, executionYear);
    }

    private void init(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate,
	    Money minAmount, Money maxAmount, Student student, ExecutionYear executionYear) {
	super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student);

	checkParameters(executionYear);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.executionYear.cannot.be.null");
	}
    }

    public static MasterDegreeInsurancePaymentCode create(final YearMonthDay startDate,
	    final YearMonthDay endDate, final Money minAmount, final Money maxAmount,
	    final Student student, final ExecutionYear executionYear) {

	if (PaymentCodeGenerator.canGenerateNewCode(PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_INSURANCE,
		student)) {
	    return new MasterDegreeInsurancePaymentCode(
		    PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_INSURANCE, startDate, endDate, minAmount,
		    maxAmount, student, executionYear);
	}

	throw new DomainException(
		"error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.could.not.generate.new.code");
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.cannot.modify.executionYear");
    }

    @Override
    protected void internalProcess(Person responsiblePerson, Money amount, DateTime whenRegistered,
	    String sibsTransactionId) {
	new InsuranceTransaction(amount.getAmount(), whenRegistered, PaymentType.SIBS,
		responsiblePerson, getPersonAccount(), getExecutionYear(), getRegistration());
    }

    private PersonAccount getPersonAccount() {
	return getStudent().getPerson().getAssociatedPersonAccount();
    }

    private Registration getRegistration() {
	return getStudent().getActiveRegistrationByDegreeType(DegreeType.MASTER_DEGREE);
    }

    @Override
    public void delete() {
	super.setStudent(null);
	super.setExecutionYear(null);
	super.delete();
    }

}
