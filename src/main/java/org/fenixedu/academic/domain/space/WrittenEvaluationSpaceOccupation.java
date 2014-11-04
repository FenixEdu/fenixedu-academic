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
package org.fenixedu.academic.domain.space;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.SpacePredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class WrittenEvaluationSpaceOccupation extends WrittenEvaluationSpaceOccupation_Base {

    // @Checked(
    // "SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations"
    // )
    public WrittenEvaluationSpaceOccupation(Space allocatableSpace) {

        super();

//        Occupation allocation =
//                SpaceUtils
//                .getFirstOccurrenceOfResourceAllocationByClass(allocatableSpace, WrittenEvaluationSpaceOccupation.class);
//        if (allocation != null) {
//            throw new DomainException("error.WrittenEvaluationSpaceOccupation.occupation.for.this.space.already.exists");
//        }

        setResource(allocatableSpace);
    }

    // @Checked(
    // "SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations"
    // )
    public void edit(WrittenEvaluation writtenEvaluation) {

        if (getWrittenEvaluationsSet().contains(writtenEvaluation)) {
            removeWrittenEvaluations(writtenEvaluation);
        }

        if (!writtenEvaluation.canBeAssociatedToRoom(getRoom())) {
            throw new DomainException("error.roomOccupied", getRoom().getName());
        }

        addWrittenEvaluations(writtenEvaluation);
    }

    @Override
    public void delete() {
        check(this, SpacePredicates.checkPermissionsToManageWrittenEvaluationSpaceOccupations);
        if (getDeletionBlockers().isEmpty()) {
            super.delete();
        }
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getWrittenEvaluationsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.cannotDeleteWrittenEvaluationSpaceOccupation"));
        }
    }

    @Override
    public List<Interval> getEventSpaceOccupationIntervals(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {

        List<Interval> result = new ArrayList<Interval>();
        Collection<WrittenEvaluation> writtenEvaluations = getWrittenEvaluationsSet();

        for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
            YearMonthDay writtenEvaluationDay = writtenEvaluation.getDayDateYearMonthDay();
            if (startDateToSearch == null
                    || (!writtenEvaluationDay.isBefore(startDateToSearch) && !writtenEvaluationDay.isAfter(endDateToSearch))) {

                result.add(createNewInterval(writtenEvaluationDay, writtenEvaluationDay,
                        writtenEvaluation.getBeginningDateHourMinuteSecond(), writtenEvaluation.getEndDateHourMinuteSecond()));
            }
        }
        return result;
    }

    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return true;
    }

    @Override
    public Group getAccessGroup() {
        return getSpace().getOccupationsGroupWithChainOfResponsability();
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

    @Override
    public String getPresentationString() {
        if (getWrittenEvaluationsSet().isEmpty()) {
            return StringUtils.EMPTY;
        }
        final WrittenEvaluation eval = getWrittenEvaluationsSet().iterator().next();
        return String.format("(%s) %s", eval.getEvaluationType(), eval.getName());
    }

    @Override
    protected boolean overlaps(final Interval interval) {
        for (final WrittenEvaluation writtenEvaluation : getWrittenEvaluationsSet()) {
            final Interval evaluationInterval = writtenEvaluation.getInterval();
            if (interval.overlaps(evaluationInterval)) {
                return true;
            }
        }
        return false;
    }

}
