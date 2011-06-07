package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;

public class ExecutionCourse1st2ndCycleSearchBean extends ExecutionCourseSearchBean {

    private static final long serialVersionUID = 1L;
    private ExecutionCourse selectedExecutionCourse;

    public void setSelectedExecutionCourse(ExecutionCourse selectedExecutionCourse) {
	this.selectedExecutionCourse = selectedExecutionCourse;
    }

    public ExecutionCourse getSelectedExecutionCourse() {
	return selectedExecutionCourse;
    }
}
