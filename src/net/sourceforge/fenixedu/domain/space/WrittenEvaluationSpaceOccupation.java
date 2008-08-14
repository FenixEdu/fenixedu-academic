package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class WrittenEvaluationSpaceOccupation extends WrittenEvaluationSpaceOccupation_Base {

    // @Checked(
    // "SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations"
    // )
    public WrittenEvaluationSpaceOccupation(AllocatableSpace allocatableSpace) {

	super();

	ResourceAllocation allocation = allocatableSpace
		.getFirstOccurrenceOfResourceAllocationByClass(WrittenEvaluationSpaceOccupation.class);
	if (allocation != null) {
	    throw new DomainException("error.WrittenEvaluationSpaceOccupation.occupation.for.this.space.already.exists");
	}

	setResource(allocatableSpace);
    }

    // @Checked(
    // "SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations"
    // )
    public void edit(WrittenEvaluation writtenEvaluation) {

	if (hasWrittenEvaluations(writtenEvaluation)) {
	    removeWrittenEvaluations(writtenEvaluation);
	}

	if (!writtenEvaluation.canBeAssociatedToRoom(getRoom())) {
	    throw new DomainException("error.roomOccupied", getRoom().getName());
	}

	addWrittenEvaluations(writtenEvaluation);
    }

    @Checked("SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations")
    public void delete() {
	if (canBeDeleted()) {
	    super.delete();
	}
    }

    private boolean canBeDeleted() {
	return !hasAnyWrittenEvaluations();
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {

	List<Interval> result = new ArrayList<Interval>();
	List<WrittenEvaluation> writtenEvaluations = getWrittenEvaluations();

	for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
	    YearMonthDay writtenEvaluationDay = writtenEvaluation.getDayDateYearMonthDay();
	    if (startDateToSearch == null
		    || (!writtenEvaluationDay.isBefore(startDateToSearch) && !writtenEvaluationDay.isAfter(endDateToSearch))) {

		result.add(createNewInterval(writtenEvaluationDay, writtenEvaluationDay, writtenEvaluation
			.getBeginningDateHourMinuteSecond(), writtenEvaluation.getEndDateHourMinuteSecond()));
	    }
	}
	return result;
    }

    @Override
    public boolean isWrittenEvaluationSpaceOccupation() {
	return true;
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
	return true;
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();
    }

    @Override
    public YearMonthDay getBeginDate() {
	return null;
    }

    @Override
    public YearMonthDay getEndDate() {
	return null;
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return null;
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return null;
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
    public DiaSemana getDayOfWeek() {
	return null;
    }

    @Override
    public FrequencyType getFrequency() {
	return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(final ExecutionCourse executionCourse, final DateTime start, final DateTime end) {
	for (final WrittenEvaluation writtenEvaluation : getWrittenEvaluationsSet()) {
	    if (writtenEvaluation.getAssociatedExecutionCoursesSet().contains(executionCourse)
		    && start.isBefore(writtenEvaluation.getEndDateTime())
		    && end.isAfter(writtenEvaluation.getBeginningDateTime())) {
		return true;
	    }
	}
	return false;
    }

}
