package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.SpacePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GenericEventSpaceOccupation extends GenericEventSpaceOccupation_Base {

    public GenericEventSpaceOccupation(AllocatableSpace allocatableSpace, GenericEvent genericEvent) {
//        check(this, SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations);

        super();

        setGenericEvent(genericEvent);

        if (allocatableSpace != null && !allocatableSpace.isFree(this)) {
            throw new DomainException("error.roomOccupation.room.is.not.free");
        }

        setResource(allocatableSpace);
    }

    @Override
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations);
        super.setGenericEvent(null);
        super.delete();
    }

    public void verifyIfIsPossibleCloseGenericEvent() {
        check(this, SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations);
    }

    @Override
    public void setGenericEvent(GenericEvent genericEvent) {
        if (genericEvent == null) {
            throw new DomainException("error.GenericEventSpaceOccupation.empty.genericEvent");
        }
        super.setGenericEvent(genericEvent);
    }

    @Override
    public boolean isGenericEventSpaceOccupation() {
        return true;
    }

    @Override
    public Group getAccessGroup() {
        return getSpace().getGenericEventOccupationsAccessGroupWithChainOfResponsibility();
    }

    @Override
    public FrequencyType getFrequency() {
        return getGenericEvent().getFrequency();
    }

    @Override
    public YearMonthDay getBeginDate() {
        return getGenericEvent() == null ? null : getGenericEvent().getBeginDate();
    }

    @Override
    public YearMonthDay getEndDate() {
        return getGenericEvent().getEndDate();
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
        return getGenericEvent().getDailyFrequencyMarkSaturday();
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
        return getGenericEvent().getDailyFrequencyMarkSunday();
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
        return getGenericEvent().getStartTimeDateHourMinuteSecond();
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
        return getGenericEvent().getEndTimeDateHourMinuteSecond();
    }

    @Override
    public DiaSemana getDayOfWeek() {
        return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(ExecutionCourse executionCourse, DateTime start, DateTime end) {
        return false;
    }

    @Override
    public String getPresentationString() {
        return getGenericEvent().getGanttDiagramEventName().getContent();
    }

    @Deprecated
    public boolean hasGenericEvent() {
        return getGenericEvent() != null;
    }

    @Override
    protected boolean overlaps(final Interval interval) {
        final DateTime start = interval.getStart();
        final DateTime end = interval.getEnd();
        if (alreadyWasOccupiedIn(start.toYearMonthDay(), end.toYearMonthDay(), new HourMinuteSecond(start.toDate()),
                new HourMinuteSecond(end.toDate()), null, null, null, null)) {
            return true;
        }
        return false;
    }

}
