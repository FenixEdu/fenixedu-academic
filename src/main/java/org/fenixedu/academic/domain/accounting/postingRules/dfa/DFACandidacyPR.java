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

import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.dfa.DFACandidacyEvent;
import org.fenixedu.academic.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class DFACandidacyPR extends DFACandidacyPR_Base {

    private DFACandidacyPR() {
        super();
    }

    public DFACandidacyPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            Money fixedAmount, Money fixedAmountPenalty) {
        this();
        super.init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate,
                serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
        final DFACandidacyEvent dfaCandidacyEvent = (DFACandidacyEvent) event;
        return dfaCandidacyEvent.hasCandidacyPeriodInDegreeCurricularPlan()
                && !dfaCandidacyEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
                        dfaCandidacyEvent.getCandidacyDate());

    }

    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

        deactivate();

        return new DFACandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount, penaltyAmount);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        checkPreconditionsToProcess(event);
        return super.internalProcess(user, entryDTOs, event, fromAccount, toAccount, transactionDetail);
    }

    private void checkPreconditionsToProcess(Event event) {
        final DFACandidacyEvent dfaCandidacyEvent = (DFACandidacyEvent) event;
        if (!dfaCandidacyEvent.hasCandidacyPeriodInDegreeCurricularPlan()) {
            throw new DomainException(
                    "error.accounting.postingRules.dfa.DFACandidacyPR.cannot.process.without.candidacy.period.defined");
        }
    }

}
