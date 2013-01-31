package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "operator",
		path = "/seperateExecutionCourse",
		input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&page=0",
		attribute = "sepeateExecutionCourseForm",
		formBean = "sepeateExecutionCourseForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "returnFromTransfer",
				path = "/editExecutionCourseChooseExPeriod.do?method=prepareEditECChooseExecutionPeriod"),
		@Forward(name = "showSeperationPage", path = "df.executionCourseManagement.seperateExecutionCourse") })
public class SeperateExecutionCourseDispatchActionForOperator extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.SeperateExecutionCourseDispatchAction {
}