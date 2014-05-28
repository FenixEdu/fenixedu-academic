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
import net.sourceforge.fenixedu.domain.accounting.AcademicEvent;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventExemptionJustification;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AcademicEventExemption extends AcademicEventExemption_Base {

    static {
        getRelationExemptionEvent().addListener(new RelationAdapter<Exemption, Event>() {
            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (exemption != null && event != null) {
                    if (exemption instanceof AcademicEventExemption) {
                        final AcademicEvent academicEvent = (AcademicEvent) event;
                        if (academicEvent.hasAcademicEventExemption()) {
                            throw new DomainException(
                                    "error.accounting.events.AcademicEventExemption.event.already.has.exemption");
                        }
                    }
                }
            }
        });
    }

    private AcademicEventExemption() {
        super();
    }

    public AcademicEventExemption(final Person responsible, final AcademicEvent event, final Money value,
            final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

        this();
        super.init(responsible, event, createJustification(justificationType, dispatchDate, reason));
        String[] args = {};

        if (value == null) {
            throw new DomainException("error.AcademicEventExemption.invalid.amount", args);
        }
        setValue(value);

        event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(AcademicEventJustificationType justificationType,
            final LocalDate dispatchDate, String reason) {
        return new AcademicEventExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        if (getEvent().hasAnyPayments()) {
            throw new DomainException("error.accounting.events.candidacy.AcademicEventExemption.cannot.delete.event.has.payments");
        }
    }

    public LocalDate getDispatchDate() {
        return ((AcademicEventExemptionJustification) getExemptionJustification()).getDispatchDate();
    }

    @Atomic
    static public AcademicEventExemption create(final Person responsible, final AcademicEvent event, final Money value,
            final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {
        return new AcademicEventExemption(responsible, event, value, justificationType, dispatchDate, reason);
    }

    @Override
    public boolean isAcademicEventExemption() {
        return true;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

}
