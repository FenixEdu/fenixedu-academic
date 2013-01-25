package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/seperateExecutionCourse", input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&page=0", attribute = "sepeateExecutionCourseForm", formBean = "sepeateExecutionCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "returnFromTransfer", path = "/manager/executionCourseManagement/listExecutionCourseActions.jsp"),
	@Forward(name = "showSeperationPage", path = "/manager/executionCourseManagement/seperateExecutionCourse.jsp") })
public class SeperateExecutionCourseDispatchActionForManager extends
	net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.SeperateExecutionCourseDispatchAction {
}