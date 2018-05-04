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

import java.util.ArrayList;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.academic.util.Money;
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

    @Override
    public void removeOtherRelations() {
        for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriodsSet()) {
            period.delete();
        }
        getPhdGratuityPaymentPeriodsSet().clear();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.PHD_GRATUITY_FEE;
    }
}
