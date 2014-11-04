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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.SortedSet;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.dto.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import org.fenixedu.academic.predicate.RolePredicates;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class DeleteLessonInstance {

    @Atomic
    public static void run(Lesson lesson, YearMonthDay day) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (lesson != null && day != null) {
            lesson.deleteLessonInstanceIn(day);
        }
    }

    @Atomic
    public static void run(final SortedSet<NextPossibleSummaryLessonsAndDatesBean> set) {
        final NextPossibleSummaryLessonsAndDatesBean last = set.last();
        final Lesson lesson = last.getLesson();
        final YearMonthDay date = last.getDate();
        lesson.refreshPeriodAndInstancesInSummaryCreation(lesson.isBiWeeklyOffset() ? date.plusDays(8) : date.plusDays(1));
        for (final NextPossibleSummaryLessonsAndDatesBean n : set) {
            run(n.getLesson(), n.getDate());
        }
    }
}