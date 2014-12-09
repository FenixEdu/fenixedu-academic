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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class GratuityExemption extends GratuityExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {

            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (event instanceof GratuityEvent) {
                    if (exemption instanceof GratuityExemption) {
                        final GratuityEvent gratuityEvent = (GratuityEvent) event;
                        if (gratuityEvent.hasGratuityExemption()) {
                            throw new DomainException(
                                    "error.org.fenixedu.academic.domain.accounting.events.gratuity.GratuityExemption.event.already.has.gratuity.exemption");
                        }

                        final GratuityExemption gratuityExemption = (GratuityExemption) exemption;
                        if (!gratuityEvent.canApplyExemption(gratuityExemption.getJustificationType())) {
                            throw new DomainException(
                                    "error.accounting.events.gratuity.GratuityExemption.gratuity.exemption.type.cannot.applied.to.gratuity.event");
                        }
                    }
                }
            }
        });
    }

    protected GratuityExemption() {
        super();
    }

    protected void init(final Person responsible, final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType exemptionType, final String reason, final YearMonthDay dispatchDate) {
        super.init(responsible, gratuityEvent,
                GratuityExemptionJustificationFactory.create(this, exemptionType, reason, dispatchDate));

        gratuityEvent.recalculateState(new DateTime());
    }

    public GratuityEvent getGratuityEvent() {
        return (GratuityEvent) getEvent();
    }

    public GratuityExemptionJustificationType getJustificationType() {
        return getExemptionJustification().getGratuityExemptionJustificationType();
    }

    @Override
    public GratuityExemptionJustification getExemptionJustification() {
        return (GratuityExemptionJustification) super.getExemptionJustification();
    }

    public boolean isValueExemption() {
        return false;
    }

    public boolean isPercentageExemption() {
        return false;
    }

    abstract public BigDecimal calculateDiscountPercentage(Money amount);

    @Override
    public boolean isGratuityExemption() {
        return true;
    }
}
