package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceEvent extends
	AdministrativeOfficeFeeAndInsuranceEvent_Base {

    protected AdministrativeOfficeFeeAndInsuranceEvent() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice,
	    Person person, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(
		getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);

    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    public boolean hasToPayInsurance() {
	for (final AccountingTransaction accountingTransaction : getNonAdjustingTransactions()) {
	    if (accountingTransaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE) {
		return false;
	    }
	}

	return !getPerson().hasInsuranceEventFor(getExecutionYear());
    }

    public boolean hasToPayAdministrativeOfficeFee() {
	for (final AccountingTransaction accountingTransaction : getNonAdjustingTransactions()) {
	    if (accountingTransaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
		return false;
	    }
	}

	return true;
    }

    private AdministrativeOfficeFeeAndInsurancePR getAdministrativeOfficeFeeAndInsurancePR() {
	return (AdministrativeOfficeFeeAndInsurancePR) getPostingRule();
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeeAmount(
		getStartDate(), getEndDate());
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePaymentLimitDate(
		getStartDate(), getEndDate());
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePenaltyAmount(
		getStartDate(), getEndDate());
    }

    public Money getInsuranceAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getInsuranceAmount(getStartDate(),
		getEndDate());
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	return Collections
		.singletonList(AccountingEventPaymentCode.create(
			PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE, new YearMonthDay(),
			calculatePaymentCodeEndDate(), this, totalAmount, totalAmount, getPerson()
				.getStudent()));
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	getNonProcessedPaymentCode().update(new YearMonthDay(), calculatePaymentCodeEndDate(),
		totalAmount, totalAmount);

	return getNonProcessedPaymentCodes();
    }

    private AccountingEventPaymentCode getNonProcessedPaymentCode() {
	return (getNonProcessedPaymentCodes().isEmpty() ? null : getNonProcessedPaymentCodes()
		.iterator().next());
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay administrativeOfficeFeePaymentLimitDate = getAdministrativeOfficeFeePaymentLimitDate();
	return today.isBefore(administrativeOfficeFeePaymentLimitDate) ? administrativeOfficeFeePaymentLimitDate
		: calculateNextEndDate(today);
    }

    private Money calculateTotalAmount() {
	Money totalAmount = Money.ZERO;
	for (final EntryDTO entryDTO : calculateEntries()) {
	    totalAmount = totalAmount.add(entryDTO.getAmountToPay());
	}
	return totalAmount;
    }

    public AccountingEventPaymentCode calculatePaymentCode() {
	return calculatePaymentCodes().iterator().next();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode,
	    Money amountToPay, SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser, buildEntryDTOsFrom(amountToPay), transactionDetail);
    }

    private List<EntryDTO> buildEntryDTOsFrom(final Money amountToPay) {
	final List<EntryDTO> result = new ArrayList<EntryDTO>(2);
	Money insuranceAmountToDiscount = Money.ZERO;
	if (hasToPayInsurance()) {
	    insuranceAmountToDiscount = getInsuranceAmount();
	    result.add(buildInsuranceEntryDTO(insuranceAmountToDiscount));
	}

	final Money remainingAmount = amountToPay.subtract(insuranceAmountToDiscount);
	if (remainingAmount.isPositive()) {
	    result.add(buildAdministrativeOfficeFeeEntryDTO(remainingAmount));
	}

	return result;
    }

    private EntryDTO buildAdministrativeOfficeFeeEntryDTO(Money administrativeOfficeFeeAmountToDiscount) {
	return new EntryDTO(EntryType.ADMINISTRATIVE_OFFICE_FEE, this,
		administrativeOfficeFeeAmountToDiscount);
    }

    private EntryDTO buildInsuranceEntryDTO(Money insuranceAmountToDiscount) {
	return new EntryDTO(EntryType.INSURANCE_FEE, this, insuranceAmountToDiscount);
    }

    public void changePaymentCodeState(DateTime whenRegistered, PaymentMode paymentMode) {
	if (canCloseEvent(whenRegistered) && hasNonProcessedPaymentCode()) {
	    getNonProcessedPaymentCode().setState(getPaymentCodeStateFor(paymentMode));
	}
    }

    private boolean hasNonProcessedPaymentCode() {
	return (getNonProcessedPaymentCode() != null);
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ").appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasAdministrativeOfficeFeeAndInsuranceExemption() {
	return getAdministrativeOfficeFeeAndInsurancePenaltyExemption() != null;
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption getAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption) {
		return (AdministrativeOfficeFeeAndInsurancePenaltyExemption) exemption;
	    }
	}

	return null;
    }
}
