package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "operator", path = "/executionCourseManagement", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "mainPage", path = "/operator/welcomeScreen.jsp"),
        @Forward(name = "firstPage", path = "/manager/executionCourseManagement/welcomeScreen.jsp", tileProperties = @Tile(
                navLocal = "/operator/executionCourseManagement/mainMenu.jsp")) })
public class ExecutionCourseManagementActionForOperator extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseManagementAction {
}