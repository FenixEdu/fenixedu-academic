package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.joda.time.DateTime;

public abstract class BaseAmountPlusAmountPerUnitPR extends BaseAmountPlusAmountPerUnitPR_Base {

    protected BaseAmountPlusAmountPerUnitPR() {
        super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal baseAmount,
            BigDecimal amountPerUnit) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
        checkParameters(baseAmount, amountPerUnit);
        super.setBaseAmount(baseAmount);
        super.setAmountPerUnit(amountPerUnit);
    }

    private void checkParameters(BigDecimal baseAmount, BigDecimal amountPerUnit) {
        if (baseAmount == null) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.baseAmount.cannot.be.null");
        }
        if (amountPerUnit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.amountPerUnit.cannot.be.null");
        }

    }

    @Override
    public void setBaseAmount(BigDecimal baseAmount) {
        throw new DomainException(
                "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.cannot.modify.baseAmount");
    }

    @Override
    public void setAmountPerUnit(BigDecimal amountPerUnit) {
        throw new DomainException(
                "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.cannot.modify.amountPerUnit");
    }

    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
        return getBaseAmount().add(getAmountPerUnit().multiply(new BigDecimal(getNumberOfUnits(event))));

    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(
                event, when), new BigDecimal("0"), calculateTotalAmountToPay(event, when), event
                .getDescriptionForEntryType(getEntryType())));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
            Account toAccount) {
        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.get(0);
        checkIfCanAddAmount(entryDTO.getAmountToPay(), event, whenRegistered);

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
                entryDTO.getEntryType(), entryDTO.getAmountToPay(), paymentMode, whenRegistered));
    }

    private void checkIfCanAddAmount(BigDecimal amountToPay, Event event, DateTime when) {
        if (!amountToPay.equals(calculateTotalAmountToPay(event, when))) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.amount.being.payed.must.match.amount.to.pay",
                    event.getDescriptionForEntryType(getEntryType()));
        }

    }

    protected abstract Integer getNumberOfUnits(Event event);

}
