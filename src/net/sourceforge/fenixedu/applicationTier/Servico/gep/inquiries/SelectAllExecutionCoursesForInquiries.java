package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;

public class SelectAllExecutionCoursesForInquiries extends Service {

    public void run(final ExecutionCourseSearchBean executionCourseSearchBean) {
	for (final ExecutionCourse executionCourse : executionCourseSearchBean.search()) {
	    executionCourse.setAvailableForInquiries(Boolean.TRUE);
	}
    }

}
