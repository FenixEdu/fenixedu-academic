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
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdGratuityEvent extends PhdGratuityEvent_Base {
    public PhdGratuityEvent(PhdIndividualProgramProcess process, int year, DateTime phdGratuityDate) {
        super();
        if (process.hasPhdGratuityEventForYear(year)) {
            throw new DomainException("error.PhdRegistrationFee.process.already.has.registration.fee.for.this.year");
        }
        init(EventType.PHD_GRATUITY, process.getPerson(), year, process, phdGratuityDate);
    }

    public boolean hasExemptionsOfType(Class cl) {
        for (Exemption exemption : getExemptions()) {
            if (cl.isAssignableFrom(exemption.getClass())) {
                return true;
            }
        }

        return false;
    }

    protected void init(EventType eventType, Person person, int year, PhdIndividualProgramProcess process,
            DateTime phdGratuityDate) {
        super.init(eventType, person);
        if (year < 2006) {
            throw new DomainException("invalid.year");
        }

        if (process == null) {
            throw new DomainException("proces.may.not.be.null");
        }

        if (phdGratuityDate == null) {
            throw new DomainException("phdGratuityDate.may.not.be.null");
        }

        setYear(year);
        setPhdIndividualProgramProcess(process);
        setPhdGratuityDate(phdGratuityDate);
    }

    @Override
    protected PhdProgram getPhdProgram() {
        return getPhdIndividualProgramProcess().getPhdProgram();
    }

    @Atomic
    static public PhdGratuityEvent create(PhdIndividualProgramProcess phdIndividualProgramProcess, int year,
            DateTime phdGratuityDate) {
        return new PhdGratuityEvent(phdIndividualProgramProcess, year, phdGratuityDate);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
        return new LabelFormatter().appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel("" + getYear())
                .appendLabel(" (").appendLabel(getPhdProgram().getName().getContent()).appendLabel(")");
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getEventType().getQualifiedName(), "enum").appendLabel(" - ")
                .appendLabel("" + getYear()).appendLabel(" (").appendLabel(getPhdProgram().getName().getContent())
                .appendLabel(")");
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public DateTime getLimitDateToPay() {
        LocalDate whenFormalizedRegistration = getPhdIndividualProgramProcess().getWhenFormalizedRegistration();

        PhdGratuityPaymentPeriod phdGratuityPeriod =
                ((PhdGratuityPR) getPostingRule()).getPhdGratuityPeriod(whenFormalizedRegistration);

        DateTime lastPaymentDay =
                new LocalDate(getYear(), phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.monthOfYear()),
                        phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.dayOfMonth())).toDateMidnight().toDateTime();
        DateTime endDay =
                new LocalDate(getYear(), phdGratuityPeriod.getEnd().get(DateTimeFieldType.monthOfYear()), phdGratuityPeriod
                        .getEnd().get(DateTimeFieldType.dayOfMonth())).toDateMidnight().toDateTime();

        if (lastPaymentDay.isBefore(endDay)) {
            return new LocalDate(getYear() + 1, phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.monthOfYear()),
                    phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.dayOfMonth())).toDateMidnight().toDateTime();
        } else {
            return lastPaymentDay;
        }

    }

    @Override
    public boolean isOpen() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).greaterThan(Money.ZERO);
    }

    @Override
    public boolean isClosed() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).lessOrEqualThan(Money.ZERO);
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasPhdGratuityDate() {
        return getPhdGratuityDate() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

}
