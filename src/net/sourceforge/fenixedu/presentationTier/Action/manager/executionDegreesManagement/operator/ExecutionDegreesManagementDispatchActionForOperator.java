package net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement.operator;

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

@Mapping(module = "operator", path = "/executionDegreesManagement", input = "/executionDegreesManagementMainPage.do", attribute = "executionDegreesManagementForm", formBean = "executionDegreesManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "manageCoordinators", path = "df.executionDegreeManagement.manageCoordinators"),
		@Forward(name = "insertCoordinator", path = "df.executionDegreeManagement.insertCoordinator"),
		@Forward(name = "editExecutionDegree", path = "df.executionDegreeManagement.editExecutionDegree"),
		@Forward(name = "executionDegreeManagement", path = "df.executionDegreeManagement") })
public class ExecutionDegreesManagementDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement.ExecutionDegreesManagementDispatchAction {
}