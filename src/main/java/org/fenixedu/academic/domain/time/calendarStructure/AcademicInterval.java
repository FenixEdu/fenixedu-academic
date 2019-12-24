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
package org.fenixedu.academic.domain.time.calendarStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.base.AbstractInterval;

import pt.ist.fenixframework.FenixFramework;

public class AcademicInterval extends AbstractInterval implements Serializable {

    private static final String RESUMED_SEPARATOR = "_";

    private static final String FULL_SEPARATOR = ":";

    private static final String NAME_SEPARATOR = " - ";

    public static final Comparator<AcademicInterval> COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicInterval>() {

        @Override
        public int compare(AcademicInterval o1, AcademicInterval o2) {
            final int c = o1.getBeginYearMonthDayWithoutChronology().compareTo(o2.getBeginYearMonthDayWithoutChronology());
            return c == 0 ? o2.getEndDateTimeWithoutChronology().compareTo(o1.getEndDateTimeWithoutChronology()) : c;
        }

    };

    public static final Comparator<AcademicInterval> REVERSE_COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicInterval>() {

        @Override
        public int compare(AcademicInterval o1, AcademicInterval o2) {
            final int c = o2.getBeginYearMonthDayWithoutChronology().compareTo(o1.getBeginYearMonthDayWithoutChronology());
            return c == 0 ? o1.getEndDateTimeWithoutChronology().compareTo(o2.getEndDateTimeWithoutChronology()) : c;
        }

    };

    private final AcademicCalendarEntry academicCalendarEntry;
    private final AcademicCalendarRootEntry academicCalendarRootEntry;

    private AcademicInterval(String entryExternalId, String academicCalendarExternalId) {
        this((AcademicCalendarEntry) FenixFramework.getDomainObject(entryExternalId),
                (AcademicCalendarRootEntry) FenixFramework.getDomainObject(academicCalendarExternalId));
    }

    public AcademicInterval(final AcademicCalendarEntry entry, final AcademicCalendarRootEntry rootEntry) {
        if (entry == null) {
            throw new DomainException("error.AcademicInterval.empty.entry.externalId");
        }
        if (rootEntry == null) {
            throw new DomainException("error.AcademicInterval.empty.academic.chronology.externalId");
        }
        this.academicCalendarEntry = entry;
        this.academicCalendarRootEntry = rootEntry;
    }

    public AcademicChronology getAcademicChronology() {
        return (AcademicChronology) getChronology();
    }

    @Override
    public Chronology getChronology() {
        return getAcademicCalendar().getAcademicChronology();
    }

    @Override
    public long getStartMillis() {
        return getAcademicCalendarEntry().getBegin().getMillis();
    }

    @Override
    public long getEndMillis() {
        return getAcademicCalendarEntry().getEnd().getMillis();
    }

    public String getPresentationName() {
        return getAcademicCalendarEntry().getPresentationName();
    }

    public String getPathName() {
        String result = "";

        AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntry();
        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
            result += academicCalendarEntry.getTitle().getContent() + NAME_SEPARATOR;
            academicCalendarEntry = academicCalendarEntry.getParentEntry();
        }

        if (result.endsWith(NAME_SEPARATOR)) {
            result = result.substring(0, result.length() - 3);
        }

        return result;
    }

    public AcademicCalendarEntry getAcademicCalendarEntryInIntervalChronology() {
        return getAcademicChronology().findSameEntry(getAcademicCalendarEntry());
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {
        return academicCalendarRootEntry;
    }

    public AcademicCalendarEntry getAcademicCalendarEntry() {
        return academicCalendarEntry;
    }

    public YearMonthDay getBeginYearMonthDayWithoutChronology() {
        return getAcademicCalendarEntry().getBegin().toYearMonthDay();
    }

    public YearMonthDay getEndYearMonthDayWithoutChronology() {
        return getAcademicCalendarEntry().getEnd().toYearMonthDay();
    }

    public DateTime getStartDateTimeWithoutChronology() {
        return getAcademicCalendarEntry().getBegin();
    }

    public DateTime getEndDateTimeWithoutChronology() {
        return getAcademicCalendarEntry().getEnd();
    }

    public boolean isEqualOrEquivalent(AcademicInterval interval) {
        return getAcademicCalendarEntry().isEqualOrEquivalent(interval.getAcademicCalendarEntry());
    }

    public String getEntryExternalId() {
        return getAcademicCalendarEntry().getExternalId();
    }

    public String getAcademicCalendarExternalId() {
        return getAcademicCalendar().getExternalId();
    }

    public String getRepresentationInStringFormat() {
        return getEntryExternalId() + FULL_SEPARATOR + getAcademicCalendarExternalId();
    }

    public static AcademicInterval getAcademicIntervalFromString(String representationInStringFormat) {
        String[] split = representationInStringFormat.split(FULL_SEPARATOR);
        String entryExternalId = split[0];
        String academicCalendarExternalId = split[1];
        return new AcademicInterval(entryExternalId, academicCalendarExternalId);
    }

    public String getResumedRepresentationInStringFormat() {
        return getEntryExternalId() + RESUMED_SEPARATOR + getAcademicCalendarExternalId();
    }

    public static AcademicInterval getAcademicIntervalFromResumedString(String representationInStringFormat) {
        String[] split = representationInStringFormat.split(RESUMED_SEPARATOR);
        String entryExternalId = split[0];
        String academicCalendarExternalId = split[1];
        return new AcademicInterval(entryExternalId, academicCalendarExternalId);
    }

    // Operations for get periods.

//    public int getAcademicSemesterOfAcademicYear() {
//        return getAcademicCalendarEntry().getAcademicSemesterOfAcademicYear(getAcademicChronology());
//    }

//    public AcademicSemesterCE plusSemester(int amount) {
//        int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
//        return getAcademicChronology().getAcademicSemesterIn(index + amount);
//    }
//
//    public AcademicSemesterCE minusSemester(int amount) {
//        int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
//        return getAcademicChronology().getAcademicSemesterIn(index - amount);
//    }

//    public AcademicYearCE plusYear(int amount) {
//        int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
//        return getAcademicChronology().getAcademicYearIn(index + amount);
//    }
//
//    public AcademicYearCE minusYear(int amount) {
//        int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
//        return getAcademicChronology().getAcademicYearIn(index - amount);
//    }

    // ///////

    public static AcademicInterval getDefaultAcademicInterval(List<AcademicInterval> academicIntervals) {
        DateTime now = new DateTime();

        AcademicInterval closest = null;
        for (AcademicInterval academicInterval : academicIntervals) {
            if (closest == null || Math.abs(academicInterval.getStart().getMillis() - now.getMillis()) > Math
                    .abs(closest.getStart().getMillis() - now.getMillis())) {
                closest = academicInterval;
            }
        }
        return closest;
    }

    public static AcademicInterval readDefaultAcademicInterval(AcademicPeriod academicPeriod) {
        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            return ExecutionSemester.findCurrent(null).getAcademicInterval();
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            return ExecutionYear.findCurrent(null).getAcademicInterval();
        }

        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    @Deprecated
    public static List<AcademicInterval> readAcademicIntervals(AcademicPeriod academicPeriod) {
        Bennu rootDomainObject = Bennu.getInstance();

        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionSemester semester : rootDomainObject.getExecutionPeriodsSet()) {
                result.add(semester.getAcademicInterval());
            }

            return result;
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionYear executionYear : rootDomainObject.getExecutionYearsSet()) {
                result.add(executionYear.getAcademicInterval());
            }

            return result;
        }
        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    @Deprecated
    public static List<AcademicInterval> readActiveAcademicIntervals(AcademicPeriod academicPeriod) {
        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionSemester semester : ExecutionSemester.readNotClosedExecutionPeriods()) {
                result.add(semester.getAcademicInterval());
            }

            return result;
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
                result.add(executionYear.getAcademicInterval());
            }

            return result;
        }
        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    public AcademicInterval getNextAcademicInterval() {
        AcademicCalendarEntry nextAcademicCalendarEntry = getAcademicCalendarEntry().getNextAcademicCalendarEntry();
        if (nextAcademicCalendarEntry != null) {
            return new AcademicInterval(nextAcademicCalendarEntry, getAcademicCalendarEntry().getRootEntry());
        }

        return null;
    }

    public AcademicInterval getPreviousAcademicInterval() {
        AcademicCalendarEntry previousAcademicCalendarEntry = getAcademicCalendarEntry().getPreviousAcademicCalendarEntry();
        if (previousAcademicCalendarEntry != null) {
            return new AcademicInterval(previousAcademicCalendarEntry, getAcademicCalendarEntry().getRootEntry());
        }

        return null;
    }
}
