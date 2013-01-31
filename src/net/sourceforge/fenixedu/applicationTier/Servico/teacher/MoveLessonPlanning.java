package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.LessonPlanning;

public class MoveLessonPlanning extends FenixService {

	public void run(Integer executionCourseID, LessonPlanning lessonPlanning, Integer order) {
		lessonPlanning.moveTo(order);
	}
}
