package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class ImportEvaluationMethod extends FenixService {

	public void run(Integer executionCourseToId, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
			Shift shift) {
		if (executionCourseTo != null && executionCourseFrom != null) {
			executionCourseTo.copyEvaluationMethodFrom(executionCourseFrom);
		}
	}

}
