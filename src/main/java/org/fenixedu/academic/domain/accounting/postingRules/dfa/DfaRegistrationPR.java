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
package org.fenixedu.academic.domain.accounting.postingRules.dfa;

import java.util.Optional;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.dfa.DfaRegistrationEvent;
import org.fenixedu.academic.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DfaRegistrationPR extends DfaRegistrationPR_Base {

    private DfaRegistrationPR() {
        super();
    }

    public DfaRegistrationPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money fixedAmount, Money fixedAmountPenalty) {
        this();
        super.init(EntryType.REGISTRATION_FEE, EventType.DFA_REGISTRATION, startDate, endDate, serviceAgreementTemplate,
                fixedAmount, fixedAmountPenalty);
    }

    @Override
    protected Optional<LocalDate> getPenaltyDueDate(Event event) {
        final DfaRegistrationEvent dfaRegistrationEvent = (DfaRegistrationEvent) event;
        if (dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()) {
            return Optional.of(dfaRegistrationEvent.getRegistrationPeriodInDegreeCurricularPlan().getEndDateDateTime().toLocalDate());
        }
        return Optional.empty();
    }

    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

        deactivate();

        return new DfaRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount, penaltyAmount);
    }

}
