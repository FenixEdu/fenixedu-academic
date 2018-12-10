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
package org.fenixedu.academic.domain.accounting.events;

import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class PenaltyExemption extends PenaltyExemption_Base {

    protected PenaltyExemption() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected PenaltyExemption(final EventExemptionJustificationType justificationType, final GratuityEvent gratuityEvent,
            final Person responsible, final String comments, final YearMonthDay dispatchDate) {
        this();
        init(justificationType, gratuityEvent, responsible, comments, dispatchDate);
    }

    protected void init(EventExemptionJustificationType justificationType, Event event, Person responsible, String reason,
            YearMonthDay dispatchDate) {
        checkParameters(justificationType);
        super.init(responsible, event, createJustification(justificationType, dispatchDate == null ? null : dispatchDate.toLocalDate(), reason));
        event.recalculateState(new DateTime());

    }

    private void checkParameters(EventExemptionJustificationType penaltyExemptionType) {
        if (penaltyExemptionType == null) {
            throw new DomainException(
                    "error.accounting.events.gratuity.exemption.penalty.PenaltyExemption.penaltyExemptionType.cannot.be.null");
        }
    }

    public EventExemptionJustificationType getJustificationType() {
        return getExemptionJustification().getJustificationType();
    }

    @Override
    public ExemptionJustification getExemptionJustification() {
        return super.getExemptionJustification();
    }

    @Override
    public boolean isPenaltyExemption() {
        return true;
    }

    @Override
    public Money getExemptionAmount(Money money) {
        return money;
    }

    public boolean isForInterest() {
        return false;
    }

    public boolean isForFine() {
        return true;
    }

    public Set<LocalDate> getDueDates(DateTime when) {
        return getEvent().getDueDateAmountMap().keySet();
    }
}
