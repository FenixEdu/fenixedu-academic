package net.sourceforge.fenixedu.presentationTier.Action.teacher.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/getTabSeparatedStudentList", scope = "request")
public class DownloadStudentsWithAttendsByExecutionCourseListActionForCoordinator extends
		net.sourceforge.fenixedu.presentationTier.Action.teacher.DownloadStudentsWithAttendsByExecutionCourseListAction {
}