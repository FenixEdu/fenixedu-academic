package net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement.manager;

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

@Mapping(module = "manager", path = "/executionDegreesManagement", input = "/executionDegreesManagementMainPage.do", attribute = "executionDegreesManagementForm", formBean = "executionDegreesManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "manageCoordinators", path = "df.executionDegreeManagement.manageCoordinators"),
		@Forward(name = "insertCoordinator", path = "df.executionDegreeManagement.insertCoordinator"),
		@Forward(name = "editExecutionDegree", path = "df.executionDegreeManagement.editExecutionDegree"),
		@Forward(name = "executionDegreeManagement", path = "df.executionDegreeManagement") })
public class ExecutionDegreesManagementDispatchActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement.ExecutionDegreesManagementDispatchAction {
}