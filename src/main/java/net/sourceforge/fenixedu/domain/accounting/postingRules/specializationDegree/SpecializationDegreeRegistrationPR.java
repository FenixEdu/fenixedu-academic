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
package net.sourceforge.fenixedu.domain.accounting.postingRules.specializationDegree;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.specializationDegree.SpecializationDegreeRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class SpecializationDegreeRegistrationPR extends SpecializationDegreeRegistrationPR_Base {

    public SpecializationDegreeRegistrationPR() {
        super();
    }

    public SpecializationDegreeRegistrationPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount, Money fixedAmountPenalty) {
        this();
        super.init(EntryType.REGISTRATION_FEE, EventType.SPECIALIZATION_DEGREE_REGISTRATION, startDate, endDate,
                serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);
    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
        return hasPenaltyForRegistration((SpecializationDegreeRegistrationEvent) event);
    }

    private boolean hasPenaltyForRegistration(final SpecializationDegreeRegistrationEvent specializationDegreeRegistrationEvent) {
        return specializationDegreeRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()
                && !specializationDegreeRegistrationEvent.getRegistrationPeriodInDegreeCurricularPlan().containsDate(
                        specializationDegreeRegistrationEvent.getRegistrationDate());
    }

    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {
        deactivate();

        return new SpecializationDegreeRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
                fixedAmount, penaltyAmount);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        checkPreconditionsToProcess(event);
        return super.internalProcess(user, entryDTOs, event, fromAccount, toAccount, transactionDetail);
    }

    private void checkPreconditionsToProcess(Event event) {
        final SpecializationDegreeRegistrationEvent specializationDegreeRegistrationEvent =
                (SpecializationDegreeRegistrationEvent) event;
        if (!specializationDegreeRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()) {
            throw new DomainException(
                    "error.accounting.postingRules.specializationDegree.SpecializationDegreeRegistrationPR.cannot.process.without.registration.period.defined");
        }
    }

}
