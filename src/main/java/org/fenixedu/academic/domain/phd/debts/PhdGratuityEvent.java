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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.DueDateAmountMap;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PhdGratuityEvent extends PhdGratuityEvent_Base {
    public PhdGratuityEvent(PhdIndividualProgramProcess process, int year, DateTime phdGratuityDate) {
        super();
        init(EventType.PHD_GRATUITY, process.getPerson(), year, process, phdGratuityDate);
    }

    public boolean hasExemptionsOfType(Class cl) {
        for (Exemption exemption : getExemptionsSet()) {
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
        persistDueDateAmountMap();
    }

    @Override
    protected PhdProgram getPhdProgram() {
        return getPhdIndividualProgramProcess().getPhdProgram();
    }

    @Atomic
    static public PhdGratuityEvent create(PhdIndividualProgramProcess phdIndividualProgramProcess, int year,
            DateTime phdGratuityDate) {
        final LocalDate whenStartedStudies = phdIndividualProgramProcess.getWhenStartedStudies();
        if (whenStartedStudies != null) {
            final DateTime dt = new LocalDate().toDateTimeAtStartOfDay().plusDays(1);
            final LocalDate initialDateForEventCreation = PhdGratuityEvent.initialDateForEventCreation(phdIndividualProgramProcess, dt);
            if (initialDateForEventCreation != null && year >= initialDateForEventCreation.getYear()) {
                final PhdProgramProcessState stateForToday = findStateForDate(phdIndividualProgramProcess, dt);
                final PhdIndividualProgramProcessState type = stateForToday.getType();
                if (type == PhdIndividualProgramProcessState.WORK_DEVELOPMENT || type == PhdIndividualProgramProcessState.SUSPENDED) {
                    final PhdGratuityEvent result = new PhdGratuityEvent(phdIndividualProgramProcess, year, dt);
                    result.fixDueDate(result.getWhenOccured().plusYears(1).minusDays(1).toLocalDate());
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    protected LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
        LabelFormatter labelFormatter = new LabelFormatter();

        final ExecutionYear executionYear = getPhdIndividualProgramProcess().getExecutionYear();
        return labelFormatter.appendLabel(getEventType().getQualifiedName(), Bundle.ENUMERATION).appendLabel(" - ")
                .appendLabel("" + getYear()).appendLabel(" (").appendLabel(getPhdProgram().getName(executionYear).getContent())
                .appendLabel(")");
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
    public boolean isGratuity() {
        return true;
    }

    @Override
    public boolean isToApplyInterest() {
        return true;
    }

    @Override
    public boolean isTransferable() { return isOpen()  && !hasExternalScholarshipGratuityExemption(); }

    public boolean hasExternalScholarshipGratuityExemption() {
        return hasExemptionsOfType(PhdGratuityExternalScholarshipExemption.class);
    }

    @Override public EntryType getEntryType() {
        return EntryType.GRATUITY_FEE;
    }

    @Override
    public Map<LocalDate, Money> calculateDueDateAmountMap() {
        return Collections.singletonMap(getLimitDateToPay().toLocalDate(), getPostingRule().calculateTotalAmountToPay(this));
    }

    private static final Comparator<? super PhdProgramProcessState> STATE_COMPARATOR = Comparator
            .comparing(PhdProgramProcessState::getWhenCreated)
            .thenComparing(PhdProgramProcessState::getExternalId);

    public static LocalDate initialDateForEventCreation(final PhdIndividualProgramProcess phdIndividualProgramProcess, final DateTime today) {
        return phdIndividualProgramProcess.getStatesSet().stream().sorted(STATE_COMPARATOR)
                .filter(s -> s.getType() == PhdIndividualProgramProcessState.WORK_DEVELOPMENT || s.getType() == PhdIndividualProgramProcessState.SUSPENDED)
                .filter(s -> !s.getStateDate().isAfter(today))
                .map(s -> s.getStateDate().toLocalDate())
                .max((d1, d2) -> d1.compareTo(d2)).orElse(null);
    }

    public static PhdProgramProcessState findStateForDate(final PhdIndividualProgramProcess phdIndividualProgramProcess, final DateTime tomorrow) {
        PhdProgramProcessState result = null;
        for (final PhdProgramProcessState state : phdIndividualProgramProcess.getStatesSet().stream().sorted(STATE_COMPARATOR).collect(Collectors.toList())) {
            if (!state.getStateDate().isAfter(tomorrow)) {
                result = state;
            }
        }
        return result;
    }

    private void fixDueDate(final LocalDate dueDate) {
        final Map<LocalDate,Money> map = new HashMap<>();
        final Map.Entry<LocalDate, Money> e = getDueDateAmountMap().entrySet().iterator().next();
        map.put(dueDate, e.getValue());
        setDueDateAmountMap(new DueDateAmountMap(map));
    }

}
