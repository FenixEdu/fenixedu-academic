package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;

import org.joda.time.YearMonthDay;

public class DeleteLessonInstance extends Service {

    public void run(Lesson lesson, YearMonthDay day) {
	if(lesson != null && day != null) {
	    lesson.deleteLessonInstanceIn(day);
	}
    }
}
