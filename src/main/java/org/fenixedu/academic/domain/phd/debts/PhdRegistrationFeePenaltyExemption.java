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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemptionJustificationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class PhdRegistrationFeePenaltyExemption extends PhdRegistrationFeePenaltyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof PhdRegistrationFeePenaltyExemption) {
                        final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;
                        if (phdEvent.hasPhdRegistrationFeePenaltyExemption()) {
                            throw new DomainException("error.PhdRegistrationFeePenaltyExemption.event.already.has.exemption");
                        }

                    }
                }
            }
        });
    }

    private PhdRegistrationFeePenaltyExemption() {
        super();
    }

    public PhdRegistrationFeePenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
            final PhdRegistrationFee event, final Person responsible, final String comments,
            final YearMonthDay directiveCouncilDispatchDate) {
        this();
        super.init(penaltyExemptionType, event, responsible, comments, directiveCouncilDispatchDate);
    }

    @Override
    public PhdRegistrationFee getEvent() {
        return (PhdRegistrationFee) super.getEvent();
    }
}
