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
package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

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
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

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
    protected boolean hasPenalty(Event event, DateTime when) {
        return hasPenaltyForRegistration((DfaRegistrationEvent) event)
                && !hasPayedPenaltyForCandidacy((DfaRegistrationEvent) event);
    }

    private boolean hasPenaltyForRegistration(final DfaRegistrationEvent dfaRegistrationEvent) {
        return dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()
                && !dfaRegistrationEvent.getRegistrationPeriodInDegreeCurricularPlan().containsDate(
                        dfaRegistrationEvent.getRegistrationDate());
    }

    private boolean hasPayedPenaltyForCandidacy(final DfaRegistrationEvent dfaRegistrationEvent) {
        return dfaRegistrationEvent.hasCandidacyPeriodInDegreeCurricularPlan()
                && !dfaRegistrationEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
                        dfaRegistrationEvent.getCandidacyDate());

    }

    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

        deactivate();

        return new DfaRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount, penaltyAmount);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        checkPreconditionsToProcess(event);
        return super.internalProcess(user, entryDTOs, event, fromAccount, toAccount, transactionDetail);
    }

    private void checkPreconditionsToProcess(Event event) {
        final DfaRegistrationEvent dfaRegistrationEvent = (DfaRegistrationEvent) event;
        if (!dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()) {
            throw new DomainException(
                    "error.accounting.postingRules.dfa.DfaRegistrationPR.cannot.process.without.registration.period.defined");
        }
    }

}
