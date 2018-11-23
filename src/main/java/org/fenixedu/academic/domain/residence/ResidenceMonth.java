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
package org.fenixedu.academic.domain.residence;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.organizationalStructure.ResidenceManagementUnit;
import org.fenixedu.academic.util.Month;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ResidenceMonth extends ResidenceMonth_Base {

    protected ResidenceMonth() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ResidenceMonth(Month month, ResidenceYear residenceYear) {
        this();
        setMonth(month);
        setYear(residenceYear);
    }

    public ResidenceManagementUnit getManagementUnit() {
        return getYear().getUnit();
    }

    public boolean isEventPresent(Person person) {
        return getEventsSet().stream().anyMatch(event -> event.getPerson() == person && (event.isOpen() || event.isPayed()));
    }

    public DateTime getPaymentStartDate() {
        LocalDate date = new LocalDate(getYear().getYear(), getMonth().getNumberOfMonth(), 1);
        return date.toDateTimeAtStartOfDay();
    }

    public DateTime getPaymentLimitDateTime() {
        ResidenceYear residenceYear = getYear();
        LocalDate date =
                new LocalDate(residenceYear.getYear(), getMonth().getNumberOfMonth(), getManagementUnit()
                        .getCurrentPaymentLimitDay());
        return date.toDateTimeAtStartOfDay();
    }

    public boolean isAbleToEditPaymentLimitDate() {
        return getEventsSet().isEmpty();
    }

    public Set<ResidenceEvent> getEventsWithPaymentCodes() {

        return getEventsSet().stream().filter(event -> !event.getAllPaymentCodes().isEmpty() && !event.isCancelled())
                .collect(Collectors.toSet());
    }

    public Set<ResidenceEvent> getEventsWithoutPaymentCodes() {

        return getEventsSet().stream().filter(event -> event.getAllPaymentCodes().isEmpty() && !event.isCancelled())
                .collect(Collectors.toSet());
    }

    public boolean isFor(int year) {
        return getYear().isFor(year);
    }

}
