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
package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ImprovementOfApprovedEnrolmentPenaltyExemption extends ImprovementOfApprovedEnrolmentPenaltyExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof ImprovementOfApprovedEnrolmentPenaltyExemption) {
                        final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent =
                                ((ImprovementOfApprovedEnrolmentEvent) event);
                        if (improvementOfApprovedEnrolmentEvent.hasImprovementOfApprovedEnrolmentPenaltyExemption()) {
                            throw new DomainException(
                                    "error.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption.event.already.has.exemption");
                        }

                    }
                }
            }
        });
    }

    protected ImprovementOfApprovedEnrolmentPenaltyExemption() {
        super();
    }

    public ImprovementOfApprovedEnrolmentPenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
            final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent, final Person responsible,
            final String comments, final YearMonthDay directiveCouncilDispatchDate) {
        this();
        super.init(penaltyExemptionType, improvementOfApprovedEnrolmentEvent, responsible, comments, directiveCouncilDispatchDate);
    }

}
