/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.debts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdGratuityPR extends PhdGratuityPR_Base {

    public PhdGratuityPR() {
    }

    public PhdGratuityPR(DateTime start, DateTime end, ServiceAgreementTemplate serviceAgreementTemplate, Money gratuity,
            double fineRate) {
        super();
        init(EventType.PHD_GRATUITY, start, end, serviceAgreementTemplate, gratuity, fineRate);

    }

    protected void init(EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money gratuity, double fineRate) {
        super.init(eventType, startDate, endDate, serviceAgreementTemplate);
        setGratuity(gratuity);
        setFineRate(fineRate);
    }

    @Override
    public void setGratuity(Money gratuity) {
        if (gratuity.lessThan(new Money(0))) {
            throw new RuntimeException("error.negative.gratuity");
        }
        super.setGratuity(gratuity);
    }

    @Override
    public void setFineRate(Double fineRate) {
        if (fineRate <= 0 || fineRate > 1) {
            throw new RuntimeException("error.invalid.fine.rate");
        }
        super.setFineRate(fineRate);
    }

    public Money getGratuityByProcess(PhdIndividualProgramProcess process) {
        ArrayList<PhdProgramProcessState> states = new ArrayList<PhdProgramProcessState>(process.getStates());
        if (states.size() > 0 && getPhdGratuityPriceQuirksSet().size() > 0) {
            int years = 0;

            for (PhdGratuityEvent event : process.getPhdGratuityEvents()) {
                if (!event.isInState(EventState.CANCELLED)) {
                    years++;
                }
            }

            for (PhdGratuityPriceQuirk quirk : getPhdGratuityPriceQuirks()) {
                if (quirk.getYear() == years) {
                    return quirk.getGratuity();
                }
            }

            return getGratuity();
        } else {
            return getGratuity();
        }
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        PhdGratuityEvent phdGratuityEvent = (PhdGratuityEvent) event;
        Money gratuity = getGratuityByProcess(phdGratuityEvent.getPhdIndividualProgramProcess());
        return gratuity;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        PhdGratuityEvent phdGratuityEvent = (PhdGratuityEvent) event;

        amountToPay = adjustGratuityWithExmptions(phdGratuityEvent, amountToPay);

        BigDecimal percentage = new BigDecimal(0);
        for (Exemption exemption : phdGratuityEvent.getExemptions()) {
            if (exemption.isGratuityExemption()) {
                percentage = percentage.add(((GratuityExemption) exemption).calculateDiscountPercentage(amountToPay));
            }
        }

        amountToPay = amountToPay.subtract(amountToPay.multiply(percentage));
        if (amountToPay.lessOrEqualThan(Money.ZERO)) {
            return Money.ZERO;
        }

        return amountToPay;
    }

    private boolean hasFineExemption(PhdGratuityEvent phdGratuityEvent) {
        for (Exemption exemption : phdGratuityEvent.getExemptions()) {
            if (exemption instanceof PhdGratuityFineExemption) {
                return true;
            }
        }
        return false;
    }

    private Money adjustGratuityWithExmptions(PhdGratuityEvent phdGratuityEvent, Money gratuity) {
        if (phdGratuityEvent.getExemptionsSet().size() > 0) {
            for (Exemption exemption : phdGratuityEvent.getExemptions()) {
                if (exemption instanceof PhdEventExemption && !(exemption instanceof PhdGratuityFineExemption)) {
                    gratuity = gratuity.subtract(((PhdEventExemption) exemption).getValue());
                }
            }
        }
        return gratuity;
    }

    public PhdGratuityPaymentPeriod getPhdGratuityPeriod(LocalDate programStartDate) {
        for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriods()) {
            if (period.contains(programStartDate)) {
                return period;
            }
        }

        throw new DomainException("error.phd.debts.PhdGratuityPR.cannot.find.period");
    }

    private Money getFine(LocalDate programStartDate, DateTime when, Money value) {
        PhdGratuityPaymentPeriod phdGratuityPeriod = getPhdGratuityPeriod(programStartDate);

        return phdGratuityPeriod.fine(getFineRate(), getGratuity(), when);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(EntryType.PHD_GRATUITY_FEE, event, calculateTotalAmountToPay(event, when),
                event.getPayedAmount(), event.calculateAmountToPay(when), event
                        .getDescriptionForEntryType(EntryType.PHD_GRATUITY_FEE), event.calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException("error.accounting.postingRules.gratuity.PhdGratuityPR.invalid.number.of.entryDTOs");
        }

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, EntryType.PHD_GRATUITY_FEE,
                entryDTOs.iterator().next().getAmountToPay(), transactionDetail));
    }

    @Override
    public void removeOtherRelations() {
        for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriods()) {
            period.delete();
        }
        getPhdGratuityPaymentPeriods().clear();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPaymentPeriod> getPhdGratuityPaymentPeriods() {
        return getPhdGratuityPaymentPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityPaymentPeriods() {
        return !getPhdGratuityPaymentPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPriceQuirk> getPhdGratuityPriceQuirks() {
        return getPhdGratuityPriceQuirksSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityPriceQuirks() {
        return !getPhdGratuityPriceQuirksSet().isEmpty();
    }

    @Deprecated
    public boolean hasGratuity() {
        return getGratuity() != null;
    }

    @Deprecated
    public boolean hasFineRate() {
        return getFineRate() != null;
    }

}
