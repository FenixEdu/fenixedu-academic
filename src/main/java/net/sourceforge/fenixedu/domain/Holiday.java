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
package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

public class Holiday extends Holiday_Base {

    public static abstract class HolidayFactory implements Serializable, FactoryExecutor {
        private Integer year;
        private Integer monthOfYear;
        private Integer dayOfMonth;

        public Integer getDayOfMonth() {
            return dayOfMonth;
        }

        public void setDayOfMonth(Integer dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public Integer getMonthOfYear() {
            return monthOfYear;
        }

        public void setMonthOfYear(Integer monthOfYear) {
            this.monthOfYear = monthOfYear;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }

    public static class HolidayFactoryCreator extends HolidayFactory {
        private Locality localityReference;

        public Locality getLocality() {
            return localityReference;
        }

        public void setLocality(Locality locality) {
            if (locality != null) {
                this.localityReference = locality;
            }
        }

        @Override
        public Holiday execute() {
            return new Holiday(this);
        }
    }

    public Holiday() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Holiday(final HolidayFactoryCreator creator) {
        this();

        final List<DateTimeFieldType> dateTimeFieldTypes = new ArrayList<DateTimeFieldType>();
        final List<Integer> dateTimeFieldValues = new ArrayList<Integer>();
        if (creator.getYear() != null) {
            dateTimeFieldTypes.add(DateTimeFieldType.year());
            dateTimeFieldValues.add(creator.getYear());
        }
        if (creator.getMonthOfYear() != null) {
            dateTimeFieldTypes.add(DateTimeFieldType.monthOfYear());
            dateTimeFieldValues.add(creator.getMonthOfYear());
        }
        if (creator.getDayOfMonth() != null) {
            dateTimeFieldTypes.add(DateTimeFieldType.dayOfMonth());
            dateTimeFieldValues.add(creator.getDayOfMonth());
        }

        final DateTimeFieldType[] dateTimeFieldTypesArray = new DateTimeFieldType[dateTimeFieldTypes.size()];
        final int[] dateTimeFieldValuesArray = new int[dateTimeFieldValues.size()];
        for (int i = 0; i < dateTimeFieldTypes.size(); i++) {
            dateTimeFieldTypesArray[i] = dateTimeFieldTypes.get(i);
            dateTimeFieldValuesArray[i] = dateTimeFieldValues.get(i).intValue();
        }
        final Partial partial = new Partial(dateTimeFieldTypesArray, dateTimeFieldValuesArray);

        setDate(partial);
        setLocality(creator.getLocality());
    }

    public void delete() {
        setLocality(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static boolean isHoliday(LocalDate date) {
        return isHoliday(date, null);
    }

    public static boolean isHoliday(LocalDate date, Space campus) {
        for (Holiday holiday : Bennu.getInstance().getHolidaysSet()) {
            if ((holiday.getLocality() == null || (campus != null && holiday.getLocality() == campus.getLocality()))
                    && holiday.getDate().isMatch(date)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLocality() {
        return getLocality() != null;
    }

    @Deprecated
    public boolean hasDate() {
        return getDate() != null;
    }

}
