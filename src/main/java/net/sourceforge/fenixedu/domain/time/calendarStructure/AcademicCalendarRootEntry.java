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
package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AcademicCalendarRootEntry extends AcademicCalendarRootEntry_Base {

    private transient AcademicChronology academicChronology;

    public AcademicCalendarRootEntry(MultiLanguageString title, MultiLanguageString description,
            AcademicCalendarEntry templateCalendar) {
        super();
        setRootDomainObjectForRootEntries(Bennu.getInstance());
        setTitle(title);
        setDescription(description);
        setTemplateEntry(templateCalendar);
    }

    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntryDestination, AcademicCalendarEntry templateEntry) {

        setTitle(title);
        setDescription(description);
        setTemplateEntry(templateEntry);
        return this;
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
        super.setRootDomainObjectForRootEntries(null);
        super.delete(rootEntry);
    }

    @Override
    public AcademicChronology getAcademicChronology() {
        if (academicChronology == null) {
            synchronized (AcademicCalendarRootEntry.class) {
                if (academicChronology == null) {
                    academicChronology = new AcademicChronology(this);
                }
            }
        }
        return academicChronology;
    }

    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
        if (templateEntry != null && !getChildEntries().isEmpty()
                && (!hasTemplateEntry() || !getTemplateEntry().equals(templateEntry))) {
            throw new DomainException("error.RootEntry.invalid.templateEntry");
        }
        super.setTemplateEntry(templateEntry);
    }

    @Override
    public void setRootDomainObjectForRootEntries(Bennu rootDomainObjectForRootEntries) {
        if (rootDomainObjectForRootEntries == null) {
            throw new DomainException("error.RootEntry.empty.rootDomainObject.to.academic.calendars");
        }
        super.setRootDomainObjectForRootEntries(rootDomainObjectForRootEntries);
    }

    @Override
    public DateTime getBegin() {

        DateTime begin = null;
        Collection<AcademicCalendarEntry> result = null;

        if (!hasTemplateEntry()) {
            result = getChildEntries();
        } else {
            result = getChildEntriesWithTemplateEntries();
        }

        for (AcademicCalendarEntry entry : result) {
            if (begin == null || entry.getBegin().isBefore(begin)) {
                begin = entry.getBegin();
            }
        }

        return begin;
    }

    public AcademicCalendarEntry getEntryByInstant(long instant, Class<? extends AcademicCalendarEntry> entryClass,
            Class<? extends AcademicCalendarEntry> parentEntryClass) {
        AcademicCalendarEntry entryResult = null;
        for (AcademicCalendarEntry entry : getChildEntriesWithTemplateEntries(Long.valueOf(instant), entryClass, parentEntryClass)) {
            entryResult = (entryResult == null || entry.getBegin().isAfter(entryResult.getBegin())) ? entry : entryResult;
        }
        return entryResult;
    }

    public Integer getEntryIndexByInstant(long instant, Class<? extends AcademicCalendarEntry> entryClass,
            Class<? extends AcademicCalendarEntry> parentEntryClass) {
        Integer counter = null;
        for (AcademicCalendarEntry entry : getChildEntriesWithTemplateEntries(entryClass, parentEntryClass)) {
            if (entry.containsInstant(instant) || entry.getEnd().isBefore(instant)) {
                counter = counter == null ? 1 : counter.intValue() + 1;
            }
        }
        return counter;
    }

    public AcademicCalendarEntry getEntryByIndex(int index, Class<? extends AcademicCalendarEntry> entryClass,
            Class<? extends AcademicCalendarEntry> parentEntryClass) {
        List<AcademicCalendarEntry> allChildEntries = getChildEntriesWithTemplateEntries(entryClass, parentEntryClass);
        Collections.sort(allChildEntries, COMPARATOR_BY_BEGIN_DATE);
        return index > 0 && index <= allChildEntries.size() ? allChildEntries.get(index - 1) : null;
    }

    public static AcademicCalendarRootEntry getAcademicCalendarByTitle(String title) {
        for (AcademicCalendarRootEntry rootEntry : Bennu.getInstance().getAcademicCalendarsSet()) {
            if (rootEntry.getTitle().getContent().equals(title)) {
                return rootEntry;
            }
        }
        return null;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Class<? extends AcademicCalendarEntry> subEntryClass,
            Class<? extends AcademicCalendarEntry> parentEntryClass) {
        return getChildEntriesWithTemplateEntries(null, subEntryClass, parentEntryClass);
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant,
            Class<? extends AcademicCalendarEntry> subEntryClass, Class<? extends AcademicCalendarEntry> parentEntryClass) {
        if (subEntryClass == null || parentEntryClass == null) {
            return Collections.emptyList();
        }
        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
        getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass, allChildEntries);
        return allChildEntries;
    }

    @Override
    public DateTime getEnd() {
        return null;
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
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        if (entryToAdd.isAcademicYear()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return false;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        return false;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return true;
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
        return false;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public boolean containsInstant(final long instant) {
        return true;
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return false;
    }

    @Deprecated
    public boolean hasBennuForRootEntries() {
        return getRootDomainObjectForRootEntries() != null;
    }

    @Deprecated
    public boolean hasBennuForDefaultRootEntry() {
        return getRootDomainObjectForDefaultRootEntry() != null;
    }

}
