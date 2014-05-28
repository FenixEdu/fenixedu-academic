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
package net.sourceforge.fenixedu.domain.residence;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

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
        for (ResidenceEvent event : getEvents()) {
            if (event.getPerson() == person && (event.isOpen() || event.isPayed())) {
                return true;
            }
        }
        return false;
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
        return getEventsSet().size() == 0;
    }

    public Set<ResidenceEvent> getEventsWithPaymentCodes() {
        Set<ResidenceEvent> eventsWithCodes = new HashSet<ResidenceEvent>();

        for (ResidenceEvent event : getEvents()) {
            if (event.getAllPaymentCodes().size() > 0 && !event.isCancelled()) {
                eventsWithCodes.add(event);
            }
        }
        return eventsWithCodes;
    }

    public Set<ResidenceEvent> getEventsWithoutPaymentCodes() {
        Set<ResidenceEvent> eventsWithoutCodes = new HashSet<ResidenceEvent>();

        for (ResidenceEvent event : getEvents()) {
            if (event.getAllPaymentCodes().size() == 0 && !event.isCancelled()) {
                eventsWithoutCodes.add(event);
            }
        }
        return eventsWithoutCodes;
    }

    public boolean isFor(int year) {
        return getYear().isFor(year);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ResidenceEvent> getEvents() {
        return getEventsSet();
    }

    @Deprecated
    public boolean hasAnyEvents() {
        return !getEventsSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMonth() {
        return getMonth() != null;
    }

}
