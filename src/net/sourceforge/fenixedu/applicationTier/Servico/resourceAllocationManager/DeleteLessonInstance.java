package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Lesson;

import org.joda.time.YearMonthDay;

public class DeleteLessonInstance extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(Lesson lesson, YearMonthDay day) {
	if (lesson != null && day != null) {
	    lesson.deleteLessonInstanceIn(day);
	}
    }
}