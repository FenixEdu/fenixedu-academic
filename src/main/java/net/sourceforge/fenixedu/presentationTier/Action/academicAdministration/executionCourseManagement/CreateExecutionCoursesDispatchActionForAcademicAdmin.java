package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/createExecutionCourses",
        input = "/createExecutionCourses.do?method=chooseDegreeType", attribute = "createExecutionCoursesForm",
        formBean = "createExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseDegreeCurricularPlans",
                path = "/academicAdministration/executionCourseManagement/chooseDegreeCurricularPlans.jsp"),
        @Forward(name = "chooseDegreeType", path = "/academicAdministration/executionCourseManagement/chooseDegreeType.jsp"),
        @Forward(name = "createExecutionCoursesSuccess",
                path = "/academicAdministration/executionCourseManagement/createExecutionCoursesSuccess.jsp") })
public class CreateExecutionCoursesDispatchActionForAcademicAdmin extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.CreateExecutionCoursesDispatchAction {
}
