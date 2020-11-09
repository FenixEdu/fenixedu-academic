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
package org.fenixedu.academic.service.services.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class ImportLessonPlannings {

    protected void run(String executionCourseID, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
            Shift shift) {
        if (executionCourseTo != null && executionCourseFrom != null) {
            if (shift == null) {
                LessonPlanning.copyLessonPlanningsFrom(executionCourseFrom, executionCourseTo);
            } else {
                createLessonPlanningsUsingSummariesFrom(executionCourseTo, shift);
            }
        }
    }

    private void createLessonPlanningsUsingSummariesFrom(ExecutionCourse executionCourseTo, Shift shift) {
        List<Summary> summaries = new ArrayList<Summary>();
        summaries.addAll(shift.getAssociatedSummariesSet());
        Collections.sort(summaries, new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
        for (Summary summary : summaries) {
            for (ShiftType shiftType : shift.getTypes()) {
                new LessonPlanning(summary.getTitle(), summary.getSummaryText(), shiftType, executionCourseTo);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportLessonPlannings serviceInstance = new ImportLessonPlannings();

    @Atomic
    public static void runImportLessonPlannings(String executionCourseID, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, executionCourseTo, executionCourseFrom, shift);
    }

}