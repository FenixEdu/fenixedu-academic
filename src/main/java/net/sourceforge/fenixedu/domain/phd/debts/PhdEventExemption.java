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
package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PhdEventExemption extends PhdEventExemption_Base {

    protected PhdEventExemption() {
        super();
    }

    public PhdEventExemption(final Person responsible, final PhdEvent event, final Money value,
            final PhdEventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

        this();
        super.init(responsible, event, createJustification(justificationType, dispatchDate, reason));
        String[] args = {};

        if (value == null) {
            throw new DomainException("error.PhdEventExemption.invalid.amount", args);
        }
        setValue(value);

        event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(PhdEventExemptionJustificationType justificationType,
            LocalDate dispatchDate, String reason) {
        return new PhdEventExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    private void checkRulesToDelete() {
        if (getEvent().hasAnyPayments()) {
            throw new DomainException("error.PhdEventExemption.cannot.delete.event.has.payments");
        }
    }

    @Atomic
    static public PhdEventExemption create(final Person responsible, final PhdEvent event, final Money value,
            final PhdEventExemptionJustificationType justificationType, final LocalDate dispatchDate, final String reason) {
        return new PhdEventExemption(responsible, event, value, justificationType, dispatchDate, reason);
    }

    @Override
    public boolean isPhdEventExemption() {
        return true;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

}
