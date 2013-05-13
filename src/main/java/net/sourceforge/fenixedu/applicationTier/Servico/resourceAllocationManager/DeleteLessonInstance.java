package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.SortedSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.domain.Lesson;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteLessonInstance extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(Lesson lesson, YearMonthDay day) {
        if (lesson != null && day != null) {
            lesson.deleteLessonInstanceIn(day);
        }
    }

    @Service
    public static void run(final SortedSet<NextPossibleSummaryLessonsAndDatesBean> set) {
        final NextPossibleSummaryLessonsAndDatesBean last = set.last();
        last.getLesson().refreshPeriodAndInstancesInSummaryCreation(last.getDate().plusDays(1));
        for (final NextPossibleSummaryLessonsAndDatesBean n : set) {
            run(n.getLesson(), n.getDate());
        }
    }
}