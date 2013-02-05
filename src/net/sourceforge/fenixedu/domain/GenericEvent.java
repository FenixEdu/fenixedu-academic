package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.DayType;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class GenericEvent extends GenericEvent_Base implements GanttDiagramEvent {

    public static final Comparator<GenericEvent> COMPARATOR_BY_DATE_AND_TIME = new ComparatorChain();
    private static transient Locale locale = Language.getLocale();

    static {
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endDate"));
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("endTimeDateHourMinuteSecond"));
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("beginDate"));
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(new BeanComparator("startTimeDateHourMinuteSecond"));
        ((ComparatorChain) COMPARATOR_BY_DATE_AND_TIME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public GenericEvent(MultiLanguageString title, MultiLanguageString description, List<AllocatableSpace> allocatableSpaces,
            YearMonthDay beginDate, YearMonthDay endDate, HourMinuteSecond beginTime, HourMinuteSecond endTime,
            FrequencyType frequencyType, PunctualRoomsOccupationRequest request, Boolean markSaturday, Boolean markSunday) {

        super();

        if (allocatableSpaces.isEmpty()) {
            throw new DomainException("error.GenericEvent.empty.rooms");
        }

        if (request != null) {
            if (request.getCurrentState().equals(RequestState.RESOLVED)) {
                throw new DomainException("error.GenericEvent.request.was.resolved");
            }
            request.associateNewGenericEvent(AccessControl.getPerson(), this, new DateTime());
        }

        if (frequencyType != null && frequencyType.equals(FrequencyType.DAILY) && (markSunday == null || markSaturday == null)) {
            throw new DomainException("error.GenericEvent.invalid.dailyFrequency");
        }

        setRootDomainObject(RootDomainObject.getInstance());
        setTitle(title);
        setDescription(description);
        setFrequency(frequencyType);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setStartTimeDateHourMinuteSecond(beginTime);
        setEndTimeDateHourMinuteSecond(endTime);
        setDailyFrequencyMarkSunday(markSunday);
        setDailyFrequencyMarkSaturday(markSaturday);

        for (AllocatableSpace allocatableSpace : allocatableSpaces) {
            new GenericEventSpaceOccupation(allocatableSpace, this);
        }

        if (!hasAnyGenericEventSpaceOccupations()) {
            throw new DomainException("error.GenericEvent.empty.rooms");
        }
    }

    public void edit(MultiLanguageString title, MultiLanguageString description, List<AllocatableSpace> newRooms,
            List<GenericEventSpaceOccupation> roomOccupationsToRemove) {

        // if (getLastInstant().isBeforeNow()) {
        // throw new
        // DomainException("error.edit.GenericEvent.because.was.old.event");
        // }

        if (getPunctualRoomsOccupationRequest() != null
                && getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.RESOLVED)) {
            throw new DomainException("error.edit.GenericEvent.request.was.resolved");
        }

        setTitle(title);
        setDescription(description);

        while (!roomOccupationsToRemove.isEmpty()) {
            roomOccupationsToRemove.remove(0).delete();
        }

        for (AllocatableSpace room : newRooms) {
            new GenericEventSpaceOccupation(room, this);
        }

        if (!hasAnyGenericEventSpaceOccupations()) {
            throw new DomainException("error.GenericEvent.empty.rooms");
        }
    }

    public void delete() {

        // if (getLastInstant().isBeforeNow()) {
        // throw new
        // DomainException("error.GenericEvent.impossible.delete.because.was.old.event");
        // }

        if (getPunctualRoomsOccupationRequest() != null
                && getPunctualRoomsOccupationRequest().getCurrentState().equals(RequestState.RESOLVED)) {
            throw new DomainException("error.GenericEvent.request.was.resolved");
        }

        // if (getFirstInstant().isAfterNow()) {

        while (hasAnyGenericEventSpaceOccupations()) {
            getGenericEventSpaceOccupations().get(0).delete();
        }

        removePunctualRoomsOccupationRequest();
        removeRootDomainObject();
        deleteDomainObject();

        // Allow GOP to delete stuff in the past!
        // } else {
        //
        // for (GenericEventSpaceOccupation spaceOccupation :
        // getGenericEventSpaceOccupations()) {
        // spaceOccupation.verifyIfIsPossibleCloseGenericEvent();
        // }
        //
        // if (getStartTimeDateHourMinuteSecond().isAfter(new
        // HourMinuteSecond())) {
        // setEndDate(new YearMonthDay().minusDays(1));
        // } else {
        // setEndDate(new YearMonthDay());
        // }
        // }
    }

    @Override
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            throw new DomainException("error.genericCalendar.empty.title");
        }
        super.setTitle(title);
    }

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
        if (beginDate == null) {
            throw new DomainException("error.GenericEvent.empty.beginDate");
        }
        super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        if (endDate == null) {
            throw new DomainException("error.GenericEvent.empty.endDate");
        }
        super.setEndDate(endDate);
    }

    @Override
    public void setStartTimeDateHourMinuteSecond(HourMinuteSecond startTimeDateHourMinuteSecond) {
        if (startTimeDateHourMinuteSecond == null) {
            throw new DomainException("error.GenericEvent.empty.startTime");
        }
        super.setStartTimeDateHourMinuteSecond(startTimeDateHourMinuteSecond);
    }

    @Override
    public void setEndTimeDateHourMinuteSecond(HourMinuteSecond endTimeDateHourMinuteSecond) {
        if (endTimeDateHourMinuteSecond == null) {
            throw new DomainException("error.GenericEvent.empty.endTime");
        }
        super.setEndTimeDateHourMinuteSecond(endTimeDateHourMinuteSecond);
    }

    public static Set<GenericEvent> getActiveGenericEventsForRoomOccupations() {
        Set<GenericEvent> result = new TreeSet<GenericEvent>(COMPARATOR_BY_DATE_AND_TIME);
        for (GenericEvent genericEvent : RootDomainObject.getInstance().getGenericEvents()) {
            if (genericEvent.isActive()) {
                result.add(genericEvent);
            }
        }
        return result;
    }

    public boolean isActive() {
        DateTime lastInstant = getLastInstant();
        return (lastInstant == null) || (lastInstant != null && !lastInstant.isBeforeNow()) ? true : false;
    }

    public List<AllocatableSpace> getAssociatedRooms() {
        List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (GenericEventSpaceOccupation occupation : getGenericEventSpaceOccupations()) {
            result.add(occupation.getRoom());
        }
        return result;
    }

    public boolean constainsRoom(AllocatableSpace room) {
        for (GenericEventSpaceOccupation occupation : getGenericEventSpaceOccupations()) {
            if (occupation.getRoom().equals(room)) {
                return true;
            }
        }
        return false;
    }

    public List<Interval> getGenericEventIntervals(YearMonthDay begin, YearMonthDay end) {
        if (!getGenericEventSpaceOccupations().isEmpty()) {
            GenericEventSpaceOccupation occupation = getGenericEventSpaceOccupations().get(0);
            return occupation.getEventSpaceOccupationIntervals(begin, end);
        }
        return Collections.emptyList();
    }

    public boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return !getBeginDate().isAfter(endDate) && !getEndDate().isBefore(startDate);
    }

    public DateTime getLastInstant() {
        return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getLastInstant() : null;
    }

    public DateTime getFirstInstant() {
        return (!getGenericEventSpaceOccupations().isEmpty()) ? getGenericEventSpaceOccupations().get(0).getFirstInstant() : null;
    }

    public Calendar getBeginTimeCalendar() {
        Calendar result = Calendar.getInstance();
        result.setTime(getStartTimeDate());
        return result;
    }

    public Calendar getEndTimeCalendar() {
        Calendar result = Calendar.getInstance();
        result.setTime(getEndTimeDate());
        return result;
    }

    public String getPresentationBeginTime() {
        return getStartTimeDateHourMinuteSecond().toString("HH:mm");
    }

    public String getPresentationEndTime() {
        return getEndTimeDateHourMinuteSecond().toString("HH:mm");
    }

    public String getPresentationBeginDate() {
        return getBeginDate().toString("dd MMMM yyyy", locale) + " ("
                + getBeginDate().toDateTimeAtMidnight().toString("E", locale) + ")";
    }

    public String getPresentationEndDate() {
        return getEndDate().toString("dd MMMM yyyy", locale) + " (" + getEndDate().toDateTimeAtMidnight().toString("E", locale)
                + ")";
    }

    @Override
    public String getGanttDiagramEventIdentifier() {
        return getIdInternal().toString();
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
        return getTitle();
    }

    @Override
    public String getGanttDiagramEventObservations() {
        StringBuilder builder = new StringBuilder();
        for (GenericEventSpaceOccupation roomOccupation : getGenericEventSpaceOccupations()) {
            builder.append(" ").append(roomOccupation.getRoom().getName());
        }
        return builder.toString();
    }

    @Override
    public int getGanttDiagramEventOffset() {
        return 0;
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
        if (!getGenericEventSpaceOccupations().isEmpty()) {
            return getGenericEventSpaceOccupations().get(0).getEventSpaceOccupationIntervals((YearMonthDay) null,
                    (YearMonthDay) null);
        }
        return Collections.emptyList();
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

    public String getPeriodPrettyPrint() {
        String prettyPrint = new String();
        if (!getGenericEventSpaceOccupations().isEmpty()) {
            prettyPrint = getGenericEventSpaceOccupations().get(0).getPrettyPrint();
        }
        return prettyPrint;
    }

    @Override
    public String getGanttDiagramEventPeriod() {
        if (!getGenericEventSpaceOccupations().isEmpty()) {
            String prettyPrint = getGenericEventSpaceOccupations().get(0).getPrettyPrint();
            if (getFrequency() != null) {
                String saturday = "", sunday = "", marker = "";
                if (getFrequency().equals(FrequencyType.DAILY)) {
                    saturday = getDailyFrequencyMarkSaturday() ? "S" : "";
                    sunday = getDailyFrequencyMarkSunday() ? "D" : "";
                    if (getDailyFrequencyMarkSaturday() || getDailyFrequencyMarkSunday()) {
                        marker = "-";
                    }
                }
                return "[" + getFrequency().getAbbreviation() + marker + saturday + sunday + "] " + prettyPrint;
            }
            return "[C] " + prettyPrint;
        }
        return " - ";
    }

    private boolean intersectPeriod(DateTime firstDayOfMonth, DateTime lastDayOfMonth) {
        for (Interval interval : getGanttDiagramEventSortedIntervals()) {
            if (interval.getStart().isAfter(lastDayOfMonth)) {
                return false;
            }
            if (interval.getStart().isBefore(lastDayOfMonth) && interval.getEnd().isAfter(firstDayOfMonth)) {
                return true;
            }
        }
        return false;
    }

    public static Set<GenericEvent> getAllGenericEvents(DateTime begin, DateTime end, AllocatableSpace allocatableSpace) {
        Set<GenericEvent> events = new TreeSet<GenericEvent>(GenericEvent.COMPARATOR_BY_DATE_AND_TIME);
        for (GenericEvent genericEvent : RootDomainObject.getInstance().getGenericEvents()) {
            if (genericEvent.intersectPeriod(begin, end)
                    && (allocatableSpace == null || genericEvent.constainsRoom(allocatableSpace))) {
                events.add(genericEvent);
            }
        }
        return events;
    }

    protected DateTime getBeginDateTime() {
        final YearMonthDay beginDate = getBeginDate();
        final HourMinuteSecond startTimeDateHourMinuteSecond = getStartTimeDateHourMinuteSecond();
        return getDateTime(beginDate, startTimeDateHourMinuteSecond);
    }

    protected DateTime getEndDateTime() {
        final YearMonthDay endDate = getEndDate();
        final HourMinuteSecond endTimeDateHourMinuteSecond = getEndTimeDateHourMinuteSecond();
        return getDateTime(endDate, endTimeDateHourMinuteSecond);
    }

    protected DateTime getDateTime(final YearMonthDay yearMonthDay, final HourMinuteSecond hourMinuteSecond) {
        return yearMonthDay == null || hourMinuteSecond == null ? null : new DateTime(yearMonthDay.getYear(),
                yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth(), hourMinuteSecond.getHour(),
                hourMinuteSecond.getMinuteOfHour(), hourMinuteSecond.getSecondOfMinute(), 0);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeIntervals() {
        final DateTime begin = getBeginDateTime();
        final DateTime end = getEndDateTime();
        return begin != null && end != null && end.isAfter(begin);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getTitle() != null
                && !getTitle().isEmpty()
                && (getFrequency() == null || !getFrequency().equals(FrequencyType.DAILY) || (getDailyFrequencyMarkSaturday() != null && getDailyFrequencyMarkSunday() != null));
    }

    @Deprecated
    public java.util.Date getStartTimeDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getStartTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartTimeDate(java.util.Date date) {
        if (date == null) {
            setStartTimeDateHourMinuteSecond(null);
        } else {
            setStartTimeDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndTimeDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEndTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndTimeDate(java.util.Date date) {
        if (date == null) {
            setEndTimeDateHourMinuteSecond(null);
        } else {
            setEndTimeDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

}
