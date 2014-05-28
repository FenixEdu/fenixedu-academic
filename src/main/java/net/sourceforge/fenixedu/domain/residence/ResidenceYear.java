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

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ResidenceYear extends ResidenceYear_Base {

    public ResidenceYear(ResidenceManagementUnit residenceManagementUnit) {
        this(getNextYear(), residenceManagementUnit);
    }

    public ResidenceYear(Integer year, ResidenceManagementUnit residenceManagementUnit) {
        super();
        setYear(year);
        setUnit(residenceManagementUnit);
        setRootDomainObject(Bennu.getInstance());
        for (Month month : Month.values()) {
            new ResidenceMonth(month, this);
        }
    }

    public Set<ResidenceMonth> getSortedMonths() {
        TreeSet<ResidenceMonth> months = new TreeSet<ResidenceMonth>(new BeanComparator("month"));
        months.addAll(getMonths());
        return months;
    }

    private static Integer getNextYear() {
        Integer next = null;
        for (ResidenceYear year : Bennu.getInstance().getResidenceYearsSet()) {
            if (next == null || year.getYear() > next) {
                next = year.getYear();
            }
        }

        return next != null ? next + 1 : new DateTime().getYear();
    }

    public static ResidenceYear getCurrentYear() {
        Integer currentYear = new LocalDate().getYear();
        for (ResidenceYear year : Bennu.getInstance().getResidenceYearsSet()) {
            if (year.getYear().equals(currentYear)) {
                return year;
            }
        }
        return null;
    }

    public static boolean hasCurrentYear() {
        return getCurrentYear() != null;
    }

    public boolean isFor(int year) {
        return getYear().intValue() == year;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidenceMonth> getMonths() {
        return getMonthsSet();
    }

    @Deprecated
    public boolean hasAnyMonths() {
        return !getMonthsSet().isEmpty();
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
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
