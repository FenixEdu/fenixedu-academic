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
package net.sourceforge.fenixedu.dataTransferObject.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.IFormation;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class YearMonth implements Serializable, IFormation {

    Integer year;

    Integer firstYear;

    Month month;

    public YearMonth() {
        super();
    }

    public YearMonth(int year, int month) {
        super();
        setYear(year);
        setMonth(Month.values()[month - 1]);
    }

    public YearMonth(LocalDate date) {
        super();
        setYear(date.getYear());
        setMonth(Month.values()[date.getMonthOfYear() - 1]);
    }

    public YearMonth(Partial date) {
        super();
        setYear(date.get(DateTimeFieldType.year()));
        setMonth(Month.values()[(date.get(DateTimeFieldType.monthOfYear()) - 1)]);
    }

    public YearMonth(Integer year, Month month) {
        setYear(year);
        setMonth(month);
    }

    public Month getMonth() {
        return month;
    }

    public int getNumberOfMonth() {
        return getMonth().getNumberOfMonth();
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getYearString() {
        return year.toString();
    }

    public void setYearString(String year) {
        this.year = Integer.valueOf(year);
    }

    @Override
    public int getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }

    public void addMonth() {
        if (getNumberOfMonth() == 12) {
            setMonth(Month.values()[0]);
            setYear(getYear() + 1);
        } else {
            setMonth(Month.values()[getNumberOfMonth()]);
        }
    }

    public void subtractMonth() {
        if (getNumberOfMonth() == 1) {
            setMonth(Month.values()[11]);
            setYear(getYear() - 1);
        } else {
            setMonth(Month.values()[getNumberOfMonth() - 2]);
        }
    }

    public Partial getPartial() {
        return new Partial().with(DateTimeFieldType.monthOfYear(), getNumberOfMonth()).with(DateTimeFieldType.year(), getYear());
    }

    @Override
    public boolean equals(Object obj) {
        return ((YearMonth) obj).getYear().equals(getYear()) && ((YearMonth) obj).getMonth().equals(getMonth());
    }

}
