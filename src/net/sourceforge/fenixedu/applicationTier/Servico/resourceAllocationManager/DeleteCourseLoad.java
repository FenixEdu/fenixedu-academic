package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CourseLoad;

public class DeleteCourseLoad extends Service {

    public void run(CourseLoad courseLoad) {
	if(courseLoad != null) {
	    courseLoad.delete();
	}	
    }
}
