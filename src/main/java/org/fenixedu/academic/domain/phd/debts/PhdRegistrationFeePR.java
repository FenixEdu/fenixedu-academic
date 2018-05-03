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

import java.util.Optional;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final PhdRegistrationFee feeEvent = (PhdRegistrationFee) event;
        if (feeEvent.hasPhdEventExemption()) {
            amountToPay = amountToPay.subtract(feeEvent.getPhdEventExemption().getValue());
        }

        return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }

    @Override
    protected Optional<LocalDate> getPenaltyDueDate(Event event) {
        final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;
        final PhdIndividualProgramProcess process = phdEvent.getProcess();
        LocalDate whenRatified = process.getCandidacyProcess().getWhenRatified();
        if (whenRatified != null) {
            return Optional.of(whenRatified.plusDays(20));
        }
        return Optional.empty();
    }

}
