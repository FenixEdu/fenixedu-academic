package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.operator;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "operator", path = "/createExecutionCourses", input = "/createExecutionCourses.do?method=chooseDegreeType", attribute = "createExecutionCoursesForm", formBean = "createExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "chooseDegreeCurricularPlans", path = "df.executionCourseManagement.chooseDegreeCurricularPlans"),
		@Forward(name = "chooseDegreeType", path = "df.executionCourseManagement.chooseDegreeType"),
		@Forward(name = "createExecutionCoursesSuccess", path = "df.executionCourseManagement.createExecutionCoursesSuccess") })
public class CreateExecutionCoursesDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.CreateExecutionCoursesDispatchAction {
}