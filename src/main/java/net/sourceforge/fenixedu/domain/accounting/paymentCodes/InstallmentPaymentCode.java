package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Deprecated
public class InstallmentPaymentCode extends InstallmentPaymentCode_Base {

    private InstallmentPaymentCode() {
        super();
    }

    private InstallmentPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Installment installment, final Money minAmount,
            final Money maxAmount, final Student student) {
        this();
        init(paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student);
    }

    public static InstallmentPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Installment installment, final Money minAmount,
            final Money maxAmount, final Student student) {
        return PaymentCode.canGenerateNewCode(InstallmentPaymentCode.class, paymentCodeType, student.getPerson()) ? new InstallmentPaymentCode(
                paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student) : findAndReuseExistingCode(
                paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student, installment);

    }

    protected static InstallmentPaymentCode findAndReuseExistingCode(final PaymentCodeType paymentCodeType,
            final YearMonthDay startDate, final YearMonthDay endDate, final Event event, final Money minAmount,
            final Money maxAmount, final Student student, final Installment installment) {
        for (PaymentCode code : student.getPerson().getPaymentCodesBy(paymentCodeType)) {
            if (code.isAvailableForReuse() && getPaymentCodeGenerator(paymentCodeType).isCodeMadeByThisFactory(code)) {
                InstallmentPaymentCode accountingEventPaymentCode = ((InstallmentPaymentCode) code);
                accountingEventPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event);
                return accountingEventPaymentCode;
            }
        }
        return null;
    }

    public void reuse(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount, Event event,
            Installment installment) {
        super.reuse(startDate, endDate, minAmount, maxAmount, event);
        super.setInstallment(installment);

    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, final Event event,
            Installment installment, final Money minAmount, final Money maxAmount, final Student student) {
        super.init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student.getPerson());
        checkParameters(installment, student);
        super.setInstallment(installment);

    }

    private void checkParameters(Installment installment, final Student student) {
        if (installment == null) {
            throw new DomainException("error.accounting.paymentCodes.InstallmentPaymentCode.installment.cannot.be.null");
        }

        if (student == null) {
            throw new DomainException("error.accounting.paymentCodes.InstallmentPaymentCode.student.cannot.be.null");
        }

    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Override
    public void setInstallment(Installment installment) {
        super.setInstallment(installment);
    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Override
    public String getDescription() {
        if (getInstallment().getPaymentPlan().hasSingleInstallment()) {
            final ResourceBundle enumerationResources =
                    ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());

            return enumerationResources.getString(PaymentCodeType.TOTAL_GRATUITY.getQualifiedName());

        }

        return super.getDescription();

    }

    @Override
    public boolean isInstallmentPaymentCode() {
        return true;
    }
}
