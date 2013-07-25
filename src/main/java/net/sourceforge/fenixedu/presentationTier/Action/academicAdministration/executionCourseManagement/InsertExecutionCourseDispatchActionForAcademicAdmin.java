package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.InsertExecutionCourseDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/insertExecutionCourse", attribute = "insertExecutionCourseForm",
        formBean = "insertExecutionCourseForm", scope = "request", parameter = "method")
@Forwards({
        @Forward(name = "firstPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp"),
        @Forward(name = "insertExecutionCourse",
                path = "/academicAdministration/executionCourseManagement/insertExecutionCourse.jsp") })
public class InsertExecutionCourseDispatchActionForAcademicAdmin extends InsertExecutionCourseDispatchAction {
}
