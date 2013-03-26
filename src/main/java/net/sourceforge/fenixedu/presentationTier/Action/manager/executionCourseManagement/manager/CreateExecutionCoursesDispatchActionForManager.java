package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/createExecutionCourses", input = "/createExecutionCourses.do?method=chooseDegreeType",
        attribute = "createExecutionCoursesForm", formBean = "createExecutionCoursesForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseDegreeCurricularPlans",
                path = "/manager/executionCourseManagement/chooseDegreeCurricularPlans.jsp"),
        @Forward(name = "chooseDegreeType", path = "/manager/executionCourseManagement/chooseDegreeType.jsp"),
        @Forward(name = "createExecutionCoursesSuccess",
                path = "/manager/executionCourseManagement/createExecutionCoursesSuccess.jsp") })
public class CreateExecutionCoursesDispatchActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.CreateExecutionCoursesDispatchAction {
}