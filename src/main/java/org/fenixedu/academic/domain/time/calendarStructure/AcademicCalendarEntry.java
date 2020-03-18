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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DayType;
import org.fenixedu.academic.util.renderer.GanttDiagramEvent;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent {

    public static final Comparator<AcademicCalendarEntry> COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicCalendarEntry>() {

        @Override
        public int compare(final AcademicCalendarEntry o1, final AcademicCalendarEntry o2) {
            int c1 = o1.getBegin().compareTo(o2.getBegin());
            return c1 == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c1;
        }

    };

    protected AcademicCalendarEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public AcademicCalendarEntry edit(LocalizedString title, LocalizedString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntry) {

        if (isRoot() || rootEntry == null) {
            throw new DomainException("error.unsupported.operation");
        }

        setTitle(title);
        setDescription(description);
        setTimeInterval(begin, end);

        return this;
    }

    protected void initEntry(AcademicCalendarEntry parentEntry, LocalizedString title, LocalizedString description,
            DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        if (rootEntry == null || parentEntry == null) {
            throw new DomainException("error.unsupported.operation");
        }

        setParentEntry(parentEntry);
        setTitle(title);
        setDescription(description);
        setTimeInterval(begin, end);
    }

    protected abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);

    protected abstract boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry);

    public void delete(AcademicCalendarRootEntry rootEntry) {
        if (!canBeDeleted(rootEntry)) {
            throw new DomainException("error.now.its.impossible.delete.entry.but.in.the.future.will.be.possible");
        }
        
        ExecutionInterval executionInterval = getExecutionInterval();
        if (executionInterval != null) {
            executionInterval.delete();
        }        
        
        super.setParentEntry(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private boolean canBeDeleted(AcademicCalendarRootEntry rootEntry) {
        if (!getRootEntry().equals(rootEntry)) {
            throw new DomainException("error.AcademicCalendarEntry.different.rootEntry");
        }
        if (!getChildEntriesSet().isEmpty()) {
            throw new DomainException("error.AcademicCalendarEntry.has.childs");
        }
        return true;
    }

    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
        if (parentEntry == null) {
            throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry");
        }
        if (isParentEntryInvalid(parentEntry)) {
            throw new DomainException("error.AcademicCalendarEntry.invalid.parent.entry", getClass().getSimpleName(),
                    parentEntry.getClass().getSimpleName());
        }
        if (parentEntry.exceededNumberOfChildEntries(this)) {
            throw new DomainException("error.AcademicCalendarEntry.number.of.subEntries.exceeded");
        }
        super.setParentEntry(parentEntry);
    }

    @Override
    public void setBegin(DateTime begin) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    public void setEnd(DateTime end) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    public void setTitle(LocalizedString title) {
        if (title == null || title.isEmpty()) {
            throw new DomainException("error.AcademicCalendarEntry.empty.title");
        }
        super.setTitle(title);
    }

    private void setTimeInterval(DateTime begin, DateTime end) {

        if (begin == null) {
            throw new DomainException("error.AcademicCalendarEntry.empty.begin.dateTime");
        }
        if (end == null) {
            throw new DomainException("error.AcademicCalendarEntry.empty.end.dateTime");
        }
        if (!end.isAfter(begin)) {
            throw new DomainException("error.begin.after.end");
        }

        super.setBegin(begin);
        super.setEnd(end);

        GenericPair<DateTime, DateTime> maxAndMinDateTimes = getChildMaxAndMinDateTimes();
        if (maxAndMinDateTimes != null
                && (getBegin().isAfter(maxAndMinDateTimes.getLeft()) || getEnd().isBefore(maxAndMinDateTimes.getRight()))) {
            throw new DomainException("error.AcademicCalendarEntry.out.of.bounds");
        }

        refreshParentTimeInterval();
    }

    private void refreshParentTimeInterval() {

        AcademicCalendarEntry parentEntry = getParentEntry();

        if (!parentEntry.isRoot()) {

            GenericPair<DateTime, DateTime> childMaxAndMinDateTimes = parentEntry.getChildMaxAndMinDateTimes();

            if (childMaxAndMinDateTimes != null) {

                DateTime begin = childMaxAndMinDateTimes.getLeft();
                DateTime end = childMaxAndMinDateTimes.getRight();

                boolean changeBeginDate = !parentEntry.getBegin().isBefore(begin);
                boolean changeEndDate = !parentEntry.getEnd().isAfter(end);

                if (!parentEntry.isRoot()) {

                    if (changeBeginDate || changeEndDate) {

                        DateTime beginDate = changeBeginDate ? begin : parentEntry.getBegin();
                        DateTime endDate = changeEndDate ? end : parentEntry.getEnd();

                        parentEntry.setTimeInterval(beginDate, endDate);
                    }

                } else if (changeBeginDate || changeEndDate) {
                    throw new DomainException("error.AcademicCalendarEntry.impossible.refresh.time.interval",
                            getClass().getName());
                }
            }
        }
    }

    private GenericPair<DateTime, DateTime> getChildMaxAndMinDateTimes() {

        Set<AcademicCalendarEntry> childEntries = getChildEntriesSet();

        if (!childEntries.isEmpty()) {

            DateTime begin = null;
            DateTime end = null;

            for (AcademicCalendarEntry entry : childEntries) {
                if (begin == null || entry.getBegin().isBefore(begin)) {
                    begin = entry.getBegin();
                }
                if (end == null || entry.getEnd().isAfter(end)) {
                    end = entry.getEnd();
                }
            }

            return new GenericPair<DateTime, DateTime>(begin, end);

        } else {
            return null;
        }
    }

    public List<AcademicCalendarEntry> getFullPath() {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        result.add(this);
        AcademicCalendarEntry parentEntry = getParentEntry();
        while (parentEntry != null) {
            result.add(0, parentEntry);
            parentEntry = parentEntry.getParentEntry();
        }
        return result;
    }

    public String getPresentationTimeInterval() {
        if (!isRoot()) {
            return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
        } else {
            DateTime begin = getBegin();
            return begin != null ? begin.toString("dd-MM-yyyy HH:mm") + " - " + "**-**-**** **:**" : "";
        }
    }

    public LocalizedString getType() {
        LocalizedString type = new LocalizedString();
        String key = "label." + getClass().getSimpleName() + ".type";
        type = type.with(org.fenixedu.academic.util.LocaleUtils.PT,
                BundleUtil.getString(Bundle.MANAGER, new Locale("pt", "PT"), key));
        return type;
    }

    public String getPresentationName() {
        return getTitle().getContent() + " - " + "[" + getType().getContent() + "]";
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {
        return getRootEntry();
    }

    public AcademicCalendarRootEntry getRootEntry() {
        if (isRoot()) {
            return (AcademicCalendarRootEntry) this;
        }
        return getParentEntry().getRootEntry();
    }

    public AcademicChronology getAcademicChronology() {
        return getRootEntry().getAcademicChronology();
    }

//    // renamed from getChildEntriesWithTemplateEntries
//    protected List<AcademicCalendarEntry> getChildEntries(Class<? extends AcademicCalendarEntry> subEntryClass) {
//        if (subEntryClass == null) {
//            return Collections.emptyList();
//        }
//        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
//        getChildEntries(null, allChildEntries, null, null, subEntryClass);
//        return allChildEntries;
//    }

    protected List<AcademicCalendarEntry> getChildEntries(final AcademicPeriod subEntryAcademicPeriod) {
        return getChildEntriesSet().stream().filter(e -> e.getAcademicPeriod().equals(subEntryAcademicPeriod))
                .collect(Collectors.toList());
    }

    public List<AcademicCalendarEntry> getAllChildEntries(final AcademicPeriod subEntryAcademicPeriod) {
        if (subEntryAcademicPeriod == null) {
            return Collections.emptyList();
        }
        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
        for (AcademicCalendarEntry child : getChildEntriesSet()) {
            if (subEntryAcademicPeriod.equals(child.getAcademicPeriod())) {
                allChildEntries.add(child);
            }
            allChildEntries.addAll(child.getAllChildEntries(subEntryAcademicPeriod));
        }
        return allChildEntries;
    }

//    // renamed from getAllChildEntriesWithTemplateEntries
//    public List<AcademicCalendarEntry> getAllChildEntries(Class<? extends AcademicCalendarEntry> subEntryClass) {
//        if (subEntryClass == null) {
//            return Collections.emptyList();
//        }
//        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
//        getChildEntries(null, allChildEntries, null, null, subEntryClass);
//        for (AcademicCalendarEntry child : getChildEntriesSet()) {
//            allChildEntries.addAll(child.getAllChildEntries(subEntryClass));
//        }
//        return allChildEntries;
//    }

//    // renamed from getChildEntriesWithTemplateEntries    
//    public List<AcademicCalendarEntry> getChildEntries(DateTime begin, DateTime end,
//            Class<? extends AcademicCalendarEntry> subEntryClass) {
//        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
//        getChildEntries(null, result, begin, end, subEntryClass);
//        return result;
//    }

//    private List<AcademicCalendarEntry> getChildEntries(Long instant, List<AcademicCalendarEntry> result,
//            Class<? extends AcademicCalendarEntry> subEntryClass) {
//
//        for (AcademicCalendarEntry subEntry : getChildEntriesSet()) {
//            if ((subEntryClass == null || subEntry.getClass().equals(subEntryClass))
//                    && (instant == null || subEntry.containsInstant(instant))) {
//
//                result.add(subEntry);
//            }
//        }
//
//        return result;
//    }

    // renamed from getFirstChildEntriesWithTemplateEntries    
//    protected void getFirstChildEntries(Long instant, Class<? extends AcademicCalendarEntry> subEntryClass,
//            Class<? extends AcademicCalendarEntry> parentEntryClass, List<AcademicCalendarEntry> childrenEntriesList) {
//
//        if (getClass().equals(parentEntryClass)) {
//            getChildEntries(instant, childrenEntriesList, subEntryClass);
//
//        } else {
//            for (AcademicCalendarEntry subEntry : getChildEntriesSet()) {
//                if (instant == null || subEntry.containsInstant(instant)) {
//                    subEntry.getFirstChildEntries(instant, subEntryClass, parentEntryClass, childrenEntriesList);
//                }
//            }
//        }
//    }

//    public AcademicCalendarEntry getEntryForCalendar(final AcademicCalendarRootEntry academicCalendar) {
//        return getRootEntry().equals(academicCalendar) ? this : null;
//    }

//    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
//        return getBegin().withChronology(academicChronology)
//                .get(AcademicSemesterOfAcademicYearDateTimeFieldType.academicSemesterOfAcademicYear());
//    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
        return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public boolean containsInstant(long instant) {
        return getBegin().getMillis() <= instant && getEnd().getMillis() >= instant;
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
        List<Interval> result = new ArrayList<Interval>();
        result.add(new Interval(getBegin(), getEnd()));
        return result;
    }

    @Override
    public LocalizedString getGanttDiagramEventName() {
        return getTitle();
    }

    @Override
    public int getGanttDiagramEventOffset() {
        if (getParentEntry() == null) {
            return 0;
        }
        return getParentEntry().getGanttDiagramEventOffset() + 1;
    }

    @Override
    public String getGanttDiagramEventObservations() {
        return "-";
    }

    @Override
    public String getGanttDiagramEventPeriod() {
        return getBegin().toString("dd/MM/yyyy HH:mm") + " - " + getEnd().toString("dd/MM/yyyy HH:mm");
    }

    @Override
    public String getGanttDiagramEventIdentifier() {
        return getExternalId().toString();
    }

    @Override
    public Integer getGanttDiagramEventMonth() {
        return null;
    }

    @Override
    public String getGanttDiagramEventUrlAddOns() {
        return null;
    }

    @Override
    public boolean isGanttDiagramEventIntervalsLongerThanOneDay() {
        return false;
    }

    @Override
    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays() {
        return false;
    }

    @Override
    public DayType getGanttDiagramEventDayType(Interval interval) {
        return null;
    }

    public boolean isOfType(AcademicPeriod period) {
        return period != null && period.equals(getAcademicPeriod());
    }

    public boolean isAcademicYear() {
        return false;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isEqualOrEquivalent(AcademicCalendarEntry entry) {
        return this.equals(entry);
    }

    public AcademicCalendarEntry getNextAcademicCalendarEntry() {
        AcademicCalendarEntry closest = null;
        for (AcademicCalendarEntry entry : this.getRootEntry().getAllChildEntries(this.getAcademicPeriod())) {
            if (entry.getBegin().isAfter(this.getBegin())) {
                if (closest == null || entry.getBegin().isBefore(closest.getBegin())) {
                    closest = entry;
                }
            }
        }
        return closest;
    }

    public AcademicCalendarEntry getPreviousAcademicCalendarEntry() {
        AcademicCalendarEntry closest = null;
        for (AcademicCalendarEntry entry : this.getRootEntry().getAllChildEntries(this.getAcademicPeriod())) {
            if (entry.getBegin().isBefore(this.getBegin())) {
                if (closest == null || entry.getBegin().isAfter(closest.getBegin())) {
                    closest = entry;
                }
            }
        }
        return closest;
    }

    /**
     * @return The cardinality of this entry relatively to its parent,
     *         counting entries of same period type and sorted by begin date.
     *         The first entry has cardinality of 1
     */
    public int getCardinality() {
        final AcademicPeriod academicPeriod = getAcademicPeriod();

        // not using directly getChildEntries(academicPeriod) in order optimize performance avoiding unecessary list collect, because this method will be invoked intensively
        return getParentEntry() != null ? getParentEntry().getChildEntriesSet().stream()
                .filter(e -> e.getAcademicPeriod().equals(academicPeriod)).sorted(COMPARATOR_BY_BEGIN_DATE)
                .collect(Collectors.toList()).indexOf(this) + 1 : 0;
    }

//    public int getCardinalityOfCalendarEntry(AcademicCalendarEntry child) {
//        int count = 1;
//        for (AcademicCalendarEntry entry : getChildEntries(child.getClass())) {
//            if (entry.getBegin().isBefore(child.getBegin())) {
//                count++;
//            }
//        }
//        return count;
//    }

    public static AcademicCalendarEntry findDefaultCalendar() {
        return Bennu.getInstance().getDefaultAcademicCalendar();
    }

}
