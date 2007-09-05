package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;

public class DeleteLessonInstance extends Service {

    public void run(Lesson lesson, YearMonthDay day) {
	if(lesson != null && day != null) {
	    lesson.deleteLessonInstanceIn(day);
	}
    }
}
