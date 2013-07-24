package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/executionCourseManagementStart", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "mainPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp"),
        @Forward(name = "firstPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp") })
public class ExecutionCourseManagementActionForAcademicAdmin extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseManagementAction {
}
