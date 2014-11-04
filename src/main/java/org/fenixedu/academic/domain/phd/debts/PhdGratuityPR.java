/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.debts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityExemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
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

            for (PhdGratuityEvent event : process.getPhdGratuityEventsSet()) {
                if (!event.isInState(EventState.CANCELLED)) {
                    years++;
                }
            }

            for (PhdGratuityPriceQuirk quirk : getPhdGratuityPriceQuirksSet()) {
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
        for (Exemption exemption : phdGratuityEvent.getExemptionsSet()) {
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
        for (Exemption exemption : phdGratuityEvent.getExemptionsSet()) {
            if (exemption instanceof PhdGratuityFineExemption) {
                return true;
            }
        }
        return false;
    }

    private Money adjustGratuityWithExmptions(PhdGratuityEvent phdGratuityEvent, Money gratuity) {
        if (phdGratuityEvent.getExemptionsSet().size() > 0) {
            for (Exemption exemption : phdGratuityEvent.getExemptionsSet()) {
                if (exemption instanceof PhdEventExemption && !(exemption instanceof PhdGratuityFineExemption)) {
                    gratuity = gratuity.subtract(((PhdEventExemption) exemption).getValue());
                }
            }
        }
        return gratuity;
    }

    public PhdGratuityPaymentPeriod getPhdGratuityPeriod(LocalDate programStartDate) {
        for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriodsSet()) {
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
        for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriodsSet()) {
            period.delete();
        }
        getPhdGratuityPaymentPeriodsSet().clear();
    }

}
