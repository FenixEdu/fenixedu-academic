package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.util.DayType;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent {

    protected abstract AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry);

    protected abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);

    protected abstract boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry);

    protected abstract boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd);

    protected abstract boolean isPossibleToChangeTimeInterval();

    protected abstract boolean associatedWithDomainEntities();

    protected void afterRedefineEntry() {
    }

    protected void beforeRedefineEntry() {
    }

    private transient AcademicChronology academicChronology;

    public static final Comparator<AcademicCalendarEntry> COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicCalendarEntry>() {

        @Override
        public int compare(final AcademicCalendarEntry o1, final AcademicCalendarEntry o2) {
            int c1 = o1.getBegin().compareTo(o2.getBegin());
            return c1 == 0 ? AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c1;
        }

    };

    protected AcademicCalendarEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete(AcademicCalendarRootEntry rootEntry) {
        if (!canBeDeleted(rootEntry)) {
            throw new DomainException("error.now.its.impossible.delete.entry.but.in.the.future.will.be.possible");
        }
        getBasedEntries().clear();
        super.setParentEntry(null);
        super.setTemplateEntry(null);
        removeRootDomainObject();
        deleteDomainObject();
    }

    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
            AcademicCalendarRootEntry rootEntry, AcademicCalendarEntry templateEntry) {

        if (isRoot() || rootEntry == null) {
            throw new DomainException("error.unsupported.operation");
        }

        if (!rootEntry.equals(getRootEntry())) {

            if (getBegin().isEqual(begin) && getEnd().isEqual(end)) {
                throw new DomainException("error.AcademicCalendarEntry.unchanged.dates");
            }

            AcademicCalendarEntry newParentEntry = createVirtualPathUntil(getParentEntry(), rootEntry);
            AcademicCalendarEntry newEntry = createVirtualEntry(newParentEntry);
            newEntry.edit(title, description, begin, end, rootEntry, templateEntry);

            return newEntry;

        } else {

            boolean isRedefinedEntry = isVirtual();

            if (isRedefinedEntry) {
                beforeRedefineEntry();
            }

            setTitle(title);
            setDescription(description);
            setTimeInterval(begin, end);

            if (isRedefinedEntry) {
                afterRedefineEntry();
            }

            return this;
        }
    }

    protected void initEntry(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description,
            DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        if (rootEntry == null || parentEntry == null) {
            throw new DomainException("error.unsupported.operation");
        }

        parentEntry =
                !parentEntry.getRootEntry().equals(rootEntry) ? createVirtualPathUntil(parentEntry, rootEntry) : parentEntry;

        setParentEntry(parentEntry, false);
        setTitle(title);
        setDescription(description);
        setTimeInterval(begin, end);
    }

    protected void initVirtualEntry(AcademicCalendarEntry parentEntry, AcademicCalendarEntry templateEntry) {
        setParentEntry(parentEntry, true);
        setTemplateEntry(templateEntry);
    }

    private AcademicCalendarEntry createVirtualPathUntil(AcademicCalendarEntry entry,
            AcademicCalendarRootEntry rootEntryDestination) {

        if (!entry.isRoot()) {

            List<AcademicCalendarEntry> entryPath = entry.getFullPath();
            entryPath.remove(0);// remove root entry

            AcademicCalendarEntry parentEntry = rootEntryDestination;
            AcademicCalendarEntry virtualOrRedefinedEntry = rootEntryDestination;

            for (AcademicCalendarEntry entryToMakeCopy : entryPath) {
                if (virtualOrRedefinedEntry != null) {
                    virtualOrRedefinedEntry = entryToMakeCopy.getVirtualOrRedefinedEntryIn(rootEntryDestination);
                }
                if (virtualOrRedefinedEntry == null) {
                    parentEntry = entryToMakeCopy.createVirtualEntry(parentEntry);
                } else {
                    parentEntry = virtualOrRedefinedEntry;
                }
            }
            return parentEntry;

        } else {
            return rootEntryDestination;
        }
    }

    private AcademicCalendarEntry getVirtualOrRedefinedEntryIn(AcademicCalendarRootEntry rootEntry) {
        if (rootEntry != null) {
            List<AcademicCalendarEntry> basedEntries = getBasedEntries();
            for (AcademicCalendarEntry entry : basedEntries) {
                if (entry.getRootEntry().equals(rootEntry)) {
                    return entry;
                }
            }
        }
        return null;
    }

    private boolean canBeDeleted(AcademicCalendarRootEntry rootEntry) {
        if (!getRootEntry().equals(rootEntry)) {
            throw new DomainException("error.AcademicCalendarEntry.different.rootEntry");
        }
        if (!getChildEntries().isEmpty()) {
            throw new DomainException("error.AcademicCalendarEntry.has.childs");
        }
        return true;
    }

    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
        if (templateEntry != null && (!templateEntry.getClass().equals(getClass()) || getBasedEntries().contains(templateEntry))) {
            throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
        }
        if (!isRoot()) {
            AcademicCalendarRootEntry rootEntry = getRootEntry();
            if (rootEntry.getTemplateEntry() == null || !rootEntry.getTemplateEntry().equals(templateEntry.getRootEntry())) {
                throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
            }
        }
        super.setTemplateEntry(templateEntry);
    }

    protected void setParentEntry(AcademicCalendarEntry parentEntry, boolean virtualEntry) {
        if (parentEntry == null) {
            throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry");
        }
        if (isParentEntryInvalid(parentEntry)) {
            throw new DomainException("error.AcademicCalendarEntry.invalid.parent.entry", getClass().getSimpleName(), parentEntry
                    .getClass().getSimpleName());
        }
        if (!virtualEntry && parentEntry.exceededNumberOfChildEntries(this)) {
            throw new DomainException("error.AcademicCalendarEntry.number.of.subEntries.exceeded");
        }
        super.setParentEntry(parentEntry);
    }

    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
        throw new DomainException("error.unsupported.operation");
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
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            throw new DomainException("error.AcademicCalendarEntry.empty.title");
        }
        super.setTitle(title);
    }

    @Override
    public DateTime getBegin() {
        return isVirtual() ? getTemplateEntry().getBegin() : super.getBegin();
    }

    @Override
    public DateTime getEnd() {
        return isVirtual() ? getTemplateEntry().getEnd() : super.getEnd();
    }

    @Override
    public MultiLanguageString getTitle() {
        return isVirtual() && !isRoot() ? getTemplateEntry().getTitle() : super.getTitle();
    }

    @Override
    public MultiLanguageString getDescription() {
        return isVirtual() && !isRoot() ? getTemplateEntry().getDescription() : super.getDescription();
    }

    protected void setTimeInterval(DateTime begin, DateTime end) {

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

        if (!getParentEntry().areIntersectionsPossible(this)) {
            for (AcademicCalendarEntry childEntry : getParentEntry().getChildEntriesWithTemplateEntries(getClass())) {
                if (!childEntry.equals(this) && childEntry.entriesTimeIntervalIntersection(begin, end)) {
                    throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
                }
            }
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

                if (parentEntry.isPossibleToChangeTimeInterval()) {

                    if (changeBeginDate || changeEndDate) {

                        DateTime beginDate = changeBeginDate ? begin : parentEntry.getBegin();
                        DateTime endDate = changeEndDate ? end : parentEntry.getEnd();

                        parentEntry.edit(parentEntry.getTitle(), parentEntry.getDescription(), beginDate, endDate,
                                parentEntry.getRootEntry(), parentEntry.getTemplateEntry());

                    }

                } else if (changeBeginDate || changeEndDate) {
                    throw new DomainException("error.AcademicCalendarEntry.impossible.refresh.time.interval", getClass()
                            .getName());
                }
            }
        }
    }

    public GenericPair<DateTime, DateTime> getChildMaxAndMinDateTimes() {

        List<AcademicCalendarEntry> childEntries = getChildEntriesWithTemplateEntries();
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

    public AcademicCalendarEntry getNonVirtualTemplateEntry() {
        if (isVirtual()) {
            return getTemplateEntry().getNonVirtualTemplateEntry();
        }
        return this;
    }

    public AcademicCalendarEntry getOriginalTemplateEntry() {
        if (hasTemplateEntry()) {
            return getTemplateEntry().getOriginalTemplateEntry();
        }
        return this;
    }

    public boolean isRedefined() {
        return isVirtual();
        // return hasTemplateEntry() && super.getBegin() != null;
    }

    public boolean isVirtual() {
        return super.getBegin() == null && hasTemplateEntry();
    }

    public EntryState getEntryState() {
        return isVirtual() ? EntryState.VIRTUAL : EntryState.ORIGINAL;
        // return isVirtual() ? EntryState.VIRTUAL : isRedefined() ?
        // EntryState.REDEFINED : EntryState.ORIGINAL;
    }

    public static enum EntryState {

        VIRTUAL, REDEFINED, ORIGINAL;

        public String getName() {
            return name();
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

    public List<AcademicInterval> getFullPathInDeep() {
        List<AcademicInterval> result = new ArrayList<AcademicInterval>();
        getChildFullPathInDeep(result, getRootEntry());
        return result;
    }

    private void getChildFullPathInDeep(List<AcademicInterval> result, AcademicCalendarRootEntry rootEntry) {

        if (associatedWithDomainEntities()) {
            result.add(new AcademicInterval(this, rootEntry));
        }

        List<AcademicCalendarEntry> childEntries =
                getTemplateEntry() != null ? getChildEntriesWithTemplateEntries() : getChildEntries();
        for (AcademicCalendarEntry child : childEntries) {
            child.getChildFullPathInDeep(result, rootEntry);
        }
    }

    public String getPresentationTimeInterval() {
        if (!isRoot()) {
            return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
        } else {
            DateTime begin = getBegin();
            return begin != null ? begin.toString("dd-MM-yyyy HH:mm") + " - " + "**-**-**** **:**" : "";
        }
    }

    public MultiLanguageString getType() {
        MultiLanguageString type = new MultiLanguageString();
        String key = "label." + getClass().getSimpleName() + ".type";
        type =type.with(Language.pt,ResourceBundle.getBundle("resources/ManagerResources", new Locale("pt", "PT")).getString(key));
        return type;
    }

    public String getPresentationName() {
        return getTitle().getContent() + " - " + "[" + getType() + "]";
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
        if (academicChronology == null) {
            academicChronology = getRootEntry().getAcademicChronology();
        }
        return academicChronology;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries() {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(null, result, null, null, null);
        return result;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant) {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(instant, result, null, null, null);
        return result;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Class<? extends AcademicCalendarEntry> subEntryClass) {
        if (subEntryClass == null) {
            return Collections.emptyList();
        }
        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(null, allChildEntries, null, null, subEntryClass);
        return allChildEntries;
    }

    public List<AcademicCalendarEntry> getAllChildEntriesWithTemplateEntries(Class<? extends AcademicCalendarEntry> subEntryClass) {
        if (subEntryClass == null) {
            return Collections.emptyList();
        }
        List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(null, allChildEntries, null, null, subEntryClass);
        for (AcademicCalendarEntry child : getChildEntries()) {
            allChildEntries.addAll(child.getAllChildEntriesWithTemplateEntries(subEntryClass));
        }
        return allChildEntries;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntriesOrderByDate(DateTime begin, DateTime end) {
        List<AcademicCalendarEntry> result = getChildEntriesWithTemplateEntries(begin, end, null);
        Collections.sort(result, COMPARATOR_BY_BEGIN_DATE);
        return result;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(DateTime begin, DateTime end,
            Class<? extends AcademicCalendarEntry> subEntryClass) {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(null, result, begin, end, subEntryClass);
        return result;
    }

    private List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant, DateTime begin, DateTime end,
            Class<? extends AcademicCalendarEntry> subEntryClass) {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        getChildEntriesWithTemplateEntries(instant, result, begin, end, subEntryClass);
        return result;
    }

    protected List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant, List<AcademicCalendarEntry> result,
            DateTime begin, DateTime end, Class<? extends AcademicCalendarEntry> subEntryClass) {

        boolean hasTemplateEntry = hasTemplateEntry();
        List<AcademicCalendarEntry> templateEntries = hasTemplateEntry ? new ArrayList<AcademicCalendarEntry>() : null;

        for (AcademicCalendarEntry subEntry : getChildEntries()) {
            if ((subEntryClass == null || subEntry.getClass().equals(subEntryClass))
                    && (instant == null || subEntry.containsInstant(instant))
                    && (begin == null || subEntry.belongsToPeriod(begin, end))) {

                result.add(subEntry);
            }
            if (hasTemplateEntry && subEntry.hasTemplateEntry()) {
                templateEntries.add(subEntry.getTemplateEntry());
            }
        }

        if (hasTemplateEntry) {
            for (AcademicCalendarEntry entry : getTemplateEntry().getChildEntriesWithTemplateEntries(instant, begin, end,
                    subEntryClass)) {
                if (!templateEntries.contains(entry)) {
                    result.add(entry);
                }
            }
        }

        return result;
    }

    protected void getFirstChildEntriesWithTemplateEntries(Long instant, Class<? extends AcademicCalendarEntry> subEntryClass,
            Class<? extends AcademicCalendarEntry> parentEntryClass, List<AcademicCalendarEntry> childrenEntriesList) {

        if (getClass().equals(parentEntryClass)) {
            getChildEntriesWithTemplateEntries(instant, childrenEntriesList, null, null, subEntryClass);

        } else {
            if (!hasTemplateEntry()) {
                for (AcademicCalendarEntry subEntry : getChildEntries()) {
                    if (instant == null || subEntry.containsInstant(instant)) {
                        subEntry.getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass,
                                childrenEntriesList);
                    }
                }
            } else {
                for (AcademicCalendarEntry subEntry : getChildEntriesWithTemplateEntries(instant)) {
                    subEntry.getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass,
                            childrenEntriesList);
                }
            }
        }
    }

    /**
     * Computes a list of the children which type matches the AcademicPeriod in
     * order of a depth first search.
     * 
     * @param period
     *            The AcademicPeriod for type checking.
     * @return the list with the matching entries.
     */
    private List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(AcademicPeriod period) {
        List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
        if (isOfType(period)) {
            result.add(this);
        }
        List<AcademicCalendarEntry> children = new ArrayList<AcademicCalendarEntry>();
        children.addAll(getChildEntries());
        Collections.sort(children, COMPARATOR_BY_BEGIN_DATE);
        for (AcademicCalendarEntry child : children) {
            result.addAll(child.getChildEntriesWithTemplateEntries(period));
        }
        return result;
    }

    public AcademicCalendarEntry getChildAcademicCalendarEntry(AcademicPeriod period, int cardinal) {
        List<AcademicCalendarEntry> children = getChildEntriesWithTemplateEntries(period);
        if (children.size() >= cardinal) {
            return children.get(cardinal - 1);
        }
        return null;
    }

    public AcademicCalendarEntry getEntryForCalendar(final AcademicCalendarRootEntry academicCalendar) {

        AcademicCalendarRootEntry rootEntry = getRootEntry();
        if (rootEntry.equals(academicCalendar)) {
            return this;
        }

        for (final AcademicCalendarEntry basedEntry : getBasedEntriesSet()) {
            final AcademicCalendarEntry basedEntryFor = basedEntry.getEntryForCalendar(academicCalendar);
            if (basedEntryFor != null) {
                return basedEntryFor;
            }
        }

        for (AcademicCalendarEntry otherRoot = academicCalendar; otherRoot != null; otherRoot = otherRoot.getTemplateEntry()) {
            if (otherRoot == rootEntry) {
                return this;
            }
        }

        return null;
    }

    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
        return getBegin().withChronology(academicChronology).get(
                AcademicSemesterOfAcademicYearDateTimeFieldType.academicSemesterOfAcademicYear());
    }

    public TeacherCreditsFillingForTeacherCE getTeacherCreditsFillingForTeacher(AcademicChronology academicChronology) {
        int index = getBegin().withChronology(academicChronology).get(AcademicSemesterDateTimeFieldType.academicSemester());
        AcademicSemesterCE academicSemester = academicChronology.getAcademicSemesterIn(index);
        return academicSemester != null ? academicSemester.getTeacherCreditsFillingForTeacher(academicChronology) : null;
    }

    public TeacherCreditsFillingForDepartmentAdmOfficeCE getTeacherCreditsFillingForDepartmentAdmOffice(
            AcademicChronology academicChronology) {
        int index = getBegin().withChronology(academicChronology).get(AcademicSemesterDateTimeFieldType.academicSemester());
        AcademicSemesterCE academicSemester = academicChronology.getAcademicSemesterIn(index);
        return academicSemester != null ? academicSemester.getTeacherCreditsFillingForDepartmentAdmOffice(academicChronology) : null;
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
        return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public boolean containsInstant(long instant) {
        return getBegin().getMillis() <= instant && getEnd().getMillis() >= instant;
    }

    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
        return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
        List<Interval> result = new ArrayList<Interval>();
        result.add(new Interval(getBegin(), getEnd()));
        return result;
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
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

    public abstract boolean isOfType(AcademicPeriod period);

    public boolean isAcademicYear() {
        return false;
    }

    public boolean isAcademicSemester() {
        return false;
    }

    public boolean isAcademicTrimester() {
        return false;
    }

    public boolean isLessonsPerid() {
        return false;
    }

    public boolean isExamsPeriod() {
        return false;
    }

    public boolean isEnrolmentsPeriod() {
        return false;
    }

    public boolean isGradeSubmissionPeriod() {
        return false;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isTeacherCreditsFilling() {
        return false;
    }

    public boolean isEqualOrEquivalent(AcademicCalendarEntry entry) {
        if (this.equals(entry)) {
            return true;
        }
        if (!getClass().equals(entry.getClass())) {
            return false;
        }
        return getOriginalTemplateEntry().equals(entry.getOriginalTemplateEntry());
    }

    public AcademicCalendarEntry getNextAcademicCalendarEntry() {
        AcademicCalendarEntry closest = null;
        for (AcademicCalendarEntry entry : this.getRootEntry().getAllChildEntriesWithTemplateEntries(this.getClass())) {
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
        for (AcademicCalendarEntry entry : this.getRootEntry().getAllChildEntriesWithTemplateEntries(this.getClass())) {
            if (entry.getBegin().isBefore(this.getBegin())) {
                if (closest == null || entry.getBegin().isAfter(closest.getBegin())) {
                    closest = entry;
                }
            }
        }
        return closest;
    }

    public int getCardinalityOfCalendarEntry(AcademicCalendarEntry child) {
        int count = 1;
        for (AcademicCalendarEntry entry : getChildEntriesWithTemplateEntries(child.getClass())) {
            if (entry.getBegin().isBefore(child.getBegin())) {
                count++;
            }
        }
        return count;
    }
}
