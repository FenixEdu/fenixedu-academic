package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryWithInstallmentDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class GratuityEventWithPaymentPlan extends GratuityEventWithPaymentPlan_Base {

    protected GratuityEventWithPaymentPlan() {
	super();
    }

    public GratuityEventWithPaymentPlan(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, person, studentCurricularPlan, executionYear);

    }

    @Override
    protected void init(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	super.init(administrativeOffice, person, studentCurricularPlan, executionYear);
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();
	for (final EntryDTO entryDTO : calculateEntries()) {

	    if (entryDTO instanceof EntryWithInstallmentDTO) {
		result.add(createInstallmentPaymentCode((EntryWithInstallmentDTO) entryDTO, getPerson()
			.getStudent()));
	    } else {
		result.add(createAccountingEventPaymentCode(entryDTO, getPerson().getStudent()));
	    }
	}
	return result;
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final List<EntryDTO> entryDTOs = calculateEntries();
	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    final EntryDTO entryDTO = findEntryDTOForPaymentCode(entryDTOs, paymentCode);
	    if (paymentCode instanceof InstallmentPaymentCode) {
		final InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;
		paymentCode.update(new YearMonthDay(),
			calculateInstallmentPaymentCodeEndDate(installmentPaymentCode.getInstallment()),
			entryDTO.getAmountToPay(), entryDTO.getAmountToPay());
	    } else {
		paymentCode.update(new YearMonthDay(), calculateFullPaymentCodeEndDate(), entryDTO
			.getAmountToPay(), entryDTO.getAmountToPay());
	    }

	}

	return getNonProcessedPaymentCodes();
    }

    private YearMonthDay calculateInstallmentPaymentCodeEndDate(final Installment installment) {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay installmentEndDate = installment.getEndDate();
	return today.isBefore(installmentEndDate) ? installmentEndDate : calculateNextEndDate(today);
    }

    private YearMonthDay calculateFullPaymentCodeEndDate() {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay totalEndDate = getFirstInstallment().getEndDate();
	return today.isBefore(totalEndDate) ? totalEndDate : calculateNextEndDate(today);
    }

    private EntryDTO findEntryDTOForPaymentCode(List<EntryDTO> entryDTOs,
	    AccountingEventPaymentCode paymentCode) {

	for (final EntryDTO entryDTO : entryDTOs) {
	    if (entryDTO instanceof EntryWithInstallmentDTO) {
		if (paymentCode instanceof InstallmentPaymentCode) {
		    if (((InstallmentPaymentCode) paymentCode).getInstallment() == ((EntryWithInstallmentDTO) entryDTO)
			    .getInstallment()) {
			return entryDTO;
		    }
		}
	    } else {
		return entryDTO;
	    }
	}

	throw new DomainException(
		"error.accounting.events.gratuity.GratuityEventWithPaymentPlan.paymentCode.does.not.have.corresponding.entryDTO.because.data.is.corrupted");

    }

    public void changeGratuityTotalPaymentCodeState(final PaymentCodeState paymentCodeState) {
	for (final AccountingEventPaymentCode accountingEventPaymentCode : getNonProcessedPaymentCodes()) {
	    if (!(accountingEventPaymentCode instanceof InstallmentPaymentCode)) {
		accountingEventPaymentCode.setState(paymentCodeState);
	    }
	}
    }

    public void changeInstallmentPaymentCodeState(final Installment installment,
	    final PaymentCodeState paymentCodeState) {
	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    if (paymentCode instanceof InstallmentPaymentCode
		    && ((InstallmentPaymentCode) paymentCode).getInstallment() == installment) {
		paymentCode.setState(paymentCodeState);
	    }
	}

	// If at least one installment is payed, we assume that the payment
	// will be based on installments
	changeGratuityTotalPaymentCodeState(PaymentCodeState.CANCELLED);
    }

    private AccountingEventPaymentCode createAccountingEventPaymentCode(final EntryDTO entryDTO,
	    final Student student) {
	return AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
		calculateFullPaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO
			.getAmountToPay(), student);
    }

    private InstallmentPaymentCode createInstallmentPaymentCode(final EntryWithInstallmentDTO entry,
	    final Student student) {
	return InstallmentPaymentCode.create(getPaymentCodeTypeFor(entry.getInstallment()),
		new YearMonthDay(), calculateInstallmentPaymentCodeEndDate(entry.getInstallment()),
		this, entry.getInstallment(), entry.getAmountToPay(), entry.getAmountToPay(), student);
    }

    private PaymentCodeType getPaymentCodeTypeFor(Installment installment) {
	if (installment.getOrder() == 1) {
	    return PaymentCodeType.GRATUITY_FIRST_INSTALLMENT;
	} else if (installment.getOrder() == 2) {
	    return PaymentCodeType.GRATUITY_SECOND_INSTALLMENT;
	} else {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityEventWithPaymentPlan.invalid.installment.order");
	}

    }

    private Installment getFirstInstallment() {
	return getGratuityPaymentPlan().getFirstInstallment();
    }

    private Installment getLastInstallment() {
	return getGratuityPaymentPlan().getLastInstallment();
    }

    public GratuityPaymentPlan getGratuityPaymentPlan() {
	final GratuityPaymentPlan gratuityPaymentPlanFor = getServiceAgreementTemplate()
		.getGratuityPaymentPlanFor(getStudentCurricularPlan(), getExecutionYear());

	return (gratuityPaymentPlanFor != null ? gratuityPaymentPlanFor
		: (GratuityPaymentPlan) getServiceAgreementTemplate().getDefaultPaymentPlan(
			getExecutionYear()));
    }

    @Override
    protected DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
	return getStudentCurricularPlan().getDegreeCurricularPlan().getServiceAgreementTemplate();
    }

    @Override
    public boolean hasInstallments() {
	return true;
    }

    public InstallmentPaymentCode getInstallmentPaymentCodeFor(final Installment installment) {
	for (final AccountingEventPaymentCode paymentCode : calculatePaymentCodes()) {
	    if (paymentCode instanceof InstallmentPaymentCode
		    && ((InstallmentPaymentCode) paymentCode).getInstallment() == installment) {
		return (InstallmentPaymentCode) paymentCode;
	    }
	}

	return null;
    }

    public AccountingEventPaymentCode getTotalPaymentCode() {
	for (final AccountingEventPaymentCode paymentCode : calculatePaymentCodes()) {
	    if (!(paymentCode instanceof InstallmentPaymentCode)) {
		return paymentCode;
	    }
	}

	return null;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode,
	    Money amountToPay, SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser, Collections.singletonList(buildEntryDTOFrom(paymentCode,
		amountToPay)), transactionDetail);
    }

    private EntryDTO buildEntryDTOFrom(final AccountingEventPaymentCode paymentCode,
	    final Money amountToPay) {
	if (paymentCode instanceof InstallmentPaymentCode) {
	    return new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, this, amountToPay,
		    ((InstallmentPaymentCode) paymentCode).getInstallment());
	} else {
	    return new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay);
	}
    }

    public boolean installmentIsInDebt(Installment installment) {
	return !hasAccountingTransactionFor(installment)
		&& new YearMonthDay().isAfter(installment.getEndDate());
    }

    @Override
    public boolean isInDebt() {
	return !isClosed()
		&& (installmentIsInDebt(getFirstInstallment()) || installmentIsInDebt(getLastInstallment()));
    }

    public InstallmentPenaltyExemption getInstallmentPenaltyExemptionFor(final Installment installment) {
	for (final PenaltyExemption penaltyExemption : getPenaltyExemptionsSet()) {
	    if (penaltyExemption instanceof InstallmentPenaltyExemption) {
		final InstallmentPenaltyExemption installmentPenaltyExemption = (InstallmentPenaltyExemption) penaltyExemption;
		if (installmentPenaltyExemption.getInstallment() == installment) {
		    return installmentPenaltyExemption;
		}
	    }
	}

	return null;
    }

    public boolean hasPenaltyExemptionFor(final Installment installment) {
	for (final PenaltyExemption penaltyExemption : getPenaltyExemptionsSet()) {
	    if (penaltyExemption instanceof InstallmentPenaltyExemption) {
		if (((InstallmentPenaltyExemption) penaltyExemption).getInstallment() == installment) {
		    return true;
		}

	    }
	}

	return false;
    }

    public Set<Installment> getInstallmentsWithoutPenalty() {
	final Set<Installment> result = new HashSet<Installment>();
	for (final PenaltyExemption penaltyExemption : getPenaltyExemptionsSet()) {
	    if (penaltyExemption instanceof InstallmentPenaltyExemption) {
		result.add(((InstallmentPenaltyExemption) penaltyExemption).getInstallment());
	    }
	}

	return result;

    }

    @Override
    public boolean isPenaltyExemptionApplicable() {
	return true;
    }

}
