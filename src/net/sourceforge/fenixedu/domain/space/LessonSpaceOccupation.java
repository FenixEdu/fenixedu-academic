package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class LessonSpaceOccupation extends LessonSpaceOccupation_Base {

    @Checked("SpacePredicates.checkPermissionsToManageLessonSpaceOccupations")
    public LessonSpaceOccupation(AllocatableSpace allocatableSpace, Lesson lesson) {

	super();

	if (lesson != null && lesson.getLessonSpaceOccupation() != null) {
	    throw new DomainException("error.lesson.already.has.lessonSpaceOccupation");
	}

	setLesson(lesson);

	if (getPeriod() == null) {
	    throw new DomainException("error.LessonSpaceOccupation.empty.period");
	}

	if (allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getIdentification(),
		    getPeriod().getStartYearMonthDay().toString("dd-MM-yyy"), getPeriod()
			    .getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay().toString("dd-MM-yyy"));
	}

	setResource(allocatableSpace);
    }

    @Checked("SpacePredicates.checkPermissionsToManageLessonSpaceOccupations")
    public void edit(AllocatableSpace allocatableSpace) {

	if (getPeriod() == null) {
	    throw new DomainException("error.LessonSpaceOccupation.empty.period");
	}

	if (allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.LessonSpaceOccupation.room.is.not.free", allocatableSpace.getIdentification(),
		    getPeriod().getStartYearMonthDay().toString("dd-MM-yyy"), getPeriod()
			    .getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay().toString("dd-MM-yyy"));
	}

	setResource(allocatableSpace);
    }

    @Checked("SpacePredicates.checkPermissionsToDeleteLessonSpaceOccupations")
    public void delete() {
	super.setLesson(null);
	super.delete();
    }

    public OccupationPeriod getPeriod() {
	return getLesson().getPeriod();
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
	return getPeriod().nestedOccupationPeriodsIntersectDates(startDate, endDate);
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {

	List<Interval> result = new ArrayList<Interval>();
	OccupationPeriod occupationPeriod = getPeriod();

	if (getPeriod() != null) {

	    result.addAll(generateEventSpaceOccupationIntervals(occupationPeriod.getStartYearMonthDay(), occupationPeriod
		    .getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(), getFrequency(),
		    getDayOfWeek(), getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday(), startDateToSearch,
		    endDateToSearch));

	    while (occupationPeriod.getNextPeriod() != null) {
		result.addAll(generateEventSpaceOccupationIntervals(occupationPeriod.getNextPeriod().getStartYearMonthDay(),
			occupationPeriod.getNextPeriod().getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(),
			getEndTimeDateHourMinuteSecond(), getFrequency(), getDayOfWeek(), getDailyFrequencyMarkSaturday(),
			getDailyFrequencyMarkSunday(), startDateToSearch, endDateToSearch));

		occupationPeriod = occupationPeriod.getNextPeriod();
	    }
	}

	return result;
    }

    @Override
    public boolean isLessonSpaceOccupation() {
	return true;
    }

    @Override
    public void setLesson(Lesson lesson) {
	if (lesson == null) {
	    throw new DomainException("error.LessonSpaceOccupation.empty.lesson");
	}
	super.setLesson(lesson);
    }

    @Override
    public FrequencyType getFrequency() {
	return getLesson().getFrequency();
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getLessonOccupationsAccessGroupWithChainOfResponsibility();
    }

    @Override
    public YearMonthDay getBeginDate() {
	return getPeriod() != null ? getPeriod().getStartYearMonthDay() : null;
    }

    @Override
    public YearMonthDay getEndDate() {
	return getPeriod() != null ? getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay() : null;
    }

    @Override
    public DiaSemana getDayOfWeek() {
	return getLesson().getDiaSemana();
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return getLesson().getBeginHourMinuteSecond();
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return getLesson().getEndHourMinuteSecond();
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start, final DateTime end) {
	final Lesson lesson = getLesson();
	if (lesson.getExecutionCourse() == executionCourse) {
	    final List<Interval> intervals = getEventSpaceOccupationIntervals(start.toYearMonthDay(), end.toYearMonthDay()
		    .plusDays(1));
	    for (final Interval interval : intervals) {
		if (start.isBefore(interval.getEnd()) && end.isAfter(interval.getStart())) {
		    return true;
		}
	    }
	}
	return false;
    }

}
