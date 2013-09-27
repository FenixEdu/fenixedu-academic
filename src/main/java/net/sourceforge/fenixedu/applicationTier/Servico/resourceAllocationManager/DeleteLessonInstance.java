package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.SortedSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.predicates.RolePredicates;

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