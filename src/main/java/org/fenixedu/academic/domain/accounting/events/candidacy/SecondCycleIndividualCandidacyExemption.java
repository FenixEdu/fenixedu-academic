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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.events.ExemptionJustification;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class SecondCycleIndividualCandidacyExemption extends SecondCycleIndividualCandidacyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof SecondCycleIndividualCandidacyExemption) {
                        final SecondCycleIndividualCandidacyEvent candidacyEvent = ((SecondCycleIndividualCandidacyEvent) event);
                        if (candidacyEvent.hasSecondCycleIndividualCandidacyExemption()) {
                            throw new DomainException(
                                    "error.accounting.events.SecondCycleIndividualCandidacyExemption.event.already.has.exemption");
                        }
                    }
                }
            }
        });
    }

    private SecondCycleIndividualCandidacyExemption() {
        super();
    }

    public SecondCycleIndividualCandidacyExemption(final Person responsible, final SecondCycleIndividualCandidacyEvent event,
            final CandidacyExemptionJustificationType candidacyExemptionJustificationType) {
        this();
        super.init(responsible, event, createJustification(candidacyExemptionJustificationType));
        event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(final CandidacyExemptionJustificationType justificationType) {
        return new SecondCycleIndividualCandidacyExemptionJustification(this, justificationType);
    }

    @Override
    public SecondCycleIndividualCandidacyEvent getEvent() {
        return (SecondCycleIndividualCandidacyEvent) super.getEvent();
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        if (getEvent().hasAnyPayments()) {
            throw new DomainException(
                    "error.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption.cannot.delete.event.has.payments");
        }

        if (getEvent().getIndividualCandidacy().isAccepted()) {
            throw new DomainException(
                    "error.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption.cannot.delete.candidacy.is.accepted");
        }
    }

    @Override
    public boolean isSecondCycleIndividualCandidacyExemption() {
        return true;
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return money;
    }
}
