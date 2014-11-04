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

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramCalendarUtil;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class PhdRegistrationFeePR extends PhdRegistrationFeePR_Base {

    private PhdRegistrationFeePR() {
        super();
    }

    public PhdRegistrationFeePR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount, final Money fixedAmountPenalty) {
        this();
        super.init(EntryType.PHD_REGISTRATION_FEE, EventType.PHD_REGISTRATION_FEE, startDate, endDate, serviceAgreementTemplate,
                fixedAmount, fixedAmountPenalty);
    }

    public PhdRegistrationFeePR edit(final Money fixedAmount, final Money penaltyAmount) {
        deactivate();
        return new PhdRegistrationFeePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount,
                penaltyAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        Money total =
                super.doCalculationForAmountToPay(event, when, applyDiscount).add(
                        hasPenalty(event, when) ? getFixedAmountPenalty() : Money.ZERO);

        return total;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final PhdRegistrationFee feeEvent = (PhdRegistrationFee) event;
        if (feeEvent.hasPhdEventExemption()) {
            amountToPay = amountToPay.subtract(feeEvent.getPhdEventExemption().getValue());
        }

        return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }

    @Override
    protected boolean hasPenalty(final Event event, final DateTime when) {
        final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;

        if (phdEvent.hasPhdRegistrationFeePenaltyExemption()) {
            return false;
        }

        final PhdIndividualProgramProcess process = phdEvent.getProcess();
        return PhdProgramCalendarUtil.countWorkDaysBetween(process.getCandidacyProcess().getWhenRatified(),
                process.getWhenFormalizedRegistration()) > 20;

    }

}
