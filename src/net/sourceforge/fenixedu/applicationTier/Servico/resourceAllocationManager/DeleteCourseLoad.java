package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CourseLoad;

public class DeleteCourseLoad extends FenixService {

    public void run(CourseLoad courseLoad) {
	if (courseLoad != null) {
	    courseLoad.delete();
	}
    }
}
